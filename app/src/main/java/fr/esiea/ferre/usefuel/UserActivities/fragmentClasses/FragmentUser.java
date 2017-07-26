package fr.esiea.ferre.usefuel.UserActivities.fragmentClasses;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.TextView;


import fr.esiea.ferre.usefuel.UserActivities.LoginActivity;
import fr.esiea.ferre.usefuel.R;
import fr.esiea.ferre.usefuel.Objects.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentUser extends Fragment implements OnClickListener{

    public FragmentUser(){}
    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private TextView usernameTV;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user, container, false);


        buttonLogout =(Button) view.findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(this);

        //show username

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser FireUser = firebaseAuth.getCurrentUser();
        if(FireUser != null)
        {
            String uid = FireUser.getUid().toString();
            usernameTV = (TextView) view.findViewById(R.id.textViewUsername);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            firebaseAuth = FirebaseAuth.getInstance();
            mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                    usernameTV.setText(user.getUsername());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                    usernameTV.setText("username");
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
        // TODO Auto-generated method stub
        switch(v.getId()){

            case R.id.button_logout :
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