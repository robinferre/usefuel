package fr.esiea.ferre.usefuel.fragmentClasses;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import fr.esiea.ferre.usefuel.Objects.Car;
import fr.esiea.ferre.usefuel.UserActivities.MapActivity;
import fr.esiea.ferre.usefuel.Objects.OrderFuel;
import fr.esiea.ferre.usefuel.R;


public class FragmentFuel extends Fragment implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    private OrderFuel order;

    Button buttonBook, buttonEditCar, buttonEditQuantity;
    TextView mCarView, mQuantityView, mPriceView;
    Button mBook;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_fuel, container, false);
        mCarView = (TextView) view.findViewById(R.id.text_car);
        mQuantityView = (TextView) view.findViewById(R.id.text_quantity);
        mPriceView = (TextView) view.findViewById(R.id.text_price);

        buttonBook =(Button) view.findViewById(R.id.button_book);
        buttonBook.setOnClickListener(this);
        buttonEditCar =(Button) view.findViewById(R.id.button_edit_car);
        buttonEditCar.setOnClickListener(this);
        buttonEditQuantity =(Button) view.findViewById(R.id.button_edit_quantity);
        buttonEditQuantity.setOnClickListener(this);

        updatePrintData(view);
        return view;
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        final AlertDialog.Builder builderCar = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder builderQuantity = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getActivity());

        FirebaseUser FireUser;

        switch(v.getId()){
//book with informations stocked
            case R.id.button_book :
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
                break;
//open dialog box to choose price
            case R.id.button_edit_quantity :

                mDatabase = FirebaseDatabase.getInstance().getReference();
                firebaseAuth = FirebaseAuth.getInstance();
                FireUser = firebaseAuth.getCurrentUser();

                if(FireUser != null)
                {

                    // Specify the type of input expected
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);


                    final AlertDialog myDialog;
                    builderQuantity.setTitle("Choose your quantity");
                    builderQuantity.setIcon(R.drawable.ic_menu_fuel);
                    builderQuantity.setMessage("In liters between 5L and 70L");
                    builderQuantity.setView(input);

                    //Button to decide what to do next
                    builderQuantity.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //nothing here, overrided later
                        }
                    });
                    //Button to cancel
                    builderQuantity.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //do nothing, cancel
                        }
                    });

                    myDialog = builderQuantity.create();
                    myDialog.show();

                    myDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {

                            String mQuantity = input.getText().toString();
                            double quantity = Double.parseDouble(mQuantity);

                            if(quantity >= 5 && quantity <= 70 ){

                                myDialog.dismiss();
                                firebaseUser = firebaseAuth.getCurrentUser();
                                String uID = firebaseUser.getUid();

                                // Create or Update Database Node
                                mDatabase.child("orders").child(uID).child("fuelQuantity").setValue(mQuantity);
                            }
                        }
                    });
                }
                break;
 // open a dialog box that shows differents car registered and made the user choose one
            case R.id.button_edit_car :

                mDatabase = FirebaseDatabase.getInstance().getReference();
                firebaseAuth = FirebaseAuth.getInstance();
                FireUser = firebaseAuth.getCurrentUser();

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
                            // if no car, spray a message
                            if(listCar.get(0).getBrand().equals("none"))
                                builderCar.setMessage("Edit your Cars in the tab \"Manage Cars\"");
                            builderCar.setCancelable(true);
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
                                    //do nothing, cancel
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

    void updatePrintData(View view){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        if(FireUser != null)
        {
            String uid = FireUser.getUid().toString();
            mDatabase.child("orders").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Car car_db = dataSnapshot.child("car").getValue(Car.class);
                    String fuel_quantity = dataSnapshot.child("fuelQuantity").getValue(String.class);
                    String status = dataSnapshot.child("status").getValue(String.class);

                    String priceString;
                    double price, priceMin, priceMax;
                    firebaseUser = firebaseAuth.getCurrentUser();
                    String uID = firebaseUser.getUid();

                    if(!car_db.getBrand().equals("none")){
                        mCarView.setText(car_db.getBrand() + " " + car_db.getColor() + "\n" + car_db.getFuelType() + " " + car_db.getNumberPlate());
                    }

                    if(!fuel_quantity.equals("none"))
                    {
                        mQuantityView.setText(fuel_quantity + " L");
                        price = Double.parseDouble(fuel_quantity);
                        priceMin = 1.567*price;
                        priceMax = 1.987*price;


                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);
                        priceString = String.valueOf(df.format(priceMin)) + " €" + " -  " + String.valueOf(df.format(priceMax)) + " €";
                        mPriceView.setText(priceString);
                        mDatabase.child("orders").child(uID).child("price").setValue(priceString);

                    }

                    if(!car_db.getBrand().equals("none") && !fuel_quantity.equals("none"))
                    {
                        // Create or Update Database Node
                        mDatabase.child("orders").child(uID).child("status").setValue("quantity");
                    }


                    switch (status){
                        case "none" :
                            buttonBook.setVisibility(View.GONE);
                            break;
                        default:
                            buttonBook.setVisibility(View.VISIBLE);
                            break;
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                }
            });
        }
    }
}
