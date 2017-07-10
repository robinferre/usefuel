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
import java.util.ArrayList;
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

        printData(view);
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

                            final OrderFuel order = new OrderFuel();
                            final String[] list_car_string;
                            final Car car = new Car();

                            final ArrayList<Car> listCar = new ArrayList<>();
                            listCar.add(dataSnapshot.child("car1").getValue(Car.class));
                            listCar.add(dataSnapshot.child("car2").getValue(Car.class));
                            listCar.add(dataSnapshot.child("car3").getValue(Car.class));
                            listCar.add(dataSnapshot.child("car4").getValue(Car.class));

                            if (listCar.get(0).getBrand().equals("none"))
                            {
                                list_car_string = new String[0];
                            }
                            else if (listCar.get(1).getBrand().equals("none"))
                            {
                                list_car_string = new String[1];
                                list_car_string[0] = listCar.get(0).getBrand() + ", " + listCar.get(0).getColor() + ", " + listCar.get(0).getFuelType() + ", " + listCar.get(0).getNumberPlate();
                            }
                            else if (listCar.get(2).getBrand().equals("none"))
                            {
                                list_car_string = new String[2];
                                list_car_string[0] = listCar.get(0).getBrand() + ", " + listCar.get(0).getColor() + ", " + listCar.get(0).getFuelType() + ", " + listCar.get(0).getNumberPlate();
                                list_car_string[1] = listCar.get(1).getBrand() + ", " + listCar.get(1).getColor() + ", " + listCar.get(1).getFuelType() + ", " + listCar.get(1).getNumberPlate();
                            }
                            else if (listCar.get(3).getBrand().equals("none"))
                            {
                                list_car_string = new String[3];
                                list_car_string[0] = listCar.get(0).getBrand() + ", " + listCar.get(0).getColor() + ", " + listCar.get(0).getFuelType() + ", " + listCar.get(0).getNumberPlate();
                                list_car_string[1] = listCar.get(1).getBrand() + ", " + listCar.get(1).getColor() + ", " + listCar.get(1).getFuelType() + ", " + listCar.get(1).getNumberPlate();
                                list_car_string[2] = listCar.get(2).getBrand() + ", " + listCar.get(2).getColor() + ", " + listCar.get(2).getFuelType() + ", " + listCar.get(2).getNumberPlate();
                            }
                            else
                            {
                                list_car_string = new String[4];
                                list_car_string[0] = listCar.get(0).getBrand() + ", " + listCar.get(0).getColor() + ", " + listCar.get(0).getFuelType() + ", " + listCar.get(0).getNumberPlate();
                                list_car_string[1] = listCar.get(1).getBrand() + ", " + listCar.get(1).getColor() + ", " + listCar.get(1).getFuelType() + ", " + listCar.get(1).getNumberPlate();
                                list_car_string[2] = listCar.get(2).getBrand() + ", " + listCar.get(2).getColor() + ", " + listCar.get(2).getFuelType() + ", " + listCar.get(2).getNumberPlate();
                                list_car_string[3] = listCar.get(3).getBrand() + ", " + listCar.get(3).getColor() + ", " + listCar.get(3).getFuelType() + ", " + listCar.get(3).getNumberPlate();
                            }

                            final AlertDialog myDialog1;

                            builderCar.setTitle("Choose your car");
                            builderCar.setIcon(R.drawable.ic_menu_car);
                            builderCar.setCancelable(false);
                            builderCar.setSingleChoiceItems(list_car_string, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    car.setBrand(listCar.get(i).getBrand());
                                    car.setNumberPlate(listCar.get(i).getNumberPlate());
                                    car.setFuelType(listCar.get(i).getFuelType());
                                    car.setColor(listCar.get(i).getColor());
                                }
                            });
                            //Button to decide what to do next
                            builderCar.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //nothing here, overrided later
                                }
                            });
                            //Button to cancel
                            builderCar.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //nothing here, overrided later
                                }
                            });

                            myDialog1 = builderCar.create();
                            myDialog1.show();
                            myDialog1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    if(car.getBrand() != null){
                                        // Save to database user informations
                                        firebaseUser = firebaseAuth.getCurrentUser();
                                        String uID = firebaseUser.getUid();

                                        // Create or Update Database Node
                                        mDatabase.child("orders").child(uID).child("car").setValue(car);

                                        myDialog1.dismiss();
                                    }
                                }
                            });
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

                    Car car_db = dataSnapshot.child("car").getValue(Car.class);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                }
            });
        }
    }
}
