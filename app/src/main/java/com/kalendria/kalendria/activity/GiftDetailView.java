package com.kalendria.kalendria.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.fragment.AddToCartFragment;
import com.kalendria.kalendria.fragment.BookWithLoyaltyFragment;
import com.kalendria.kalendria.fragment.DashBoardFragment;
import com.kalendria.kalendria.fragment.FavoriteFragment;
import com.kalendria.kalendria.fragment.GiftDetailViewFragment;
import com.kalendria.kalendria.fragment.GiftListViewFragment;
import com.kalendria.kalendria.fragment.MyOrdersFragment;
import com.kalendria.kalendria.fragment.ProfileFragments;
import com.kalendria.kalendria.fragment.VenueFragment;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.FragmentDrawer;
import com.kalendria.kalendria.utility.KalendriaAppController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mansoor on 11/03/16.
 */
public class GiftDetailView extends KalendriaActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment pro = null;
        pro = new GiftDetailViewFragment();

        if (pro != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, pro);
            fragmentTransaction.commit();

        }
    }

}
