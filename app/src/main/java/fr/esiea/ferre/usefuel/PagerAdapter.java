package fr.esiea.ferre.usefuel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import fr.esiea.ferre.usefuel.fragmentClasses.FragmentWBAD_1;
import fr.esiea.ferre.usefuel.fragmentClasses.FragmentWBAD_2;
import fr.esiea.ferre.usefuel.fragmentClasses.FragmentWBAD_3;

/**
 * Created by Raphaël on 02/07/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentWBAD_1();

            case 1:
                return new FragmentWBAD_2();

            case 2:
                return new FragmentWBAD_3();

            default:
                break;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
