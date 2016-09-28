package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Login;
import com.kalendria.kalendria.api.Constant;


/**
 * Created by rajaganapathi on 1/14/2016.
 */
public class LogoutFragment extends Fragment {
    SharedPreferences sharedPref; //Initialize the SharedPreference class
    SharedPreferences.Editor editor; ///Initialize the SharedPreference.Editor class it is used to Edit the key and values
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);
        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0); //we can use same Reference key or dynamic key
        editor = sharedPref.edit();
        buildAlertMessageNoGps();
        return rootView;
    }

    private void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Kalendria");
        builder.setMessage(
                "Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    @SuppressWarnings("unused") final DialogInterface dialog, final int id) {
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(getActivity(), Login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                dialog.cancel();

                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                            //getActivity().finish();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
