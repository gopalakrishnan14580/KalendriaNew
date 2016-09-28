package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.VenueItem;
import com.kalendria.kalendria.adapter.BookWityLoyalityAdapter;
import com.kalendria.kalendria.adapter.VenueAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.BookWithLoyalityModel;
import com.kalendria.kalendria.model.Venue;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by murugan on 1/14/2016.
 */
public class BookWithLoyaltyFragment extends Fragment {
    public static String TAG = BookWithLoyaltyFragment.class.getSimpleName();
    EditText maximum_et,minimum_et;
    ImageView searchImage;
    ListView lv_book_with_loyalty;
    private ProgressDialog pDialog;
    ArrayList<BookWithLoyalityModel> custum_list =new ArrayList<BookWithLoyalityModel>();
    ArrayList<BookWithLoyalityModel> searchList =new ArrayList<BookWithLoyalityModel>();
    BookWityLoyalityAdapter adapter1;
    private SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_with_loyalty, container, false);
        maximum_et=(EditText)rootView.findViewById(R.id.maximum_et);
        minimum_et=(EditText)rootView.findViewById(R.id.minimum_et);
        searchImage=(ImageView)rootView.findViewById(R.id.searchImage);
        lv_book_with_loyalty=(ListView) rootView.findViewById(R.id.lv_book_with_loyalty);
        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        if(KalendriaAppController.isNetworkConnected(getActivity())){
            makeJsonObjectRequest();
        }else{
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
        }

        allActions();

        KalendriaAppController.getInstance().hideSoftKeyboard(getActivity());

        TextView emptyText = (TextView)rootView.findViewById(android.R.id.empty);
        lv_book_with_loyalty.setEmptyView(emptyText);
        return rootView;

    }

    public void allActions()
    {
        lv_book_with_loyalty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Intent intent = new Intent(getActivity(), VenueItem.class);
                SharedPreferences.Editor editor = sharedPref.edit();
               /* editor.putString("HdeaderName", custum_list.get(position).getName());
                editor.putString("ServiceName", custum_list.get(position).getName_service());
                editor.putString("Retting", custum_list.get(position).getOverallRating());*/

                editor.putString("venueID", searchList.get(position).getBuisnessId());
              /*  editor.putString("lat", custum_list.get(position).getLat());
                editor.putString("long", custum_list.get(position).getLongId());
                editor.putString("ImageUrl", custum_list.get(position).getImageUrl());*/
                editor.commit();
                startActivity(intent);
            }
        });


        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //searchList.clear();


                try {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                catch (Exception ex)
                {

                    ex.printStackTrace();
                }


               // KalendriaAppController.hideKeyboar(getActivity());

                String min = maximum_et.getText().toString().trim();
                String max = minimum_et.getText().toString().trim();
                searchList.clear();
               // ArrayList temp = new ArrayList();
                if (max != null && min != null) {

                    for (int i = 0; i < custum_list.size(); i++) {
                        int foo = Integer.parseInt(custum_list.get(i).getPoints());
                        int max1 = -1;
                        try {
                            max1 = Integer.parseInt(max);
                        }
                        catch (Exception ex)
                        {
                            max1 = -1;
                            ex.printStackTrace();
                        }

                        int min1 = -1;
                        try {
                            min1 = Integer.parseInt(min);
                        }
                        catch (Exception ex)
                        {
                            min1 = -1;
                            ex.printStackTrace();
                        }

                        //int min1 = Integer.parseInt(min);

                        if ( (min1 <= foo || min1==-1) && (max1 >= foo|| max1==-1)) {

                            /*
                            BookWithLoyalityModel selectedModel = new BookWithLoyalityModel();
                            selectedModel.setId(custum_list.get(i).getId());
                            selectedModel.setName(custum_list.get(i).getName());
                            selectedModel.setServiceName(custum_list.get(i).getServiceName());
                            selectedModel.setServiceId(custum_list.get(i).getServiceId());
                            selectedModel.setOverallRating(custum_list.get(i).getOverallRating());
                            selectedModel.setPoints(custum_list.get(i).getPoints());
                            selectedModel.setCity(custum_list.get(i).getCity());
                            selectedModel.setRegion(custum_list.get(i).getRegion());
                            selectedModel.setImageUrl(custum_list.get(i).getImageUrl());
                            selectedModel.setImageUrlThumb(custum_list.get(i).getImageUrlThumb());

                            maxMin.add(selectedModel);
                            System.out.println("result-->" + foo);
                            */
                            searchList.add(custum_list.get(i));
                        }
                    }
                    Collections.sort(searchList, BookWithLoyalityModel.sortPoints);

                   // adapter1 = new BookWityLoyalityAdapter(maxMin, getActivity());
                  //  lv_book_with_loyalty.setAdapter(adapter1);
                } else {
                   // adapter1 = new BookWityLoyalityAdapter(custum_list, getActivity());
                   // lv_book_with_loyalty.setAdapter(adapter1);

                    //Toast.makeText(getActivity(), "Please choose your points ", Toast.LENGTH_SHORT).show();
                }
                adapter1.notifyDataSetChanged();
            }
        });

    }
    private void makeJsonObjectRequest() {
        showpDialog();

       String url=Constant.HOST +"/api/v1/service?populate=city,region,business&where={\"business\":{\"!\":\"null\"},\"enable_loyalty_points\":1,\"is_search\":1}";
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"--->"+response);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        BookWithLoyalityModel selectedModel=new BookWithLoyalityModel();
                        selectedModel.setDuration(jsonObject.getString("duration"));
                        selectedModel.setServiceId(jsonObject.getString("id"));
                        selectedModel.setServiceName(jsonObject.getString("name"));

                        selectedModel.setOverallRating(jsonObject.getString("review_count"));
                        selectedModel.setPoints(jsonObject.getString("points"));

                        JSONObject bussiness=jsonObject.getJSONObject("business");
                        selectedModel.setBuisnessName(bussiness.getString("name"));
                        selectedModel.setBuisnessId(bussiness.getString("id"));
                        selectedModel.setSector(SafeParser.getString(bussiness,"type","1"));


                        JSONObject city=jsonObject.getJSONObject("city");
                        JSONObject region=jsonObject.getJSONObject("region");


                        selectedModel.setCity(city.getString("name"));
                        selectedModel.setRegion(region.getString("name"));


                        selectedModel.setOverallRating(bussiness.getString("overall_rating"));
                        String nameValue = bussiness.getString("media");
                        if(nameValue !="null" ) {
                            JSONObject jsonObject1=new JSONObject(nameValue);
                            selectedModel.setImageUrl(jsonObject1.getString("url"));
                            selectedModel.setImageUrlThumb(jsonObject1.getString("thumb"));
                        }
                        custum_list.add(selectedModel);

                    }
                    searchList.clear();
                    searchList.addAll(custum_list);
                    adapter1 = new BookWityLoyalityAdapter(searchList,getActivity());
                    lv_book_with_loyalty.setAdapter(adapter1);

                } catch (JSONException e) {
                    System.out.println(TAG+e.getMessage());

                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.d(TAG, "Error : " + error.getMessage());
                // final int statusCode = error.networkResponse.statusCode;
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("content-type", "application/json");
                return params;
            }

        };

        //queue.add(jsonObjReq);
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);

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
