package fr.esiea.ferre.usefuel.DeliveryActivities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.esiea.ferre.usefuel.R;
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
        mDatabase.child("orders").child(uid).child("status").setValue("canceled");
        finish();
    }

    public void onMap(View view){
        String strUri = "http://maps.google.com/maps?q=loc:" + 0 + "," + 0 + " (" + "Label which you want" + ")";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

        startActivity(intent);
    }

    public void onConfirm(View view){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        final String uid = FireUser.getUid().toString();
        mDatabase.child("orders").child(uid).child("status").setValue("finish");
        finish();
    }


}
