package fr.esiea.ferre.usefuel.UserActivities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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

public class UserBookedActivity extends AppCompatActivity {

    TextView text;
    TextView textPerc;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    LatLng d_latlng;
    String username;
    OrderFuel order;
    String d_uid;
    int percentage = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_user);

        Bundle extras = getIntent().getExtras();
        if (extras == null){
            return;
        }
        String value_user_type = extras.getString("value1");
        if (value_user_type != null) {
            d_uid = value_user_type;
        }

        text = (TextView)findViewById(R.id.text);
        textPerc = (TextView)findViewById(R.id.text_perc);

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

        mDatabase.child("orders").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order = dataSnapshot.getValue(OrderFuel.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
            }
        });
        Log.d("firebase", d_uid);
        mDatabase.child("users").child(d_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("username").getValue(String.class);
                text.setText(   username + " is bringing your fuel at\n"
                        + order.getAddress() + "\n\n"
                        + "for your " + order.getCar().getBrand() + " " + order.getCar().getColor()  + "\n"
                        + "-----" + order.getCar().getNumberPlate() + "\n\n"
                        + order.getFuelQuantity() + "L of " + order.getCar().getFuelType() +  "\n\n"
                        + order.getPrice());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
            }
        });
        mDatabase.child("deliverer_info").child(d_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textPerc.setText("Remaining Time");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
            }
        });
        mDatabase.child("orders").child(uid).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class).equals("canceled")){
                    Toast.makeText(UserBookedActivity.this ,"Canceled by the deliverer",Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(dataSnapshot.getValue(String.class).equals("finished")){
                    Toast.makeText(UserBookedActivity.this ,"Delivery completed",Toast.LENGTH_LONG).show();
                    finish();
                }
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
        final String uid = FireUser.getUid().toString();

        final AlertDialog myDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to cancel this delivery ?");
        builder.setIcon(R.drawable.ic_cancel);
        builder.setCancelable(true);
        builder.setMessage("Your deliverer is coming for you\nCanceling to much delivery will affect your visibility");
        //Button to decide what to do next
        builder.setPositiveButton("Cancel anyway", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("orders").child(uid).child("status").setValue("canceled");
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
