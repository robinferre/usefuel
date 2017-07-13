package fr.esiea.ferre.usefuel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by rob on 6/27/2017.
 */

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private GoogleMap mMap;
    private Marker marker;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        context = MapActivity.this;

        firebaseAuth = FirebaseAuth.getInstance();

        // If already connected
        if (firebaseAuth.getCurrentUser() != null){
            // Get the SupportMapFragment and request notification
            // when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        updatePrintData();
    }
    public void onSearch(View view){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser FireUser = firebaseAuth.getCurrentUser();


        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList=null;

        if (location != null || !location.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO block if address is bullshit like "flekadhgljzhg" it crash

            //show a marker from the address entered
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            //adjust camera
            CameraPosition myPosition = new CameraPosition.Builder().target(latLng).zoom(17).bearing(0).tilt(30).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));

            //place marker
            if(marker == null){
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Address"));
            }
            else {
                marker.setPosition(latLng);
            }

            //update database with the address entered
            firebaseUser = firebaseAuth.getCurrentUser();
            String uID = firebaseUser.getUid();
            mDatabase.child("orders").child(uID).child("address").setValue(location);
            mDatabase.child("orders").child(uID).child("lat").setValue(address.getLatitude());
            mDatabase.child("orders").child(uID).child("lng").setValue(address.getLongitude());

            //Hide keyboard after button
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public void onReturn(View view)
    {
        firebaseUser = firebaseAuth.getCurrentUser();
        String uID = firebaseUser.getUid();
        mDatabase.child("orders").child(uID).child("address").setValue("none");
        mDatabase.child("orders").child(uID).child("lat").setValue(0);
        mDatabase.child("orders").child(uID).child("lng").setValue(0);

        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    public void onBook(View view)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        final String uid = FireUser.getUid().toString();
        mDatabase.child("orders").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String address =  dataSnapshot.child("address").getValue(String.class);

                //get searched address and place a marker
                if(address.equals("none")){

                    final AlertDialog.Builder builderBook = new AlertDialog.Builder(MapActivity.this);
                    final AlertDialog myDialog;
                    builderBook.setTitle("Booking");
                    builderBook.setIcon(R.drawable.ic_menu_map);
                    builderBook.setMessage("Choose an address before booking");

                    //Button to decide what to do next
                    builderBook.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //nothing here, just pass
                        }
                    });
                    myDialog = builderBook.create();
                    myDialog.show();
                }
                else
                    startActivity(new Intent(getApplicationContext(),LoadingScreenBookActivity.class));

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
            }
        });

    }

    void updatePrintData(){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        if(FireUser != null)
        {
            final String uid = FireUser.getUid().toString();
            mDatabase.child("orders").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String address =  dataSnapshot.child("address").getValue(String.class);

                    //get searched address and place a marker
                    if(address != "none"){
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                }
            });
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.INTERNET
                        }, 10);
            }
            return;
        }

        //Geolocalise l'utilisateur (Point bleu)
        if(mMap!=null){
            mMap.setMyLocationEnabled(true);
            Location myLocation = mMap.getMyLocation();

            if(myLocation !=null){
                LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                CameraPosition myPosition = new CameraPosition.Builder().target(myLatLng).zoom(17).bearing(0).tilt(30).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
            }
        }


    }
}