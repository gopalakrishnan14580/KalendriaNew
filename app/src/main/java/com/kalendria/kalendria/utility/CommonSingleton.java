package com.kalendria.kalendria.utility;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.RegisterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Magesh on 5/22/16.
 */
public class CommonSingleton {

    static CommonSingleton myInstance = null;
    String Tag = "CommonSingleton";
    List<String> cityTextArray;
    List<String> regionTextArray;
    ArrayList<RegisterSpinner> cityModelArray =new ArrayList<RegisterSpinner>();
    ArrayList<RegisterSpinner> regionModelArray =new ArrayList<RegisterSpinner>();

    public static CommonSingleton getInstance()
    {
        if(myInstance==null)
            myInstance= new CommonSingleton();

        return myInstance;
    }

    public List<String> getCityList()
    {
      return cityTextArray;
    }
    public List<String> getRegionList()
    {
        return regionTextArray;
    }

    public ArrayList<RegisterSpinner> getCityMode()
    {
        return cityModelArray;
    }

    public ArrayList<RegisterSpinner> getRegionMode()
    {
        return regionModelArray;
    }



    public void parseLocation(JSONArray locations)
    {
        if(cityTextArray==null)
        cityTextArray = new ArrayList<>();
        else
            cityTextArray.clear();

        if(regionTextArray==null)
            regionTextArray = new ArrayList<>();
        else
            regionTextArray.clear();

        for(int i=0;i<locations.length();i++) {

            try {
                JSONObject jsonObject = locations.getJSONObject(i);
               RegisterSpinner homePage_model = new RegisterSpinner();
                homePage_model.setType(jsonObject.getString("type"));
                String type = jsonObject.getString("type");

                if (type.equalsIgnoreCase("city")) {
                    homePage_model.setParent(jsonObject.getString("parent"));
                    homePage_model.setId(jsonObject.getString("id"));
                    homePage_model.setName(jsonObject.getString("name"));
                    cityTextArray.add(jsonObject.getString("name"));
                    cityModelArray.add(homePage_model);
                }
                else if (type.equalsIgnoreCase("region")) {
                    homePage_model.setParent(jsonObject.getString("parent"));
                    homePage_model.setId(jsonObject.getString("id"));
                    homePage_model.setName(jsonObject.getString("name"));
                    regionTextArray.add(jsonObject.getString("name"));
                    regionModelArray.add(homePage_model);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        Log.d("CommonSingleton","Region count: "+regionModelArray.size());
    }

    public void fetchCityList() {


        try {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constant.REGISTER_SPINNER, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("CommonSingleTon", response.toString());

                    try {
                        // Parsing json object response response will be a json object
                        if (response != null) {

                            try {
                                String JsonString = response.toString();
                                Constant.savedData(JsonString, "kCityJSONKey");

                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                            JSONArray jsonArray = response.getJSONArray("locations");
                            parseLocation(jsonArray);
                            System.out.println("size of json array" + jsonArray.length());


                        }else{
                            // if responce is null write your commants here
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(Tag, "Error: " + error.getMessage());

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getUserProfileInformation() {

        String url= Constant.GET_RROFILE+Constant.getUserId(KalendriaAppController.getInstance());
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
                        Constant.savedData(wallets, "kWalletsKey");


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
