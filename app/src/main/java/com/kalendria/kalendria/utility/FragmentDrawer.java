package com.kalendria.kalendria.utility;

/**
 * Created by murugan on 19/02/16
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.adapter.NavigationDrawerAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.NavDrawerItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FragmentDrawer extends Fragment {


    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    public ImageView navigation_close;
    RelativeLayout slider_rl;


    //coded by Magesh
    ImageView fullImage;
    ImageView thumbnail;
    TextView fullname;
    TextView email;
    TextView loyalty;
    TextView wallet;

    ImageView frag_icon;

    public FragmentDrawer() {
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);

        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating view layout


        final View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        initDrawer(layout);

        navigation_close = (ImageView)layout.findViewById(R.id.navigation_close);

        slider_rl = (RelativeLayout) layout.findViewById(R.id.slider_rl);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*vasasnth you add this for navigation drawer close*/
        navigation_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.closeDrawers();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
                /*Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show();
                layout.findViewById(R.id.frag_layout).setSelected(true);*/

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        slider_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return layout;
    }

    //--Coded by Magesh
    public void initDrawer(View view)
    {
         thumbnail = (ImageView)view.findViewById(R.id.thumbnail);
         fullname   = (TextView)view.findViewById(R.id.fullname);
         email      = (TextView)view.findViewById(R.id.emailTextView);
         loyalty    = (TextView)view.findViewById(R.id.loyaltyTextField);
         wallet     = (TextView)view.findViewById(R.id.walletTextField);
        fullImage = (ImageView)view.findViewById(R.id.fullImage);

    }

    public void setUserDetails()
    {
        fullname.setText(Constant.getFirstName() +" "+ Constant.getLastName());
        email.setText(Constant.getEmail());
        loyalty.setText(Constant.getLoyality());
        wallet.setText(Constant.getWallet());
        String profile_image = Constant.getProfileImage();

        if(!profile_image.equals("")){



            Picasso.with(getActivity())
                    .load(profile_image)
                            // .memoryPolicy(MemoryPolicy.NO_CACHE )
                            // .networkPolicy(NetworkPolicy.NO_CACHE)
                            //.resize(720, 350)
                             .error(R.drawable.avatar_men_new)
                    .placeholder(R.drawable.avatar_men_new)
                    .noFade()
                            // .fit().centerCrop()
                    .into(thumbnail);

            Picasso.with(getActivity())
                    .load(profile_image)
                            // .memoryPolicy(MemoryPolicy.NO_CACHE )
                            // .networkPolicy(NetworkPolicy.NO_CACHE)
                            //.resize(720, 350)
                    .error(R.drawable.avatar_men_wide)
                    .placeholder(R.drawable.avatar_men_wide)
                    .noFade()
                            // .fit().centerCrop()
                    .into(fullImage);
        }
        else {
            thumbnail.setImageResource(R.drawable.avatar_men_new);
            fullImage.setImageResource(R.drawable.avatar_men_wide);
        }
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, null, R.string.drawer_open, R.string.drawer_close) {

            public boolean onOptionsItemSelected(MenuItem item) {
                if (item != null && item.getItemId() == R.id.action_search) {
                    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        mDrawerLayout.openDrawer(Gravity.RIGHT);
                    }
                }
                return false;
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setUserDetails();//code added by Magesh
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
