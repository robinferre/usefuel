package fr.esiea.ferre.usefuel.UserActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by rob on 6/30/2017.
 */

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Allows Firebase database offline mode
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}