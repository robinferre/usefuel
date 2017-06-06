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
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.lang.reflect.Array;
import java.util.Arrays;

import fr.esiea.ferre.usefuel.Car;
import fr.esiea.ferre.usefuel.R;
import fr.esiea.ferre.usefuel.User;

public class FragmentCar extends Fragment {

    Button mAddCar1, mAddCar2, mAddCar3, mAddCar4;
    TextView mCarView1, mCarView2, mCarView3, mCarView4;

    Car car;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_fragment_car, container, false);

        mAddCar1 = (Button) view.findViewById(R.id.button_car1);
        mCarView1 = (TextView) view.findViewById(R.id.tv_car1);

        // When press the add Car Button, set up multiple AlertDialog to configure your car
        mAddCar1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                car = chooseCar(mCarView1, "car1");
            }
        });



        return view;
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

        AlertDialog myDialog1, myDialog2, myDialog3, myDialog4;

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

        //third Builder to choose the fuel
        AlertDialog.Builder builder4 = new AlertDialog.Builder(getActivity());
        builder4.setTitle("Enter the last 3 digit of your plate");
        builder4.setIcon(R.drawable.ic_menu_car);
        builder4.setCancelable(false);
        builder4.setView(digit);


        builder4.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                car.setNumberPlate(digit.getText().toString());

                // Save to database user informations
                firebaseUser = firebaseAuth.getCurrentUser();
                String uID = firebaseUser.getUid();

                // Create or Update Database Node
                mDatabase.child("cars").child(uID).child(car_number).setValue(car);
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

        return car;
    }


}
