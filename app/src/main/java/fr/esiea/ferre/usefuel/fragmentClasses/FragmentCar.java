package fr.esiea.ferre.usefuel.fragmentClasses;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import fr.esiea.ferre.usefuel.Car;
import fr.esiea.ferre.usefuel.R;

public class FragmentCar extends Fragment {

    Button mAddCar1, mAddCar2, mAddCar3, mAddCar4;
    Button mDelCar1, mDelCar2, mDelCar3, mDelCar4;
    TextView mCarView1, mCarView2, mCarView3, mCarView4;
    TextView mCarView10, mCarView20, mCarView30, mCarView40;

    ImageView blueLine1, blueLine2, blueLine3;
    LinearLayout car10layout, car2layout,car20layout, car3layout,car30layout, car4layout,car40layout;

    Car car1;
    Car car2;
    Car car3;
    Car car4;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_car, container, false);

        car10layout = (LinearLayout) view.findViewById(R.id.car10layout);
        mAddCar1 = (Button) view.findViewById(R.id.button_car1);
        mDelCar1 = (Button) view.findViewById(R.id.button_d_car1);
        mCarView1 = (TextView) view.findViewById(R.id.tv_car1);
        mCarView10 = (TextView) view.findViewById(R.id.tv_car10);
        blueLine1 = (ImageView) view.findViewById(R.id.blueLine1);

        car2layout = (LinearLayout) view.findViewById(R.id.car2layout);
        car20layout = (LinearLayout) view.findViewById(R.id.car20layout);
        mAddCar2 = (Button) view.findViewById(R.id.button_car2);
        mDelCar2 = (Button) view.findViewById(R.id.button_d_car2);
        mCarView2 = (TextView) view.findViewById(R.id.tv_car2);
        mCarView20 = (TextView) view.findViewById(R.id.tv_car20);
        blueLine2 = (ImageView) view.findViewById(R.id.blueLine2);

        car3layout = (LinearLayout) view.findViewById(R.id.car3layout);
        car30layout = (LinearLayout) view.findViewById(R.id.car30layout);
        mAddCar3 = (Button) view.findViewById(R.id.button_car3);
        mDelCar3 = (Button) view.findViewById(R.id.button_d_car3);
        mCarView3 = (TextView) view.findViewById(R.id.tv_car3);
        mCarView30 = (TextView) view.findViewById(R.id.tv_car30);
        blueLine3 = (ImageView) view.findViewById(R.id.blueLine3);

        car4layout = (LinearLayout) view.findViewById(R.id.car4layout);
        car40layout = (LinearLayout) view.findViewById(R.id.car40layout);
        mAddCar4 = (Button) view.findViewById(R.id.button_car4);
        mDelCar4 = (Button) view.findViewById(R.id.button_d_car4);
        mCarView4 = (TextView) view.findViewById(R.id.tv_car4);
        mCarView40 = (TextView) view.findViewById(R.id.tv_car40);

        // When press the add Car Button, set up multiple AlertDialog to configure your car
        mAddCar1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                car1 = chooseCar(mCarView1, "car1");

            }
        });
        mDelCar1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Save to database user informations
                firebaseUser = firebaseAuth.getCurrentUser();
                String uID = firebaseUser.getUid();
                // set to none all data
                car1 = new Car("none", "none", "none", "none");
                mDatabase.child("cars").child(uID).child("car1").setValue(car1);
            }
        });

        mAddCar2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                car1 = chooseCar(mCarView2, "car2");

            }
        });
        mDelCar2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Save to database user informations
                firebaseUser = firebaseAuth.getCurrentUser();
                String uID = firebaseUser.getUid();
                // set to none all data
                car2 = new Car("none", "none", "none", "none");
                mDatabase.child("cars").child(uID).child("car2").setValue(car2);
            }
        });

        mAddCar3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                car3 = chooseCar(mCarView3, "car3");

            }
        });
        mDelCar3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Save to database user informations
                firebaseUser = firebaseAuth.getCurrentUser();
                String uID = firebaseUser.getUid();
                // set to none all data
                car3 = new Car("none", "none", "none", "none");
                mDatabase.child("cars").child(uID).child("car3").setValue(car3);
            }
        });

        mAddCar4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                car4 = chooseCar(mCarView4, "car4");

            }
        });
        mDelCar4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Save to database user informations
                firebaseUser = firebaseAuth.getCurrentUser();
                String uID = firebaseUser.getUid();
                // set to none all data
                car4 = new Car("none", "none", "none", "none");
                mDatabase.child("cars").child(uID).child("car4").setValue(car4);
            }
        });


        // print the data and manage the screen using the database
        printData(view);

        return view;
    }


    void printData(View view){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        if(FireUser != null)
        {
            String uid = FireUser.getUid().toString();


            mDatabase.child("cars").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    Car car_db1 = dataSnapshot.child("car1").getValue(Car.class);
                    Car car_db2 = dataSnapshot.child("car2").getValue(Car.class);
                    Car car_db3 = dataSnapshot.child("car3").getValue(Car.class);
                    Car car_db4 = dataSnapshot.child("car4").getValue(Car.class);

                    mCarView1.setText(car_db1.getBrand());
                    mCarView10.setText(car_db1.getColor()+ ", " + car_db1.getFuelType() + ", " + car_db1.getNumberPlate());

                    mCarView2.setText(car_db2.getBrand());
                    mCarView20.setText(car_db2.getColor()+ ", " + car_db2.getFuelType() + ", " + car_db2.getNumberPlate());

                    mCarView3.setText(car_db3.getBrand());
                    mCarView30.setText(car_db3.getColor()+ ", " + car_db3.getFuelType() + ", " + car_db3.getNumberPlate());

                    mCarView4.setText(car_db4.getBrand());
                    mCarView40.setText(car_db4.getColor()+ ", " + car_db4.getFuelType() + ", " + car_db4.getNumberPlate());

                    if(car_db1.getBrand().equals("none") && car_db2.getBrand().equals("none") && car_db3.getBrand().equals("none") && car_db4.getBrand().equals("none")  )
                    {
                        mAddCar1.setText("Add");
                        mAddCar2.setText("Add");
                        mAddCar3.setText("Add");
                        mAddCar4.setText("Add");

                        mDelCar1.setVisibility(View.VISIBLE);
                        mDelCar2.setVisibility(View.VISIBLE);
                        mDelCar3.setVisibility(View.VISIBLE);

                        car10layout.setVisibility(View.GONE);
                        blueLine1.setVisibility(View.GONE);
                        car2layout.setVisibility(View.GONE);

                        car20layout.setVisibility(View.GONE);
                        blueLine2.setVisibility(View.GONE);
                        car3layout.setVisibility(View.GONE);

                        car30layout.setVisibility(View.GONE);
                        blueLine3.setVisibility(View.GONE);
                        car4layout.setVisibility(View.GONE);

                        car40layout.setVisibility(View.GONE);
                    }
                    else if(car_db2.getBrand().equals("none") && car_db3.getBrand().equals("none") && car_db4.getBrand().equals("none") )
                    {
                        mAddCar1.setText("Edit");
                        mAddCar2.setText("Add");
                        mAddCar3.setText("Add");
                        mAddCar4.setText("Add");

                        mDelCar1.setVisibility(View.VISIBLE);
                        mDelCar2.setVisibility(View.VISIBLE);
                        mDelCar3.setVisibility(View.VISIBLE);

                        car10layout.setVisibility(View.VISIBLE);
                        blueLine1.setVisibility(View.VISIBLE);
                        car2layout.setVisibility(View.VISIBLE);

                        car20layout.setVisibility(View.GONE);
                        blueLine2.setVisibility(View.GONE);
                        car3layout.setVisibility(View.GONE);

                        car30layout.setVisibility(View.GONE);
                        blueLine3.setVisibility(View.GONE);
                        car4layout.setVisibility(View.GONE);

                        car40layout.setVisibility(View.GONE);
                    }
                    else if( car_db3.getBrand().equals("none") && car_db4.getBrand().equals("none") )
                    {
                        mAddCar1.setText("Edit");
                        mAddCar2.setText("Edit");
                        mAddCar3.setText("Add");
                        mAddCar4.setText("Add");

                        mDelCar1.setVisibility(View.GONE);
                        mDelCar2.setVisibility(View.VISIBLE);
                        mDelCar3.setVisibility(View.VISIBLE);

                        car10layout.setVisibility(View.VISIBLE);
                        blueLine1.setVisibility(View.VISIBLE);
                        car2layout.setVisibility(View.VISIBLE);

                        car20layout.setVisibility(View.VISIBLE);
                        blueLine2.setVisibility(View.VISIBLE);
                        car3layout.setVisibility(View.VISIBLE);

                        car30layout.setVisibility(View.GONE);
                        blueLine3.setVisibility(View.GONE);
                        car4layout.setVisibility(View.GONE);

                        car40layout.setVisibility(View.GONE);
                    }
                    else if( car_db4.getBrand().equals("none"))
                    {
                        mAddCar1.setText("Edit");
                        mAddCar2.setText("Edit");
                        mAddCar3.setText("Edit");
                        mAddCar4.setText("Add");

                        mDelCar1.setVisibility(View.GONE);
                        mDelCar2.setVisibility(View.GONE);
                        mDelCar3.setVisibility(View.VISIBLE);

                        car10layout.setVisibility(View.VISIBLE);
                        blueLine1.setVisibility(View.VISIBLE);
                        car2layout.setVisibility(View.VISIBLE);

                        car20layout.setVisibility(View.VISIBLE);
                        blueLine2.setVisibility(View.VISIBLE);
                        car3layout.setVisibility(View.VISIBLE);

                        car30layout.setVisibility(View.VISIBLE);
                        blueLine3.setVisibility(View.VISIBLE);
                        car4layout.setVisibility(View.VISIBLE);

                        car40layout.setVisibility(View.GONE);
                    }
                    else
                    {
                        mAddCar1.setText("Edit");
                        mAddCar2.setText("Edit");
                        mAddCar3.setText("Edit");
                        mAddCar4.setText("Edit");

                        mDelCar1.setVisibility(View.GONE);
                        mDelCar2.setVisibility(View.GONE);
                        mDelCar3.setVisibility(View.GONE);

                        car10layout.setVisibility(View.VISIBLE);
                        blueLine1.setVisibility(View.VISIBLE);
                        car2layout.setVisibility(View.VISIBLE);

                        car20layout.setVisibility(View.VISIBLE);
                        blueLine2.setVisibility(View.VISIBLE);
                        car3layout.setVisibility(View.VISIBLE);

                        car30layout.setVisibility(View.VISIBLE);
                        blueLine3.setVisibility(View.VISIBLE);
                        car4layout.setVisibility(View.VISIBLE);

                        car40layout.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                }
            });
        }
    }


    Car chooseCar(final TextView mCarView, final String car_number){

        final Car car = new Car();

        // Edit text to enter digit
        final EditText digit = new EditText(getActivity());
        int maxLength = 3;
        digit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        digit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);

        final String[] listBrands;
        final String[] listColors;
        final String[] listFuels;

        final AlertDialog myDialog1, myDialog2, myDialog3, myDialog4;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        //First Builder to choose the brand
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle("Choose the car brand");
        builder1.setIcon(R.drawable.ic_menu_car);
        builder1.setCancelable(false);

        listBrands = getResources().getStringArray(R.array.car_brand);
        Arrays.sort(listBrands);

        builder1.setSingleChoiceItems(listBrands, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                car.setBrand(Arrays.asList(listBrands).get(i));

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
        builder2.setTitle("Choose the car color");
        builder2.setIcon(R.drawable.ic_menu_car);
        builder2.setCancelable(false);

        listColors = getResources().getStringArray(R.array.car_color);
        Arrays.sort(listColors);

        builder2.setSingleChoiceItems(listColors, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                car.setColor(Arrays.asList(listColors).get(i));
            }
        });

        builder2.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        //third Builder to choose the fuel
        AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
        builder3.setTitle("Choose the car fuel");
        builder3.setIcon(R.drawable.ic_menu_car);
        builder3.setCancelable(false);

        listFuels = getResources().getStringArray(R.array.car_fuel);
        builder3.setSingleChoiceItems(listFuels, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                car.setFuelType(Arrays.asList(listFuels).get(i));
            }
        });

        builder3.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        //final Builder to choose the plate
        AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
        builder4.setTitle("Enter the last 3 digit of your plate");
        builder4.setIcon(R.drawable.ic_menu_car);
        builder4.setCancelable(false);
        builder4.setView(digit);


        builder4.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        // Create the alert dialog
        myDialog1 = builder1.create();
        myDialog2 = builder2.create();
        myDialog3 = builder3.create();
        myDialog4 = builder4.create();

        // Finally, display the alert dialog
        myDialog4.show();
        myDialog3.show();
        myDialog2.show();
        myDialog1.show();

        myDialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(car.getBrand() != null)
                    myDialog1.dismiss();
            }
        });

        myDialog2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(car.getColor() != null)
                    myDialog2.dismiss();
            }
        });

        myDialog3.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(car.getFuelType() != null)
                    myDialog3.dismiss();
            }
        });

        myDialog4.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                car.setNumberPlate(digit.getText().toString());

                if(!car.getNumberPlate().isEmpty())
                {
                    // Save to database user informations
                    firebaseUser = firebaseAuth.getCurrentUser();
                    String uID = firebaseUser.getUid();

                    // Create or Update Database Node
                    mDatabase.child("cars").child(uID).child(car_number).setValue(car);

                    myDialog4.dismiss();
                }

            }
        });

        return car;
    }


}
