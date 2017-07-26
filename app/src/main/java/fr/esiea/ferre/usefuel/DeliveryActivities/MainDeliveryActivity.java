package fr.esiea.ferre.usefuel.DeliveryActivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.database.FirebaseDatabase;

import fr.esiea.ferre.usefuel.DeliveryActivities.fragmentDeliveryClasses.FragmentDeliveryProfile;
import fr.esiea.ferre.usefuel.DeliveryActivities.fragmentDeliveryClasses.FragmentOrdersLocation;
import fr.esiea.ferre.usefuel.R;

public class MainDeliveryActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_delivery);
        // Charge all data to avoid delay
        FirebaseDatabase.getInstance().getReference().keepSynced(true);

        final Fragment profileDeliverFragment = new FragmentDeliveryProfile();
        final Fragment ordersPositionFragment = new FragmentOrdersLocation();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentContainer, profileDeliverFragment).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fragmentManager = getSupportFragmentManager();

                if (item.getItemId() == R.id.profileItem) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, profileDeliverFragment).commit();
                } else if (item.getItemId() ==  R.id.ordersItem){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, ordersPositionFragment).commit();

                }
                return true;
            }
        });
    }
}

