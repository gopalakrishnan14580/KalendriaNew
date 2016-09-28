package com.kalendria.kalendria.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Login;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.utility.KalendriaAppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by murugan on 25/04/16.
 */
public class ResetPasswordFragament extends Fragment {

    // LogCat tag

    private ProgressDialog pDialog;
    JSONObject gcm_device_id = null;
    EditText old_password,new_password,confrim_password;
    Button reset_password_sumbit,register_back_btn;
    String email;
    public ResetPasswordFragament() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
        KalendriaAppController.hideSoftKeyboard(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View rootView = inflater.inflate(R.layout.resetpassword, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        old_password=(EditText)rootView.findViewById(R.id.old_password) ;
        new_password=(EditText)rootView.findViewById(R.id.new_password) ;
        confrim_password=(EditText)rootView.findViewById(R.id.confrim_password) ;
        reset_password_sumbit=(Button) rootView.findViewById(R.id.reset_password_sumbit) ;
        register_back_btn=(Button) rootView.findViewById(R.id.register_back_btn) ;

         email=Constant.getEmail();
        System.out.println("email-->"+email);
        onclickButton();

        return rootView;
    }

    public void onclickButton(){
        register_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KalendriaAppController.hideSoftKeyboard(getActivity());
                getActivity().finish();

            }
        });
        reset_password_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moldPassword= old_password.getText().toString();
                String mnewPassword= new_password.getText().toString();
                String mconfirPassword= confrim_password.getText().toString();
                if(TextUtils.isEmpty(moldPassword))
                {
                    old_password.setError("Please enter your old password");
                    old_password.requestFocus();
                }
                else if(TextUtils.isEmpty(mnewPassword)){
                    new_password.setError("Please enter your new password!");
                    new_password.requestFocus();
                }
                else if(TextUtils.isEmpty(mnewPassword) || mnewPassword.length() < 8){
                    new_password.setError("Your password should have minimum 8 character!");
                    new_password.requestFocus();
                }
                else if(TextUtils.isEmpty(mconfirPassword) ){
                    confrim_password.setError("Please enter your confirm password!");
                    confrim_password.requestFocus();
                }
                else if(TextUtils.isEmpty(mconfirPassword) || mconfirPassword.length() < 8){
                    confrim_password.setError("Your confirm password should have minimum 8 character!");
                    confrim_password.requestFocus();
                }
                else if (!mconfirPassword.equals(mnewPassword)) {
                    confrim_password.setError("Password does not match!");
                    confrim_password.requestFocus();
                }else{
                    if(KalendriaAppController.isNetworkConnected(getActivity())){
                        checkOldPassword(moldPassword,email);

                    }
                }
            }
        });
    }

    private void checkOldPassword(String oldpassword,String email) {
        try {
            gcm_device_id = new JSONObject();
            gcm_device_id.put("identifier", email);
            gcm_device_id.put("password", oldpassword);

            System.out.println("dfddf" + gcm_device_id);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constant.CHECK_OLD_PASSWROD, gcm_device_id, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("tag", response.toString());
                makeJsonObjectRequest();

                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Tag", "Error: " + error.getMessage());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);

                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);

                                String message = jsonObject.getString("message");
                                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                                dlgAlert.setMessage("The old password you entered is wrong!");
                                dlgAlert.setTitle("Kalendria ");
                                dlgAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //dismiss the dialog
                                                dialog.dismiss();

                                            }
                                        });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("error"+e.getMessage());
                           // Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
                hidepDialog();
            }

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };


        // Adding request to request queue
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void makeJsonObjectRequest() {
        try {

            String mnewPassword= new_password.getText().toString();
            String mconfirPassword= confrim_password.getText().toString();
            gcm_device_id = new JSONObject();
            gcm_device_id.put("password", mnewPassword);
            gcm_device_id.put("password_confirmation", mconfirPassword);
            gcm_device_id.put("id", Constant.getUserId(getActivity()));
            gcm_device_id.put("last_login", "");
            System.out.println("dfddf" + gcm_device_id);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constant.RESET_PASSWORD+Constant.getUserId(getActivity()), gcm_device_id, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("tag", response.toString());
                    // Parsing json object response response will be a json object
                    if (response != null) {

                        showSimplePopUp();
                        }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Tag", "Error: " + error.getMessage());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);

                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);

                                String message = jsonObject.getString("message");
                                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Kalendria ");
                                dlgAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //dismiss the dialog
                                                dialog.dismiss();

                                            }
                                        });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
                hidepDialog();
            }

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };


        // Adding request to request queue
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    private void showSimplePopUp() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
        helpBuilder.setTitle("Kalendria");
        helpBuilder.setMessage("Your password has been updated successfully. Please login again");
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }




    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
