package fr.esiea.ferre.usefuel.fragmentClasses;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Map;

import fr.esiea.ferre.usefuel.LoginActivity;
import fr.esiea.ferre.usefuel.MapActivity;
import fr.esiea.ferre.usefuel.R;

public class FragmentFuel extends Fragment implements View.OnClickListener {

    private Button buttonBook;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fuel, container, false);


        buttonBook =(Button) view.findViewById(R.id.button_book);
        buttonBook.setOnClickListener(this);


        return view;
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){

            case R.id.button_book :
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);

                break;

            default:
                break;

        }
    }
}
