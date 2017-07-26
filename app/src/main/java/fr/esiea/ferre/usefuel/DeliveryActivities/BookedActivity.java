package fr.esiea.ferre.usefuel.DeliveryActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.esiea.ferre.usefuel.Objects.OrderFuel;
import fr.esiea.ferre.usefuel.R;
import fr.esiea.ferre.usefuel.UserActivities.LoadingScreenBookActivity;
import fr.esiea.ferre.usefuel.UserActivities.MapActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by rob on 7/26/2017.
 */

public class BookedActivity extends AppCompatActivity {
    Button button_cancel;
    TextView text;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    String u_uid;
    String username;
    OrderFuel order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);

        Bundle extras = getIntent().getExtras();
        if (extras == null){
            return;
        }
        String value_user_type = extras.getString("value1");
        if (value_user_type != null) {
            u_uid = value_user_type;
        }


        button_cancel = (Button)findViewById(R.id.button_cancel);
        text = (TextView)findViewById(R.id.text);

        // Set up of the Calligraphy dependencies, that allows us to use a custom font in a .xml layout file
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        final String uid = FireUser.getUid().toString();
        mDatabase.child("users").child(u_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("username").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
            }
        });
        mDatabase.child("orders").child(u_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order = dataSnapshot.getValue(OrderFuel.class);
                text.setText(   username + " parking at\n"
                                + order.getAddress() + "\n\n"
                                + order.getCar().getBrand() + " " + order.getCar().getColor()  + "\n"
                                + "-----" + order.getCar().getNumberPlate() + "\n\n"
                                + order.getFuelQuantity() + "L of " + order.getCar().getFuelType() +  "\n\n"
                                + order.getPrice());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
            }
        });
    }

    // Block the return button
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    // Calligraphy dependencies required that
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void onCancel(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser FireUser = firebaseAuth.getCurrentUser();

        final AlertDialog myDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to cancel this delivery ?");
        builder.setIcon(R.drawable.ic_cancel);
        builder.setCancelable(true);
        builder.setMessage("Your client is waiting for you\nCanceling to much delivery will affect your visibility");
        //Button to decide what to do next
        builder.setPositiveButton("Cancel anyway", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("orders").child(u_uid).child("status").setValue("canceled");
                finish();
            }
        });
        //Button to cancel
        builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing, cancel
            }
        });
        myDialog = builder.create();
        myDialog.show();
    }

    public void onMap(View view){
        String strUri = "http://maps.google.com/maps?q=loc:" + order.getLat() + "," + order.getLng() + " (" + order.getAddress() + ")";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

        startActivity(intent);
    }

    public void onConfirm(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser FireUser = firebaseAuth.getCurrentUser();

        final AlertDialog myDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Have you delivered the fuel ?");
        builder.setIcon(R.drawable.ic_done);
        builder.setCancelable(true);
        builder.setMessage("Once your client have received his fuel\nAnd when you have been paid\nYou can confirm the delivery");
        //Button to decide what to do next
        builder.setPositiveButton("Confirm delivery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("orders").child(u_uid).child("status").setValue("finished");
                finish();
            }
        });
        //Button to cancel
        builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing, cancel
            }
        });
        myDialog = builder.create();
        myDialog.show();
    }


}
