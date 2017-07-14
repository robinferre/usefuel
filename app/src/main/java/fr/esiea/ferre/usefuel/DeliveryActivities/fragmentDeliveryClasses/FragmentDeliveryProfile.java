package fr.esiea.ferre.usefuel.DeliveryActivities.fragmentDeliveryClasses;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.esiea.ferre.usefuel.LoginActivity;
import fr.esiea.ferre.usefuel.R;
import fr.esiea.ferre.usefuel.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDeliveryProfile extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button buttonLogout2;
    private TextView delivernameTV;
    private DatabaseReference mDatabase;


    public FragmentDeliveryProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_deliver, container, false);

        buttonLogout2 =(Button) view.findViewById(R.id.button_logout2);
        buttonLogout2.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser FireUser = firebaseAuth.getCurrentUser();

        if(FireUser != null)
        {
            String uid = FireUser.getUid().toString();
            delivernameTV = (TextView) view.findViewById(R.id.textViewUsername);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            firebaseAuth = FirebaseAuth.getInstance();
            mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                    delivernameTV.setText(user.getUsername());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                    delivernameTV.setText("username");
                }
            });
        }
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()== null){
            getActivity().finish();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }


        return view;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.button_logout2 :
                firebaseAuth.signOut();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

                break;

            default:
                break;

        }

    }
}
