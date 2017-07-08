package fr.esiea.ferre.usefuel.fragmentClasses;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

import fr.esiea.ferre.usefuel.Car;
import fr.esiea.ferre.usefuel.LoginActivity;
import fr.esiea.ferre.usefuel.MapActivity;
import fr.esiea.ferre.usefuel.OrderFuel;
import fr.esiea.ferre.usefuel.R;

public class FragmentFuel extends Fragment implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Button buttonBook;
    private Button buttonEditCar;

    private OrderFuel order;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fuel, container, false);


        buttonBook =(Button) view.findViewById(R.id.button_book);
        buttonBook.setOnClickListener(this);
        buttonEditCar =(Button) view.findViewById(R.id.button_edit_car);
        buttonEditCar.setOnClickListener(this);


        return view;
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        final AlertDialog.Builder builderCar = new AlertDialog.Builder(getActivity());

        switch(v.getId()){

 //book with informations stocked
            case R.id.button_book :
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
                break;
 // open a dialog box that shows differents car registered and made the user choose one
            case R.id.button_edit_car :

                mDatabase = FirebaseDatabase.getInstance().getReference();
                firebaseAuth = FirebaseAuth.getInstance();

                final FirebaseUser FireUser = firebaseAuth.getCurrentUser();
                if(FireUser != null)
                {
                    String uid = FireUser.getUid().toString();
                    mDatabase.child("cars").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {



                                final String[] listCars;
                                final Car car = new Car();
                                int j;

                                Car car_db1 = dataSnapshot.child("car1").getValue(Car.class);
                                Car car_db2 = dataSnapshot.child("car2").getValue(Car.class);
                                Car car_db3 = dataSnapshot.child("car3").getValue(Car.class);
                                Car car_db4 = dataSnapshot.child("car4").getValue(Car.class);

                                if (car_db1.getBrand().equals("none"))
                                {
                                    listCars = new String[0];
                                }
                                else if (car_db2.getBrand().equals("none"))
                                {
                                    listCars = new String[1];
                                    listCars[0] = car_db1.getBrand() + ", " + car_db1.getColor() + ", " + car_db1.getFuelType() + ", " + car_db1.getNumberPlate();
                                }
                                else if (car_db3.getBrand().equals("none"))
                                {
                                    listCars = new String[2];
                                    listCars[0] = car_db1.getBrand() + ", " + car_db1.getColor() + ", " + car_db1.getFuelType() + ", " + car_db1.getNumberPlate();
                                    listCars[1] = car_db2.getBrand() + ", " + car_db2.getColor() + ", " + car_db2.getFuelType() + ", " + car_db2.getNumberPlate();
                                }
                                else if (car_db4.getBrand().equals("none"))
                                {
                                    listCars = new String[3];
                                    listCars[0] = car_db1.getBrand() + ", " + car_db1.getColor() + ", " + car_db1.getFuelType() + ", " + car_db1.getNumberPlate();
                                    listCars[1] = car_db2.getBrand() + ", " + car_db2.getColor() + ", " + car_db2.getFuelType() + ", " + car_db2.getNumberPlate();
                                    listCars[2] = car_db3.getBrand() + ", " + car_db3.getColor() + ", " + car_db3.getFuelType() + ", " + car_db3.getNumberPlate();
                                }
                                else
                                {
                                    listCars = new String[4];
                                    listCars[0] = car_db1.getBrand() + ", " + car_db1.getColor() + ", " + car_db1.getFuelType() + ", " + car_db1.getNumberPlate();
                                    listCars[1] = car_db2.getBrand() + ", " + car_db2.getColor() + ", " + car_db2.getFuelType() + ", " + car_db2.getNumberPlate();
                                    listCars[2] = car_db3.getBrand() + ", " + car_db3.getColor() + ", " + car_db3.getFuelType() + ", " + car_db3.getNumberPlate();
                                    listCars[3] = car_db4.getBrand() + ", " + car_db4.getColor() + ", " + car_db4.getFuelType() + ", " + car_db4.getNumberPlate();
                                }

                                final AlertDialog myDialog1;

                                builderCar.setTitle("Choose your car");
                                builderCar.setIcon(R.drawable.ic_menu_car);
                                builderCar.setCancelable(false);
                                builderCar.setSingleChoiceItems(listCars, -1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                //Button to decide what to do next
                                builderCar.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //nothing here, overrided later
                                    }
                                });

                                myDialog1 = builderCar.create();
                                myDialog1.show();

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Failed to read value
                        }

                    });
                }
                break;

            default:
                break;

        }
    }

    void printData(View view){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        if(FireUser != null)
        {
            String uid = FireUser.getUid().toString();


            mDatabase.child("order").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                }
            });
        }
    }
}
