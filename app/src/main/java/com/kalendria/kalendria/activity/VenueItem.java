package com.kalendria.kalendria.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.fragment.VenueItemFragment;

/**
 * Created by mansoor on 11/03/16.
 */
public class VenueItem extends KalendriaActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment pro = null;
        pro = new VenueItemFragment();
        if (pro != null) {


            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, pro);
            fragmentTransaction.commit();

        }
    }


}
