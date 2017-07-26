package fr.esiea.ferre.usefuel.DeliveryActivities.fragmentDeliveryClasses;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;
import android.widget.Toast;

import fr.esiea.ferre.usefuel.DeliveryActivities.BookedActivity;
import fr.esiea.ferre.usefuel.DeliveryActivities.MainDeliveryActivity;
import fr.esiea.ferre.usefuel.Objects.OrderFuel;
import fr.esiea.ferre.usefuel.R;
import fr.esiea.ferre.usefuel.UserActivities.LoadingScreenBookActivity;
import fr.esiea.ferre.usefuel.UserActivities.MapActivity;
import fr.esiea.ferre.usefuel.UserActivities.RegisterActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOrdersLocation extends Fragment
        implements
        OnMapReadyCallback,
        View.OnClickListener,
        LocationListener{


    MapView mMapView;
    private GoogleMap googleMap;
    LatLng camLatLng = new LatLng(0.0,0.0);
    double distance = 30;
    double quantity= 5;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    HashMap<String,OrderFuel> hashMapOrder = new HashMap<>();

    public FragmentOrdersLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_orders_location, container, false);

        Button buttonBook =(Button) view.findViewById(R.id.button_options);
        buttonBook.setOnClickListener(this);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        final HashMap<String,Marker> hashMapMarker = new HashMap<>();
        final ArrayList<String> OrderUidList = new ArrayList<String>();


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        CheckPosition();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                googleMap = mMap;
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    // Show rationale and request permission.
                }

                //Geolocalise l'utilisateur (Point bleu)
                if(mMap!=null){
                    mMap.setMyLocationEnabled(true);

                    CameraPosition myPosition = new CameraPosition.Builder().target(camLatLng).zoom(12).bearing(0).tilt(30).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                }


                mDatabase = FirebaseDatabase.getInstance().getReference();
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser FireUser = firebaseAuth.getCurrentUser();
                mDatabase.child("orders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Check every order
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            //if an order have the value booking and this value is not in the arraylsit already

                            if (postSnapshot.child("status").getValue() == null)
                                continue;

                            if (postSnapshot.child("status").getValue().equals("booking") && !OrderUidList.contains(postSnapshot.getKey())){

                                // we put it in the arraylist
                                OrderUidList.add(postSnapshot.getKey());

                                //latlng of the order
                                double lat = (double)postSnapshot.child("lat").getValue();
                                double lng = (double)postSnapshot.child("lng").getValue();
                                LatLng coord= new LatLng(lat,lng);

                                Marker marker = googleMap.addMarker(new MarkerOptions().position(coord).title("Order"));
                                marker.setTag((String) postSnapshot.getKey());
                                hashMapMarker.put(postSnapshot.getKey(), marker);
                                hashMapOrder.put(postSnapshot.getKey(),postSnapshot.getValue(OrderFuel.class));


                                // else if it does not have the value booking but is in the array list
                            }else if (!postSnapshot.child("status").getValue().equals("booking") && OrderUidList.contains(postSnapshot.getKey())){

                                // remove the uid from the uid arraylist
                                OrderUidList.remove(postSnapshot.getKey());

                                //remove the marker from the hashmap and from the googlemap
                                Marker marker = hashMapMarker.get(postSnapshot.getKey());
                                marker.remove();
                                hashMapMarker.remove(postSnapshot.getKey());
                                hashMapOrder.remove(postSnapshot.getKey());

                            }

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Failed to read value
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {

                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser FireUser = firebaseAuth.getCurrentUser();

                        final String u_uid = (String) marker.getTag();
                        final String d_uid = FireUser.getUid().toString();

                        final AlertDialog myDialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(" Someone is looking for fuel");
                        builder.setIcon(R.drawable.ic_menu_fuel);
                        builder.setCancelable(true);

                        OrderFuel o = hashMapOrder.get(u_uid);

                        builder.setMessage("Address entered:\t\t\t\t" +o.getAddress() +"\n"+ "Fuel type required:\t\t\t\t" +o.getCar().getFuelType() +"\n" +"Fuel quantity desired:\t\t\t\t" + o.getFuelQuantity() +"L");

                        //Button to decide what to do next
                        builder.setPositiveButton("Confirm Booking", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatabase.child("orders").child(u_uid).child("status").setValue("booked");
                                mDatabase.child("orders").child(u_uid).child("deliverer").setValue(d_uid);

                                Intent intent = new Intent(getActivity(), BookedActivity.class);
                                intent.putExtra("value1",u_uid);
                                startActivity(intent);
                            }
                        });
                        //Button to cancel
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //do nothing, cancel
                            }
                        });
                        myDialog = builder.create();
                        myDialog.show();

                        return true;
                    }

                });
            }
        });

        return view;
    }

    private void CheckPosition(){
        LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        double my_latitude = 0.0;
        double my_longitude = 0.0;

        //try for permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            Log.d("manifest location","ERROR");
            Toast.makeText(getActivity(), "manifest right ERROR", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isGPSOn = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSOn && !isNetworkEnabled){
            Log.d("GPS","ERROR");
            Toast.makeText(getActivity(), "GPS ERROR", Toast.LENGTH_SHORT).show();
            return;
        }

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,1, this);

        Location location;
        location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (location == null){
            Log.d("location", "null");
            Toast.makeText(getActivity(),"Location = null", Toast.LENGTH_SHORT).show();
            return;
        }

        my_latitude = location.getLatitude();
        my_longitude = location.getLongitude();

        // print in logcat
        String result = String.valueOf(my_latitude);
        result += " ";
        result += String.valueOf(my_longitude);
        Log.d("GPS",result + "");
        camLatLng = new LatLng(my_latitude,my_longitude);

    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_options:
                onOptions();
                break;
            default:
                break;
        }
    }

    void onOptions(){
        final String[] listDistance;
        final String[] listQuantity;

        final AlertDialog myDialog1, myDialog2;
        //First Builder to choose the brand
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle("Choose the maximum distance");
        builder1.setIcon(R.drawable.ic_menu_car);
        builder1.setCancelable(false);

        listDistance = getResources().getStringArray(R.array.deliver_distance);

        builder1.setSingleChoiceItems(listDistance, 3, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        distance = 5;
                        break;
                    case 1:
                        distance = 10;
                        break;
                    case 2:
                        distance = 20;
                        break;
                    case 3:
                        distance = 30;
                        break;
                    default:
                        distance = 30;
                        break;
                }

            }
        });
        //Button to decide what to do next
        builder1.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //nothing here, overrided later
            }
        });

        //Second Builder to choose the Color
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        builder2.setTitle("Choose minimum quantity");
        builder2.setIcon(R.drawable.ic_menu_fuel);
        builder2.setCancelable(false);

        listQuantity = getResources().getStringArray(R.array.deliver_quantity);

        builder2.setSingleChoiceItems(listQuantity, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        quantity = 5;
                        break;
                    case 1:
                        quantity = 20;
                        break;
                    case 2:
                        quantity = 50;
                        break;
                    default:
                        quantity = 5;
                        break;
                }

            }
        });

        builder2.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        // Create the alert dialog
        myDialog1 = builder1.create();
        myDialog2 = builder2.create();
        // Finally, display the alert dialog
        myDialog2.show();
        myDialog1.show();
    }



        @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onLocationChanged(Location location) {
        double my_latitude;
        double my_longitude;

        my_latitude = location.getLatitude();
        my_longitude = location.getLongitude();

        // print in logcat
        String result = String.valueOf(my_latitude);
        result += " ";
        result += String.valueOf(my_longitude);
        Log.d("GPS",result + "");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        final String d_uid = FireUser.getUid().toString();

        mDatabase.child("deliverer_info").child(d_uid).child("lat").setValue(my_latitude);
        mDatabase.child("deliverer_info").child(d_uid).child("lng").setValue(my_longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
