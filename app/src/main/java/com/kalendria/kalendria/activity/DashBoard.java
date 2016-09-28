package com.kalendria.kalendria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.utility.CommonSingleton;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DashBoard extends KalendriaActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonSingleton.getInstance().fetchCityList();
        getUserProfileInformation();
        KalendriaAppController.getInstance().initGPS(this);
        displayView(0);

    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
        super.onBackPressed();
    }

    private void getUserProfileInformation() {

        String url= Constant.GET_RROFILE+Constant.getUserId(this);
        System.out.println("getprofile_url-->" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("DashBoard", response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        String first_name = response.getString("first_name");
                        String last_name = response.getString("last_name");
                        String email = response.getString("email");
                        String city = response.getString("city");
                        String gender = response.getString("gender");
                        String phone = response.getString("phone");
                        String address = response.getString("address");
                       // String profile_image = response.getString("profile_image");
                        String points = response.getString("points");
                        String wallets = response.getString("credit");


                        try {
                            JSONObject profile = SafeParser.getObject(response, "profile_image");
                            if (profile != null) {
                                if(profile.has("thumb")) {
                                    String profile_image_thump = profile.getString("thumb");
                                    Constant.setProfileImage(profile_image_thump);
                                }
                                if(profile.has("medium")) {
                                    String profile_image_medium = profile.getString("medium");

                                    Constant.setProfileImageMedium(profile_image_medium);
                                }
                            }
                        }
                        catch (Exception ex ){ex.printStackTrace();}


                        Constant.setCity(city);
                        Constant.setFirstName(first_name);
                        Constant.setLastName(last_name);
                        Constant.setEmail(email);

                        Constant.savedData(gender, "kGenderKey");
                        Constant.savedData(address, "kAddressKey");
                        Constant.savedData(phone, "kphoneKey");
                        Constant.savedData(points,"kLoyalityKey");
                        Constant.savedData(wallets,"kWalletsKey");
                        drawerFragment.setUserDetails();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
               // hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("DashBoard", "Error: " + error.getMessage());
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







}
