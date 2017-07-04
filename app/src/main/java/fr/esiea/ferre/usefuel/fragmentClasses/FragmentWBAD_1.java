package fr.esiea.ferre.usefuel.fragmentClasses;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.esiea.ferre.usefuel.LoginActivity;
import fr.esiea.ferre.usefuel.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWBAD_1 extends Fragment {




    public FragmentWBAD_1() {
        // Required empty public constructor
    }

    public void Action1(){

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_wbad_1, container, false);

    }

}
