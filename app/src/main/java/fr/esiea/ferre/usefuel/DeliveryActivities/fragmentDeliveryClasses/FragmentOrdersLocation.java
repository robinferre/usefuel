package fr.esiea.ferre.usefuel.DeliveryActivities.fragmentDeliveryClasses;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;

import fr.esiea.ferre.usefuel.Objects.Car;
import fr.esiea.ferre.usefuel.Objects.OrderFuel;
import fr.esiea.ferre.usefuel.R;
import fr.esiea.ferre.usefuel.UserActivities.LoadingScreenBookActivity;
import fr.esiea.ferre.usefuel.UserActivities.MapActivity;

import static fr.esiea.ferre.usefuel.R.string.options;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOrdersLocation extends Fragment implements OnMapReadyCallback, View.OnClickListener {


    MapView mMapView;
    private GoogleMap googleMap;
    double distance = 30;
    double quantity= 5;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;


    public FragmentOrdersLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment_orders_location, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        final HashMap<String,Marker> hashMapMarker = new HashMap<>();
        final ArrayList<String> OrderUidList = new ArrayList<String>();

        Button b_options = (Button) view.findViewById(R.id.button_options);
        b_options.setOnClickListener(this);



        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                mDatabase = FirebaseDatabase.getInstance().getReference();
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser FireUser = firebaseAuth.getCurrentUser();
                mDatabase.child("orders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Check every order
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            //if an order have the value booking and this value is not in the arraylsit already

                            if (postSnapshot.child("status").getValue().equals("booking") && !OrderUidList.contains(postSnapshot.getKey())){

                                // we put it in the arraylist
                                OrderUidList.add(postSnapshot.getKey());

                                //latlng of the order
                                double lat = (double)postSnapshot.child("lat").getValue();
                                double lng = (double)postSnapshot.child("lng").getValue();
                                LatLng coord= new LatLng(lat,lng);

                                // We put a marker on a map with tue uid as key
                                hashMapMarker.put(postSnapshot.getKey(),googleMap.addMarker(new MarkerOptions().position(coord).title("Order")));

                                // else if it does not have the value booking but is in the array list
                            }else if (!postSnapshot.child("status").getValue().equals("booking") && OrderUidList.contains(postSnapshot.getKey())){
                                // remove the uid from the uid arraylist
                                OrderUidList.remove(postSnapshot.getKey());

                                //remove the marker from the hashmap and from the googlemap
                                Marker marker = hashMapMarker.get(postSnapshot.getKey());
                                marker.remove();
                                hashMapMarker.remove(postSnapshot.getKey());
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

                        final AlertDialog myDialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Ordering Fuel");
                        builder.setIcon(R.drawable.ic_menu_fuel);
                        builder.setCancelable(false);
                        builder.setMessage("Address\nFuel\nQuantity");

                        //Button to decide what to do next
                        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //nothing here, overrided later
                            }
                        });
                        myDialog = builder.create();
                        myDialog.show();

                        return true;
                    }

                });

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                /*LatLng kmutt= new LatLng(13.65, 100.49);
                LatLng cosmo = new LatLng(13.77, 100.58);
                LatLng order3 = new LatLng(13.70, 100.52);
                Marker addMarker = googleMap.addMarker(new MarkerOptions().position(kmutt).title("Order 1").snippet("28L - 45€"));
                Marker addMarker1 = googleMap.addMarker(new MarkerOptions().position(cosmo).title("Order 2").snippet("55L - 85€"));
                Marker addMarker2 = googleMap.addMarker(new MarkerOptions().position(order3).title("Order 3").snippet("40L - 60€"));

                addMarker.remove();*/
                // For zooming automatically to the location of the marker
                /*CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
            }

        });

        return view;
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
}
