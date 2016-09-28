package com.kalendria.kalendria.fragment;

/**
 * Created by murugan on 18/02/2016
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Category;
import com.kalendria.kalendria.activity.GiftVoucherActivity;
import com.kalendria.kalendria.activity.Venue;
import com.kalendria.kalendria.activity.VenueItem;
import com.kalendria.kalendria.adapter.DeshBoardAdapter;
import com.kalendria.kalendria.adapter.DeshBoardViewPagerAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.DeshBoard;
import com.kalendria.kalendria.model.DeshBoardViewPageModel;
import com.kalendria.kalendria.model.FilerModel;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.singleton.DeshBoardTypeSingleton;
import com.kalendria.kalendria.singleton.JsonResponce;
import com.kalendria.kalendria.utility.CommonSingleton;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class DashBoardFragment extends Fragment {


    public static String Tag = DashBoardFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    JSONObject gcm_device_id = null;
    JSONArray jsonArray = null;
    ListView list;
    DeshBoardAdapter adapter1;
    List<DeshBoard> custum_list ;//= new ArrayList<DeshBoard>();

    List<String> responceReseultFromServerSingletone;
    List<DeshBoard> categorySingletone;
    List<AddToCardVenueModel> addToCardSingletone;
    private SharedPreferences sharedPref;

    //Header page viewer

    ViewPager viewPager;
    DeshBoardViewPagerAdapter adapter;
    int currentPage, NUM_PAGES;
    ArrayList<DeshBoardViewPageModel> serviceList;

    ArrayList<String> COUNTRIES = new ArrayList<>();   // This array list is used to save all the Names for filter
    ArrayList<FilerModel> filterModel ; // This array list is used to save all the Names
    AutoCompleteTextView textView;
    ImageButton homepage_search_img_btn; // Image Button for search view
    Context thiscontext;

    public DashBoardFragment() {
        // Required empty public constructor

        responceReseultFromServerSingletone = JsonResponce.getInstance().getParamList();/*get the all the datas when app is initializing */
        categorySingletone = DeshBoardTypeSingleton.getInstance().getParamList();/*get only categories datas which is used to categories page for next and pres button on click */
        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();/*This line is used add to card venue name and servie list */
        categorySingletone.clear();
        custum_list=categorySingletone;
        //responceReseultFromServerSingletone.clear();
        // addToCardSingletone.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    Timer swipeTimer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 /*getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
                */

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        thiscontext = container.getContext();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);

        custum_list=categorySingletone;
        filterModel= KalendriaAppController.getInstance().mFilterModel;

        String date = (DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
        System.out.println("data&time-->"+date);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);

        textView = (AutoCompleteTextView) rootView.findViewById(R.id.searchBox);
        homepage_search_img_btn = (ImageButton) rootView.findViewById(R.id.homepage_search_img_btn);
        textView.setCursorVisible(false);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setCursorVisible(true);

            }
        });

        list = (ListView) rootView.findViewById(R.id.list);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        list.addFooterView(footerView);

        try {

            makeJsonObjectRequest();

        } catch (Exception e) {
            e.printStackTrace();
        }

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

               int cIndex=viewPager.getCurrentItem();
                if (cIndex+1 >= NUM_PAGES) {
                    cIndex = -1;
                }
                viewPager.setCurrentItem(cIndex+1, true);
            }
        };

        swipeTimer  = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);


        onclickButton();


        return rootView;
    }


    public void onclickButton(){

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                KalendriaAppController.hideKeyboar(getActivity());
            }
        });
        textView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                KalendriaAppController.hideKeyboar(getActivity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String position1 = String.valueOf(position);
                System.out.println("position id==>" + position1);

                if(custum_list.size()>position) {
                    Intent intent = new Intent(getActivity(), Category.class);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("position_id", position1);
                    editor.putString("type_id", custum_list.get(position).getTypeId());
                    editor.putString("type_name", custum_list.get(position).getTypeName());
                    editor.putString("sector", custum_list.get(position).sector);
                    editor.commit();
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getActivity(), GiftVoucherActivity.class);
                    startActivity(intent);
                }



            }
        });


        homepage_search_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mfilterText = textView.getText().toString().trim();

                if (!mfilterText.isEmpty() || mfilterText != null) {

                    SharedPreferences.Editor editor1 = sharedPref.edit();
                    editor1.putString("HdeaderName", mfilterText);
                    editor1.commit();
                    filterModel = KalendriaAppController.getInstance().mFilterModel;
                    for (int i = 0; i < filterModel.size(); i++) {

                        if (mfilterText.equalsIgnoreCase(filterModel.get(i).getName())) {
                            //Toast.makeText(getActivity(), "i am equal ", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("type_id", "");
                            editor.putString("category_id", "");
                            editor.putString("HeaderId", "");
                            editor.putString("ChildId", "");
                            editor.putString("venueID", "");
                            // editor.putString("HdeaderName", "");
                            editor.putString("sector", "1");


                            if (filterModel.get(i).getLevel().equalsIgnoreCase("type")) {
                                Intent intent = new Intent(getActivity(), Venue.class);

                                editor.putString("type_id", filterModel.get(i).getId());
                                editor.putString("HdeaderName", filterModel.get(i).getName());
                                editor.putString("sector", filterModel.get(i).sector);
                                editor.commit();
                                startActivity(intent);
                                return;
                            } else if (filterModel.get(i).getLevel().equalsIgnoreCase("sub_category 2")) {

                                String sub_category_2_id = filterModel.get(i).getId();
                                String sub_category_2_parent_id = filterModel.get(i).getParent_id();
                                String sub_category_1_id = null;
                                String sub_category_1_parent_id = null;
                                String category_id = null;
                                String category_parent_id = null;
                                String type_id = null;
                                String type_parent_id = null;
                                String sector = "";
                                for (int j = 0; j < filterModel.size(); j++) {
                                    if (filterModel.get(j).getId().equalsIgnoreCase(sub_category_2_parent_id)) {
                                        sub_category_1_id = filterModel.get(j).getId();
                                        sub_category_1_parent_id = filterModel.get(j).getParent_id();
                                        for (int k = 0; k < filterModel.size(); k++) {
                                            if (filterModel.get(k).getId().equalsIgnoreCase(sub_category_1_parent_id)) {
                                                category_id = filterModel.get(k).getId();
                                                category_parent_id = filterModel.get(k).getParent_id();
                                                for (int n = 0; n < filterModel.size(); n++) {
                                                    if (filterModel.get(n).getId().equalsIgnoreCase(category_parent_id)) {
                                                        type_id = filterModel.get(n).getId();
                                                        type_parent_id = filterModel.get(n).getParent_id();
                                                        sector = filterModel.get(n).sector;
                                                    }
                                                }

                                            }
                                        }

                                    }
                                }


                                Intent intent = new Intent(getActivity(), Venue.class);

                                editor.putString("type_id", type_id);
                                editor.putString("category_id", category_id);
                                editor.putString("HeaderId", sub_category_1_id);
                                editor.putString("ChildId", sub_category_2_id);
                                editor.putString("sector", sector);
                                //editor.putString("HdeaderName", "");
                                editor.commit();
                                startActivity(intent);
                                return;
                            } else if (filterModel.get(i).getLevel().equalsIgnoreCase("sub_category 1")) {

                                String sub_category_1_id = filterModel.get(i).getId();
                                String sub_category_1_parent_id = filterModel.get(i).getParent_id();
                                String category_id = null;
                                String category_parent_id = null;
                                String type_id = null;
                                String type_parent_id = null;
                                String sector = "";
                                for (int j = 0; j < filterModel.size(); j++) {
                                    if (filterModel.get(j).getId().equalsIgnoreCase(sub_category_1_parent_id)) {
                                        category_id = filterModel.get(j).getId();
                                        category_parent_id = filterModel.get(j).getParent_id();
                                        for (int k = 0; k < filterModel.size(); k++) {
                                            if (filterModel.get(k).getId().equalsIgnoreCase(category_parent_id)) {
                                                type_id = filterModel.get(k).getId();
                                                type_parent_id = filterModel.get(k).getParent_id();
                                                sector = filterModel.get(k).sector;
                                            }
                                        }

                                    }
                                }


                                Intent intent = new Intent(getActivity(), Venue.class);

                                editor.putString("type_id", type_id);
                                editor.putString("category_id", category_id);
                                editor.putString("HeaderId", sub_category_1_id);
                                editor.putString("sector", sector);
                                //editor.putString("HdeaderName", "");
                                editor.commit();
                                startActivity(intent);
                                return;

                            } else if (filterModel.get(i).getLevel().equalsIgnoreCase("category")) {

                                Intent intent = new Intent(getActivity(), Venue.class);
                                editor.putString("sector", filterModel.get(i).sector);
                                editor.putString("type_id", filterModel.get(i).getParent_id());
                                editor.putString("category_id", filterModel.get(i).getId());
                                editor.putString("sector", filterModel.get(i).sector);
                                // editor.putString("HdeaderName", filterModel.get(i).getName());
                                editor.commit();
                                startActivity(intent);
                                return;
                            } else {
                                Intent intent = new Intent(getActivity(), VenueItem.class);
                                editor.putString("sector", filterModel.get(i).sector);
                                editor.putString("venueID", filterModel.get(i).getId());
                                editor.commit();
                                startActivity(intent);
                                return;
                            }

                        }
                    }
                }


            }
        });
        // Inflate the layout for this fragment
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void makeJsonObjectRequest() {

        if(!KalendriaAppController.getInstance().isNetworkConnected(getActivity()))
        {
            Toast.makeText(getActivity(),"Please Check your internet connection",Toast.LENGTH_LONG).show();
            return;
        }
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constant.DESHBORAD, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                hidepDialog();
                System.out.println("deshboard1234-->" + response.toString());
                System.out.println("deshboard-->" + Constant.DESHBORAD);
                Log.d(Tag, response.toString());
                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        responceReseultFromServerSingletone.clear();
                        responceReseultFromServerSingletone.add(response.toString());

                        JSONArray service = response.getJSONArray("services");
                        serviceList = new ArrayList<DeshBoardViewPageModel>();
                        System.out.println("size of json array service-->" + service);
                        for (int j = 0; j < service.length(); j++) {
                            DeshBoardViewPageModel deshBoardViewPageModel = new DeshBoardViewPageModel();
                            JSONObject jsonObject = service.getJSONObject(j);

                            deshBoardViewPageModel.setServiceId(jsonObject.getString("id"));
                            deshBoardViewPageModel.setServiceName(jsonObject.getString("name"));
                            deshBoardViewPageModel.setServicePrice(jsonObject.getString("price"));
                            deshBoardViewPageModel.setServiceDiscount(jsonObject.getString("discount"));
                            deshBoardViewPageModel.setServiceDuration(jsonObject.getString("duration"));
                            deshBoardViewPageModel.sector= SafeParser.getString(jsonObject,"sector","1");


                            //City and region  Json object
                            JSONObject city = jsonObject.getJSONObject("city"); // in this object have id , name ,type , parantid too right now i using only name
                            deshBoardViewPageModel.setCity(city.getString("name"));
                            JSONObject region = jsonObject.getJSONObject("region"); // in this object have id , name ,type , parantid too right now i using only name
                            deshBoardViewPageModel.setRegion(region.getString("name"));

                            //Business Json object
                            JSONObject business = jsonObject.getJSONObject("business");
                            //System.out.println("business--"+business);
                            deshBoardViewPageModel.sector=business.getString("type");
                            deshBoardViewPageModel.setVenueID(business.getString("id"));
                            deshBoardViewPageModel.setVenueName(business.getString("name"));
                            deshBoardViewPageModel.setVenueOverallRatting(business.getString("overall_rating"));
                            deshBoardViewPageModel.setVenuReviewCount(business.getString("review_count"));


                            String mediaString = business.getString("media");
                            if (!mediaString.equalsIgnoreCase("null")) {
                                JSONObject media = business.getJSONObject("media");
                                deshBoardViewPageModel.setVenuImage(media.getString("url"));
                                deshBoardViewPageModel.setVenuImageThamp(media.getString("thumb"));

                            }

                            // System.out.println("media url-->"+media.getString("url"));
                            serviceList.add(deshBoardViewPageModel);
                        }

                        adapter = new DeshBoardViewPagerAdapter(getActivity(), serviceList);
                        viewPager.setAdapter(adapter);

                        currentPage = viewPager.getCurrentItem();
                        NUM_PAGES = serviceList.size();


                        jsonArray = response.getJSONArray("categorys");
                        System.out.println("size of json array" + jsonArray.length());

                      /*  ArrayList<String> categoryString =new ArrayList<>();   // This array list is used to save all the Names for filter
                        ArrayList<FilerModel> catergoryModel =new ArrayList<FilerModel>(); // This array list is used to save all the Names*/
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            boolean isSearch3 = SafeParser.getBoolen(jsonObject, "search", true);
                            if(isSearch3==false) {
                                Log.d("Subcategory", jsonObject.getString("name") + " Breaking..");
                                continue;
                            }
                            //======to get the fillter values start -======
                            FilerModel mfilerModel = new FilerModel();
                            mfilerModel.setParent_id(jsonObject.getString("parent"));
                            mfilerModel.setId(jsonObject.getString("id"));
                            mfilerModel.setName(jsonObject.getString("name"));
                            mfilerModel.setLevel(jsonObject.getString("level"));
                            mfilerModel.sector= SafeParser.getString(jsonObject,"sector","1");

                            KalendriaAppController.getInstance().mFilterModel.add(mfilerModel);
                            COUNTRIES.add(jsonObject.getString("name"));

                            //======to get the fillter values end -======


                            String type = jsonObject.getString("level");
                            String search = jsonObject.getString("search");
                            // code modified by magesh
                            String visible = jsonObject.getString("isVisible");

                            //System.out.println("Visible : "+visible);

                        if (type.equalsIgnoreCase("type") && search.equalsIgnoreCase("true") && visible.equalsIgnoreCase("true")) {
                            //if (type.equalsIgnoreCase("type")  && search.equalsIgnoreCase("true") && visible.equals("true") && visible.equalsIgnoreCase("true") ) {
                                DeshBoard deshBoard = new DeshBoard();
                                deshBoard.setTypeId(jsonObject.getString("id"));
                                deshBoard.setTypeName(jsonObject.getString("name"));

                                System.out.println("Name----------------------->"+jsonObject.getString("name"));

                                //code added by Magesh
                                deshBoard.sector= SafeParser.getString(jsonObject,"sector","1");
                                int  order = SafeParser.getInt(jsonObject,"menuOrder",0);
                                deshBoard.order=order;
                                String nameValue = jsonObject.getString("media");
                                //System.out.println(nameValue);
                                if (nameValue != "null") {
                                    System.out.println("i am in side");
                                    JSONObject jsonObject1 = new JSONObject(nameValue);
                                    String media_url = jsonObject1.getString("url");
                                    deshBoard.setType_image(jsonObject1.getString("url"));
                                    System.out.println(media_url);
                                }

                                //custum_list.add(deshBoard);
                                categorySingletone.add(deshBoard);

                            }

                        }

                        Collections.sort(categorySingletone, new Comparator<DeshBoard>() {
                            @Override
                            public int compare(DeshBoard o1, DeshBoard o2) {
                                return o1.order - o2.order;
                            }
                        });
                        Collections.sort(custum_list, new Comparator<DeshBoard>() {
                            @Override
                            public int compare(DeshBoard o1, DeshBoard o2) {
                                return o1.order - o2.order;
                            }
                        });

                        System.out.println("aaaaaaaaaaa--1234>" + categorySingletone);
                                System.out.println("aaaaaaaaaaa-->" + filterModel.size() + COUNTRIES.size());
                        JSONArray venues = response.getJSONArray("venues");
                        for (int i = 0; i < venues.length(); i++) {

                            JSONObject jsonObject_venu = venues.getJSONObject(i);
                            FilerModel mfilerModel = new FilerModel();
                            mfilerModel.setId(jsonObject_venu.getString("id"));
                            mfilerModel.setName(jsonObject_venu.getString("name"));
                            mfilerModel.setLevel("");
                            mfilerModel.sector= SafeParser.getString(jsonObject_venu,"sector","1");
                            mfilerModel.setParent_id("");
                            KalendriaAppController.getInstance().mFilterModel.add(mfilerModel);

                            boolean isSearch3 = SafeParser.getBoolen(jsonObject_venu, "search", true);
                            if( isSearch3==false) {
                                Log.d("Subcategory", jsonObject_venu.getString("name") + " Breaking..");
                                continue;
                            }
                            COUNTRIES.add(jsonObject_venu.getString("name"));
                        }


                        JSONArray location = response.getJSONArray("locations");
                        CommonSingleton.getInstance().parseLocation(location);


                        System.out.println("aaaaaaaaaaabbbb-->" + filterModel.size() + COUNTRIES.size());
                        //System.out.println("size of type array"+ custum_list.size());
                        /*for(DeshBoard homePage_model: custum_list) {
                            System.out.println("iiiiiiii"+homePage_model.getTypeName());
                        }
*/
                        //==== Filter Adapter start
                        try {
                            ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.autocomplete_layout, COUNTRIES);
                            textView.setAdapter(adapter);
                            //==== Filter Adapter start end
                            adapter1 = new DeshBoardAdapter(custum_list, getActivity());
                            list.setAdapter(adapter1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        // if responce is null write your commants here
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                String message = error.getMessage();
                if (error != null && message != null) {
                    VolleyLog.d(Tag, "Error: " + message);
                    //coded by Magesh : need to check null because on timeout we get null
                    Toast.makeText(getActivity(), KalendriaAppController.getErrorMessage(), Toast.LENGTH_SHORT).show();

                }

                // hide the progress dialog
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


    private void showpDialog() {


        try {
            if (!pDialog.isShowing())
                pDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hidepDialog() {
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
