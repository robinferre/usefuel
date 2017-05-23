package fr.esiea.ferre.usefuel.fragmentClasses;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.TextView;


import fr.esiea.ferre.usefuel.LoginActivity;
import fr.esiea.ferre.usefuel.MainActivity;
import fr.esiea.ferre.usefuel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentUser extends Fragment implements OnClickListener{

    public FragmentUser(){}
    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private TextView usernameTV;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
      // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_fragment_user, container, false);


        buttonLogout =(Button) view.findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(this);

        //show username

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        {
            String username = user.getEmail().toString();
            usernameTV = (TextView) view.findViewById(R.id.textViewUsername);
            usernameTV.setText(username);
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

