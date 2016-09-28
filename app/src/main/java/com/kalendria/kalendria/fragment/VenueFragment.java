package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Login;
import com.kalendria.kalendria.activity.VenueItem;
import com.kalendria.kalendria.adapter.VenueAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.Category;
import com.kalendria.kalendria.model.DeshBoard;
import com.kalendria.kalendria.model.RegisterSpinner;
import com.kalendria.kalendria.model.ServiceModal;
import com.kalendria.kalendria.model.SubCategoryChild;
import com.kalendria.kalendria.model.SubCategoryHeader;
import com.kalendria.kalendria.singleton.DeshBoardTypeSingleton;
import com.kalendria.kalendria.singleton.JsonResponce;
import com.kalendria.kalendria.utility.AppLocationService;
import com.kalendria.kalendria.utility.CommonSingleton;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mansoor on 11/03/16.
 */
public class VenueFragment extends Fragment {


    public static String TAG = VenueFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    JSONObject gcm_device_id = null;
    public static String Tag = Login.class.getSimpleName();
    ArrayList<ServiceModal> custum_list = new ArrayList<ServiceModal>();
    VenueAdapter adapter1;
    ListView venulistView;
    TextView selected_category, statusLabel;
    private SharedPreferences sharedPref;
    Button btnnotification;
    ImageView filter_btn,btnsettings;

    private String selectedCategoryId;
    private String selectedTypeId;
    private String selectedHeaderId;
    private String selectedChildId;
    private String selectedHeaderName;
    private String selectedSector;
    public ArrayList<RegisterSpinner> arrCommunity;


    //Filter start
    TextView filderDateTxt, filderTimeTxt, filter_close_txt;
    TextView category_close, subcategory_close, subcategory2_close, time_close, date_close, gender_close;
    TextView often_close, community_close, city_close;
    Button filterClearBtn, filterSearchBtn;
    EditText filderPriceFrom, filderPriceTo, filderKeyWordToSerachTxt;
    TextView filter_keyword_close_txt;
    TextView typeAutoCompletedText, categoiresAuctoCompletedText, subCategoreisHeaderAutoCompletedText, subCategoriesChildAutoCompletedText, filderGenderAutoCompletedText;

    List<String> mjsonResonceSingletone;
    List<DeshBoard> mdeshboard_type_singletone;

    ArrayList<String> typeStringArray = new ArrayList<>();   // This array list is used to save all the Names for filter
    ArrayList<DeshBoard> typeModelArray = new ArrayList<DeshBoard>(); // This array list is used to save all the Names

    ArrayList<String> categoriesStringArray = new ArrayList<>();   // This array list is used to save all the Names for filter
    ArrayList<Category> categoriesModelArray = new ArrayList<Category>(); // This array list is used to save all the Names

    ArrayList<String> subcategoriesHeaderstring = new ArrayList<>();   // This array list is used to save all the Names for filter
    ArrayList<SubCategoryHeader> subcategoriesHeadermodel = new ArrayList<SubCategoryHeader>(); // This array list is used to save all the Names

    ArrayList<String> subcategoriesChildStringArray = new ArrayList<>();
    ArrayList<SubCategoryChild> subcategoriesChildmodelArray = new ArrayList<SubCategoryChild>();

    ArrayList<String> gender, venueArray, petArray;
    ArrayList<String> oftenArray;

    ArrayList<String> regionTextArray = new ArrayList<String>();
    ArrayList<RegisterSpinner> regionModal = new ArrayList();


    private AlertDialog myDialog;
    private View alertView;

    private AlertDialog myDialog1;
    private View alertView1;

    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;


    LinearLayout locaion, how_often_LL, categoreyLL, subCategoryHeaderLL, subCategoriyChildLL, LinearLayout6;
    LinearLayout community_LL;
    LinearLayout region_LL;
    LinearLayout LinearLayoutVenue;

    AutoCompleteTextView filter_type_venue;

    FrameLayout need_ironing;
    TextView filter_location_home_and_garden, how_often_txt, filter_location_city;

    TextView price_to_close, price_close;
    RadioGroup register_radiogroup;
    int pos;
    String radiogroup_value;

    //Filter sent to server values
    String sendTypeIdToServer, sendTo_subcategories, sentTosubcategories2, sentTo_categories_act, sendToServerGender;
    String sendOften, sendCityID, selectedVenueType;
    String sendCity, sendCommunity, sendCommunityID;

    String dayName;
    //Button btnsettings;

    RadioGroup radioGroup;
    Button sortButton;
    String keyForSharting = "";
    TextView sortingCloseTxt;
    // Filter set all categories
    String catergoryName = "", subcatergoryName = "", subcatergoryName2 = "";

    TextView btnNWShowLocation, selected_text;


    AppLocationService appLocationService;
    int geocoderMaxResults = 1;

    double latitude;
    double longitude;
    List<String> cityTextArray;
    ArrayList<RegisterSpinner> cityModelArray = new ArrayList<RegisterSpinner>();
    String spinner_selected_id = "3";


    //Filter end
    public VenueFragment() {
        // Required empty public constructor
        mjsonResonceSingletone = JsonResponce.getInstance().getParamList();
        mdeshboard_type_singletone = DeshBoardTypeSingleton.getInstance().getParamList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View gridview = inflater.inflate(R.layout.categoryselect, container, false);

        appLocationService = new AppLocationService(getActivity());

        btnNWShowLocation = (TextView) gridview.findViewById(R.id.btnmenu);
        selected_text = (TextView) gridview.findViewById(R.id.selected_text);
        statusLabel = (TextView) gridview.findViewById(R.id.statusLabel);


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());


        LayoutInflater inflateralert = LayoutInflater.from(getActivity());
        alertView = inflateralert.inflate(R.layout.filter, null);
        builder.setView(alertView);

        LayoutInflater inflateralert1 = LayoutInflater.from(getActivity());
        alertView1 = inflateralert1.inflate(R.layout.sharting, null);
        builder1.setView(alertView1);

        //builder.setTitle("Log in");
        //builder.setIcon(R.drawable.snowflake);

        // AutoCompleteTextView for filter
        arrCommunity = new ArrayList<>();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        typeAutoCompletedText = (TextView) alertView.findViewById(R.id.filter_type_act);
        categoiresAuctoCompletedText = (TextView) alertView.findViewById(R.id.filter_categories_act);
        subCategoreisHeaderAutoCompletedText = (TextView) alertView.findViewById(R.id.filter_subcategories_act1);
        subCategoriesChildAutoCompletedText = (TextView) alertView.findViewById(R.id.filter_subcategories_act2);
        filderGenderAutoCompletedText = (TextView) alertView.findViewById(R.id.filter_gender);
        filter_location_city = (TextView) alertView.findViewById(R.id.filter_location_city);

        filter_type_venue = (AutoCompleteTextView) alertView.findViewById(R.id.filter_type_venue);
        LinearLayoutVenue = (LinearLayout) alertView.findViewById(R.id.LinearLayoutVenue);


//      Homa and garden start
        locaion = (LinearLayout) alertView.findViewById(R.id.locaion);
        categoreyLL = (LinearLayout) alertView.findViewById(R.id.LinearLayout2);
        subCategoryHeaderLL = (LinearLayout) alertView.findViewById(R.id.LinearLayout3);
        subCategoriyChildLL = (LinearLayout) alertView.findViewById(R.id.LinearLayout4);
        LinearLayout6 = (LinearLayout) alertView.findViewById(R.id.LinearLayout6);

        how_often_LL = (LinearLayout) alertView.findViewById(R.id.how_often_LL);
        need_ironing = (FrameLayout) alertView.findViewById(R.id.need_ironing);
        filter_location_home_and_garden = (TextView) alertView.findViewById(R.id.filter_location_home_and_garden);
        how_often_txt = (TextView) alertView.findViewById(R.id.how_often_txt);
        often_close = (TextView) alertView.findViewById(R.id.how_often_close);
        community_close = (TextView) alertView.findViewById(R.id.community_close);

        price_close = (TextView) alertView.findViewById(R.id.price_close);
        price_to_close = (TextView) alertView.findViewById(R.id.price_to_close);

        //      Homa and garden end

        community_LL = (LinearLayout) alertView.findViewById(R.id.location_ll);
        region_LL = (LinearLayout) alertView.findViewById(R.id.RegionLayout);


        category_close = (TextView) alertView.findViewById(R.id.category_close);
        subcategory_close = (TextView) alertView.findViewById(R.id.subcategory_close);
        subcategory2_close = (TextView) alertView.findViewById(R.id.subcategory2_close);
        date_close = (TextView) alertView.findViewById(R.id.date_close);
        time_close = (TextView) alertView.findViewById(R.id.time_close);
        gender_close = (TextView) alertView.findViewById(R.id.option);


        // filter

        filter_keyword_close_txt = (TextView) alertView.findViewById(R.id.keyword_close);

        filter_keyword_close_txt.setVisibility(View.INVISIBLE);
        gender_close.setVisibility(View.INVISIBLE);
        price_to_close.setVisibility(View.INVISIBLE);
        price_close.setVisibility(View.INVISIBLE);

        filderKeyWordToSerachTxt = (EditText) alertView.findViewById(R.id.filter_keyword_search);
        filter_close_txt = (TextView) alertView.findViewById(R.id.filter_close_txt);
        filderPriceFrom = (EditText) alertView.findViewById(R.id.filter_price_from);
        filderPriceTo = (EditText) alertView.findViewById(R.id.filter_price_to);
        filderDateTxt = (TextView) alertView.findViewById(R.id.filter_date);
        filderTimeTxt = (TextView) alertView.findViewById(R.id.filter_time);
        filterClearBtn = (Button) alertView.findViewById(R.id.filter_clear_btn);
        filterSearchBtn = (Button) alertView.findViewById(R.id.filter_search_btn);

        radioGroup = (RadioGroup) alertView1.findViewById(R.id.rgOpinion);
        sortButton = (Button) alertView1.findViewById(R.id.sorting_save_btn);
        sortingCloseTxt = (TextView) alertView1.findViewById(R.id.sharting_close_txt);


        builder.setCancelable(false);
        builder1.setCancelable(false);
        myDialog = builder.create();
        myDialog1 = builder1.create();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);
        selected_category = (TextView) gridview.findViewById(R.id.selected_category);
        filter_btn = (ImageView) gridview.findViewById(R.id.filter_btn);
        venulistView = (ListView) gridview.findViewById(R.id.venulist);
        venulistView.setTranscriptMode(0);//added my magesh
        register_radiogroup = (RadioGroup) gridview.findViewById(R.id.register_radiogroup_filter);

        btnsettings = (ImageView) gridview.findViewById(R.id.btnsettings1);
        btnnotification = (Button) gridview.findViewById(R.id.btnnotification);

        adapter1 = new VenueAdapter(custum_list, getActivity(), this);
        venulistView.setAdapter(adapter1);




        /*GPS checking method*/

        /*if(KalendriaAppController.isNetworkConnected(getActivity())){

            try {
                Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

                if (nwLocation != null) {

                    latitude = nwLocation.getLatitude();
                    longitude = nwLocation.getLongitude();
                    selected_text.setText(getLocality(getActivity()));
                    if (KalendriaAppController.isNetworkConnected(getActivity())) {
                        makeJsonObjectRequest(spinner_selected_id);
                    } else {
                        Toast.makeText(getActivity(), "Please check your location", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    showSettingsAlert("NETWORK");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }*/

        loadDefaultFilter();

        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (KalendriaAppController.isNetworkConnected(getActivity())) {
            makeJsonObjectRequest(spinner_selected_id);
        } else {
            showStatusLabel(true);
            Toast.makeText(getActivity(), "Please check your location", Toast.LENGTH_SHORT).show();
        }
        getFiler();
        getDOB();
        onclick();
        return gridview;
    }

    private void showStatusLabel(boolean hasShow) {
        if (hasShow) {
            statusLabel.setVisibility(View.VISIBLE);
            venulistView.setVisibility(View.INVISIBLE);
        } else {
            statusLabel.setVisibility(View.INVISIBLE);
            venulistView.setVisibility(View.VISIBLE);

        }
    }

    public void loadDefaultFilter() {
        selectedCategoryId = Constant.getCategoryId(getActivity());
        selectedTypeId = Constant.getTypeId(getActivity());
        selectedHeaderId = Constant.subCategeryHeaderID(getActivity());
        selectedHeaderName = Constant.subCategeryHeaderName(getActivity());
        selectedChildId = Constant.subCategeryChildId(getActivity());
        selectedSector = Constant.getSector(getActivity());

        String city = Constant.getUserGPSCity();

        boolean isFound = false;
        if (city != null && city.length() > 0) {
            selected_text.setText(city);
            cityTextArray = CommonSingleton.getInstance().getCityList();
            ArrayList<RegisterSpinner> cityModel = CommonSingleton.getInstance().getCityMode();

            for (RegisterSpinner spinner : cityModel) {

                if (city.equalsIgnoreCase(spinner.getName())) {
                    sendCity = spinner.getName();
                    sendCityID = spinner.getId();
                    isFound = true;
                    break;
                }

            }

        } else
            selected_text.setText("Dubai");

        if (isFound == false) {
            sendCity = "Dubai";
            sendCityID = "3";
        }


        filter_location_city.setText(sendCity);




        venueArray = new ArrayList<>();
        venueArray.add("Venue");
        venueArray.add("Home");

        petArray = new ArrayList<>();
        petArray.add("Venue");
        petArray.add("Mobile");


        filter_type_venue.setText(venueArray.get(0));
        selectedVenueType = venueArray.get(0);

        gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Children");

        oftenArray = new ArrayList<>();
        oftenArray.add("Just Once");
        oftenArray.add("Weekly");
        oftenArray.add("Every 2 Weeks");
        oftenArray.add("2 times/week");
        oftenArray.add("3 times/week");

        updateFilterUI();
    }

    /*
    public void updateFilterUI() {

        if (selectedSector.equalsIgnoreCase("2")) {
            how_often_LL.setVisibility(View.VISIBLE);
            need_ironing.setVisibility(View.GONE);
            LinearLayout6.setVisibility(View.GONE);
            filter_location_home_and_garden.setHint("Where you are located ?");
            how_often_txt.setHint("How often?");
            LinearLayoutVenue.setVisibility(View.GONE);

        } else {
            filter_location_home_and_garden.setHint("Community");
            how_often_LL.setVisibility(View.GONE);
            need_ironing.setVisibility(View.GONE);
            LinearLayout6.setVisibility(View.VISIBLE);
            LinearLayoutVenue.setVisibility(View.VISIBLE);
        }

    }
    */

    @Override
    public void onResume() {
        super.onResume();

    }

    private void updateFilterUI() {

        //Handle Venu Section
        if (selectedSector.equalsIgnoreCase("1")) {
            filter_type_venue.setText(venueArray.get(0));
            selectedVenueType = venueArray.get(0);
            LinearLayoutVenue.setVisibility(View.VISIBLE);
        } else if (selectedSector.equalsIgnoreCase("4")) {

            filter_type_venue.setText(petArray.get(0));
            selectedVenueType = petArray.get(0);
            LinearLayoutVenue.setVisibility(View.VISIBLE);
        } else {
            filter_type_venue.setText("");
            selectedVenueType = "";
            LinearLayoutVenue.setVisibility(View.GONE);
        }

        //Handle Male/Female Section
        if (selectedSector.equalsIgnoreCase("1"))
            LinearLayout6.setVisibility(View.VISIBLE);
        else
            LinearLayout6.setVisibility(View.GONE);

        // Handle How often
        if (selectedSector.equalsIgnoreCase("2")) {
            // locaion.setVisibility(View.VISIBLE);
            LinearLayoutVenue.setVisibility(View.GONE);
            how_often_LL.setVisibility(View.VISIBLE);
            need_ironing.setVisibility(View.GONE);

            filter_location_home_and_garden.setHint("Where you are located ?");

        }
        else {

            subCategoryHeaderLL.setVisibility(View.GONE);
            subCategoriyChildLL.setVisibility(View.GONE);
            // locaion.setVisibility(View.GONE);
            filter_location_home_and_garden.setHint("Community");
            how_often_LL.setVisibility(View.GONE);
            need_ironing.setVisibility(View.GONE);


        }



    }

    private void changeRegion(String cityId) {


        if (selectedSector.equalsIgnoreCase("1") || selectedSector.equalsIgnoreCase("4")) {


            getRegions(cityId, selectedSector, selectedVenueType);

        } else if (selectedSector.equalsIgnoreCase("1")) {

            getRegions(cityId, selectedSector, "Venue");

        } else {

            getRegions(cityId, selectedSector, "");

        }


    }


    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(provider + " SETTINGS");
        alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public List<Address> getGeocoderAddress(Context context) {

        Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
        if (nwLocation != null) {

            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            try {
                /**
                 * Geocoder.getFromLocation - Returns an array of Addresses
                 * that are known to describe the area immediately surrounding the given latitude and longitude.
                 */
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, this.geocoderMaxResults);

                return addresses;
            } catch (IOException e) {
                //e.printStackTrace();
                Log.e("###########", "Impossible to connect to Geocoder", e);
            }
        }

        return null;
    }


    public String getAddressLine(Context context) {

        List<Address> addresses = getGeocoderAddress(context);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);

            return addressLine;
        } else {
            return null;
        }
    }

    public String getLocality(Context context) {
        List<Address> addresses = getGeocoderAddress(context);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getLocality();

            return locality;
        } else {
            return null;
        }
    }

    public String getPostalCode(Context context) {
        List<Address> addresses = getGeocoderAddress(context);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String postalCode = address.getPostalCode();

            return postalCode;
        } else {
            return null;
        }
    }

    public String getCountryName(Context context) {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String countryName = address.getCountryName();
            return countryName;
        } else {
            return null;
        }
    }


    public void getRegions(String city, String sector, String petType) {


        showpDialog();


        String url = Constant.HOST;
        url += "api/v1/external/business/regions?city=" + city + "&sector=" + sector + "&pet_type=" + petType;


        JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray arrJsonResult) {
                Log.d(Tag, arrJsonResult.toString());
                hidepDialog();

                if (arrJsonResult != null) {

                    if (arrCommunity.size() > 0) {
                        arrCommunity.clear();
                    }
                    regionTextArray.clear();
                    if (arrJsonResult.length() > 0) {


                        for (int i = 0; i < arrJsonResult.length(); i++) {
                            try {


                                JSONObject dictRegion = arrJsonResult.getJSONObject(i);
                                RegisterSpinner region = new RegisterSpinner();

                                int id = SafeParser.getInt(dictRegion, "id", 0);
                                String lat = SafeParser.getString(dictRegion, "lat", "");
                                String longi = SafeParser.getString(dictRegion, "long", "");
                                String name = SafeParser.getString(dictRegion, "name", "");
                                String type = SafeParser.getString(dictRegion, "type", "");
                                int parent = SafeParser.getInt(dictRegion, "parent", 0);

                                region.setId(id + "");
                                region.setLatitude(lat);
                                region.setLongitude(longi);
                                region.setName(name);
                                region.setType(type);
                                region.setParent(parent + "");


                                regionTextArray.add(name);
                                arrCommunity.add(region);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }

                    }

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        KalendriaAppController.getInstance().addToRequestQueue(jreq);


    }


    private void getCityList() {

        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constant.REGISTER_SPINNER, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(Tag, response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        JSONArray jsonArray = response.getJSONArray("locations");
                        System.out.println("size of json array" + jsonArray.length());

                        cityTextArray = new ArrayList<>();
                        cityModelArray.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            com.kalendria.kalendria.model.RegisterSpinner homePage_model = new com.kalendria.kalendria.model.RegisterSpinner();

                            homePage_model.setType(jsonObject.getString("type"));
                            String type = jsonObject.getString("type");

                            if (type.equalsIgnoreCase("city")) {
                                homePage_model.setParent(jsonObject.getString("parent"));
                                homePage_model.setId(jsonObject.getString("id"));
                                homePage_model.setName(jsonObject.getString("name"));
                                cityTextArray.add(jsonObject.getString("name"));
                                cityModelArray.add(homePage_model);

                            }
                        }

                        final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityTextArray);

                        new AlertDialog.Builder(getActivity())
                                .setTitle("City")
                                .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        selected_text.setText(cityTextArray.get(which).toString());
                                        spinner_selected_id = cityModelArray.get(which).getId();
                                        dialog.dismiss();
                                        if (KalendriaAppController.isNetworkConnected(getActivity())) {
                                            makeJsonObjectRequest(spinner_selected_id);
                                        } else {
                                            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }).create().show();

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
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void setFilterValues() {


        String TypeId = selectedTypeId;
        String categoryId = selectedCategoryId;
        String Headerid = selectedHeaderId;
        String HeaderName = selectedHeaderName;
        String ChildId = selectedChildId;


        //========== Get the type Name ============//

        typeStringArray.clear();
        String typeName = "";
        for (int i = 0; i < typeModelArray.size(); i++) {
            try {

                DeshBoard dashBoard = typeModelArray.get(i);

                if (dashBoard.getSector() > 0) {
                    typeStringArray.add(typeModelArray.get(i).getTypeName());
                    if (TypeId.equalsIgnoreCase(typeModelArray.get(i).getTypeId())) {

                        typeName = typeModelArray.get(i).getTypeName();

                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        typeAutoCompletedText.setText(typeName);
        if (typeAutoCompletedText.getText() != null) {

            if (typeAutoCompletedText.getText().toString().equalsIgnoreCase("Home & Garden")) {

                selectedSector = "2";
                updateFilterUI();


            }
        }
        //========== Get the type end ============//


        //========== Get the categories Name start  ============//
        catergoryName = "";
        categoriesStringArray.clear();
        for (int i = 0; i < categoriesModelArray.size(); i++) {

            String categoryParentId = categoriesModelArray.get(i).getCategoryImages(); // categorie parant id
            if (categoryParentId.equalsIgnoreCase(TypeId)) {
                categoriesStringArray.add(categoriesModelArray.get(i).getCategoryName());
                if (categoriesModelArray.get(i).getCategoryId().equalsIgnoreCase(categoryId)) {
                    catergoryName = categoriesModelArray.get(i).getCategoryName();
                }
            }
        }

        boolean isCateogryAvailable = true;
        if (catergoryName != null && catergoryName.length() > 0) {
            category_close.setVisibility(View.VISIBLE);
            categoreyLL.setVisibility(View.VISIBLE);
        } else {
            isCateogryAvailable = false;
            category_close.setVisibility(View.INVISIBLE);
            subCategoryHeaderLL.setVisibility(View.GONE);
            subCategoriyChildLL.setVisibility(View.GONE);
        }
        categoiresAuctoCompletedText.setText(catergoryName);
        //========== Get the categories Name end  ============//


        //========== Get the subcategoriesHeader Name start  ============//
        subcategoriesHeaderstring.clear();
        subcatergoryName = "";
        if (isCateogryAvailable) {
            for (int i = 0; i < subcategoriesHeadermodel.size(); i++) {

                String subcategoryParentId = subcategoriesHeadermodel.get(i).getSubcategoryHeaderParent(); // categorie parant id
                if (subcategoryParentId.equalsIgnoreCase(categoryId)) {
                    subcategoriesHeaderstring.add(subcategoriesHeadermodel.get(i).getSubcategoryHeaderName());
                    if (subcategoriesHeadermodel.get(i).getSubcategoryHeaderId().equalsIgnoreCase(Headerid)) {
                        subcatergoryName = subcategoriesHeadermodel.get(i).getSubcategoryHeaderName();
                    }
                }
            }
        }

        boolean isSubCateogryAvailable = true;
        if (subcatergoryName != null && subcatergoryName.length() > 0) {
            subcategory_close.setVisibility(View.VISIBLE);
            subCategoryHeaderLL.setVisibility(View.VISIBLE);
            subCategoriyChildLL.setVisibility(View.VISIBLE);
        } else {
            isSubCateogryAvailable = false;
            subCategoriyChildLL.setVisibility(View.GONE);
            subcategory_close.setVisibility(View.INVISIBLE);
        }
        subCategoreisHeaderAutoCompletedText.setText(subcatergoryName);

        //========== Get the subcategoriesHeader Name end  ============//


        //========== Get the subcategorieschild Name start  ============//
        subcategoriesChildStringArray.clear();
        subcatergoryName2 = "";
        if (isSubCateogryAvailable) {
            for (int i = 0; i < subcategoriesChildmodelArray.size(); i++) {

                String subcategoryParentId2 = subcategoriesChildmodelArray.get(i).getParentId(); // categorie parant id
                if (subcategoryParentId2.equalsIgnoreCase(Headerid)) {
                    subcategoriesChildStringArray.add(subcategoriesChildmodelArray.get(i).getSubcategoryName());
                    if (subcategoriesChildmodelArray.get(i).getSubcategoryId().equalsIgnoreCase(ChildId)) {
                        subcatergoryName2 = subcategoriesChildmodelArray.get(i).getSubcategoryName();
                    }
                }
            }
        }
        if (subcatergoryName2 != null && subcatergoryName2.length() > 0) {

            subcategory2_close.setVisibility(View.VISIBLE);
            subCategoryHeaderLL.setVisibility(View.VISIBLE);
        } else {

            subcategory2_close.setVisibility(View.INVISIBLE);
        }
        subCategoriesChildAutoCompletedText.setText(subcatergoryName2);
    }

    public String getOften() {
        return sendOften;
    }

    private void onclick() {

        selected_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCityPopup(1);
            }
        });
        btnNWShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                showCityPopup(1);

            }
        });
/*
        venulistView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        */
        venulistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), VenueItem.class);
                SharedPreferences.Editor editor = sharedPref.edit();
               /* editor.putString("HdeaderName", custum_list.get(position).getName());
                editor.putString("ServiceName", custum_list.get(position).getName_service());
                editor.putString("Retting", custum_list.get(position).getOverallRating());*/

                editor.putString("venueID", custum_list.get(position).getBuisnessId());
                editor.putString("sector", custum_list.get(position).getSector());
              /*  editor.putString("lat", custum_list.get(position).getLat());
                editor.putString("long", custum_list.get(position).getLongId());
                editor.putString("ImageUrl", custum_list.get(position).getImageUrl());*/
                editor.commit();
                startActivity(intent);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.radioLowest:
                        // switch to fragment 1
                        keyForSharting = "1";
                        break;
                    case R.id.radioHighest:
                        // Fragment 2
                        keyForSharting = "2";
                        break;
                    case R.id.radioReview:
                        // Fragment 3
                        keyForSharting = "3";
                        break;
                    case R.id.radioDiscount:
                        // Fragment 3
                        keyForSharting = "4";

                        break;
                    case R.id.radioNearest:
                        // Fragment 3
                        keyForSharting = "5";
                        break;
                }
            }
        });


        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog1.dismiss();
                KalendriaAppController.hideSoftKeyboard(getActivity());
                if (keyForSharting.equalsIgnoreCase("5")) {
                    Collections.sort(custum_list, new Comparator<ServiceModal>() {
                        @Override
                        public int compare(ServiceModal o1, ServiceModal o2) {
                            return Double.compare(o1.distance, o2.distance);
                        }
                    });
                    adapter1.notifyDataSetChanged();
                } else
                    getFilterResponce();
            }
        });

        sortingCloseTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KalendriaAppController.hideSoftKeyboard(getActivity());
                myDialog1.dismiss();
            }
        });
        filterSearchBtn.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

                                                   selected_category.setText(typeAutoCompletedText.getText().toString());
                                                   KalendriaAppController.hideSoftKeyboard(getActivity());
                                                   myDialog.dismiss();
                                                   getFilterResponce();

                                               }
                                           }
        );

        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KalendriaAppController.hideSoftKeyboard(getActivity());
                myDialog1.show();
            }
        });

        filterClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KalendriaAppController.hideSoftKeyboard(getActivity());
                categoiresAuctoCompletedText.setText("");
                category_close.setVisibility(View.GONE);
                subCategoryHeaderLL.setVisibility(View.GONE);
                subCategoriyChildLL.setVisibility(View.GONE);
                filderGenderAutoCompletedText.setText("");
                filderKeyWordToSerachTxt.setText("");
                filderPriceTo.setText("");
                filderPriceFrom.setText("");
                filderDateTxt.setText("");
                filderTimeTxt.setText("");


                subCategoreisHeaderAutoCompletedText.setText("");
                subCategoriesChildAutoCompletedText.setText("");

                date_close.setVisibility(View.INVISIBLE);
                time_close.setVisibility(View.INVISIBLE);
                gender_close.setVisibility(View.INVISIBLE);
                KalendriaAppController.hideSoftKeyboard(getActivity());


                //subcategory2_close.setVisibility(View.INVISIBLE);

            }
        });

        filderKeyWordToSerachTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    if (filderKeyWordToSerachTxt.getText().toString().length() > 0) {
                        filter_keyword_close_txt.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        filderPriceFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    if (filderPriceFrom.getText().toString().length() > 0) {
                        price_close.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        filderPriceTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    if (filderPriceTo.getText().toString().length() > 0) {
                        price_to_close.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        filter_type_venue.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (selectedSector.equalsIgnoreCase("1")) {
                    ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, venueArray);

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Choose")
                            .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    filter_type_venue.setText(venueArray.get(which).toString());
                                    selectedVenueType = filter_type_venue.getText().toString();

                                    dialog.dismiss();
                                    changeRegion(sendCityID);
                                }
                            }).create().show();
                } else if (selectedSector.equalsIgnoreCase("4")) {
                    ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, petArray);

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Choose")
                            .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    filter_type_venue.setText(petArray.get(which).toString());
                                    selectedVenueType = filter_type_venue.getText().toString();
                                    changeRegion(sendCityID);
                                    dialog.dismiss();
                                }
                            }).create().show();
                }

                KalendriaAppController.hideSoftKeyboard(getActivity());

            }
        });

        typeAutoCompletedText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                KalendriaAppController.hideSoftKeyboard(getActivity());
                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, typeStringArray);

                new AlertDialog.Builder(getActivity())
                        .setTitle("All Categories ")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                DeshBoard objDeshboard= typeModelArray.get(which);

                                typeAutoCompletedText.setText(typeStringArray.get(which).toString());

                                if(!selectedTypeId.equalsIgnoreCase(objDeshboard.getTypeId()))
                                {
                                        categoreyLL.setVisibility(View.VISIBLE);
                                        subCategoryHeaderLL.setVisibility(View.GONE);
                                        subCategoriyChildLL.setVisibility(View.GONE);
                                    filter_location_home_and_garden.setText("");
                                        categoiresAuctoCompletedText.setText("");
                                        subCategoreisHeaderAutoCompletedText.setText("");
                                        subCategoriesChildAutoCompletedText.setText("");
                                        category_close.setVisibility(View.INVISIBLE);
                                }



                                selectedSector=objDeshboard.sector;
                                sendTypeIdToServer=objDeshboard.getTypeId();

                                updateFilterUI();


                                dialog.dismiss();
                            }
                        }).create().show();
                KalendriaAppController.hideSoftKeyboard(getActivity());

            }
        });

        categoiresAuctoCompletedText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                KalendriaAppController.hideSoftKeyboard(getActivity());
                for (int i = 0; i < typeModelArray.size(); i++) {
                    if (typeAutoCompletedText.getText().toString().equalsIgnoreCase(typeModelArray.get(i).getTypeName())) {
                        sendTypeIdToServer = typeModelArray.get(i).getTypeId();

                    }
                }
                System.out.println("type id" + sendTypeIdToServer);
                categoriesStringArray.clear();
                for (int i = 0; i < categoriesModelArray.size(); i++) {
                    String categoryParentId = categoriesModelArray.get(i).getCategoryImages(); // categorie parant id
                    if (categoryParentId.equalsIgnoreCase(sendTypeIdToServer)) {
                        categoriesStringArray.add(categoriesModelArray.get(i).getCategoryName());
                    }
                }


                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, categoriesStringArray);

                new AlertDialog.Builder(getActivity())
                        .setTitle("All Categories ")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                categoiresAuctoCompletedText.setText(categoriesStringArray.get(which).toString());
                                category_close.setVisibility(View.VISIBLE);

                                subCategoreisHeaderAutoCompletedText.setText("");
                                subCategoryHeaderLL.setVisibility(View.VISIBLE);
                                subcategory_close.setVisibility(View.INVISIBLE);

                                subCategoriesChildAutoCompletedText.setText("");
                                subcategory2_close.setVisibility(View.INVISIBLE);
                                subCategoriyChildLL.setVisibility(View.GONE);

                                dialog.dismiss();
                            }
                        }).create().show();
                KalendriaAppController.hideSoftKeyboard(getActivity());

            }
        });
        subCategoreisHeaderAutoCompletedText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                KalendriaAppController.hideSoftKeyboard(getActivity());
/*
                for(int i = 0; i< categoriesModelArray.size(); i++) {
                    if(categoiresAuctoCompletedText.getText().toString().equalsIgnoreCase(categoriesModelArray.get(i).getCategoryName())){
                        sentTo_categories_act= categoriesModelArray.get(i).getCategoryId();
                        subCategoriyChildLL.setVisibility(View.VISIBLE);
                        subcategory2_close.setVisibility(View.VISIBLE);

                    }
                }

                subcategoriesHeaderstring.clear();
                for(int i = 0; i< subcategoriesHeadermodel.size(); i++) {

                    String subcategoryParentId = subcategoriesHeadermodel.get(i).getSubcategoryHeaderParent(); // categorie parant id
                    if (subcategoryParentId.equalsIgnoreCase(sentTo_categories_act)) {
                        subcategoriesHeaderstring.add(subcategoriesHeadermodel.get(i).getSubcategoryHeaderName());

                    }
                }
                */
                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, subcategoriesHeaderstring);

                new AlertDialog.Builder(getActivity())
                        .setTitle("All Categories ")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                subCategoreisHeaderAutoCompletedText.setText(subcategoriesHeaderstring.get(which).toString());
                                subCategoriesChildAutoCompletedText.setText("");
                                subcategory_close.setVisibility(View.VISIBLE);
                                subcategory2_close.setVisibility(View.INVISIBLE);
                                subCategoriyChildLL.setVisibility(View.VISIBLE);

                                dialog.dismiss();
                            }
                        }).create().show();
                KalendriaAppController.hideSoftKeyboard(getActivity());
            }
        });

        subCategoriesChildAutoCompletedText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                KalendriaAppController.hideSoftKeyboard(getActivity());
                for (int i = 0; i < subcategoriesHeadermodel.size(); i++) {
                    if (subCategoreisHeaderAutoCompletedText.getText().toString().equalsIgnoreCase(subcategoriesHeadermodel.get(i).getSubcategoryHeaderName())) {
                        sendTo_subcategories = subcategoriesHeadermodel.get(i).getSubcategoryHeaderId();

                    }
                }

                subcategoriesChildStringArray.clear();

                for (int i = 0; i < subcategoriesChildmodelArray.size(); i++) {

                    String subcategoryParentId2 = subcategoriesChildmodelArray.get(i).getParentId(); // categorie parant id
                    if (subcategoryParentId2.equalsIgnoreCase(sendTo_subcategories)) {
                        subcategoriesChildStringArray.add(subcategoriesChildmodelArray.get(i).getSubcategoryName());

                    }
                }

                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, subcategoriesChildStringArray);

                new AlertDialog.Builder(getActivity())
                        .setTitle("All Categories ")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                subCategoriesChildAutoCompletedText.setText(subcategoriesChildStringArray.get(which).toString());

                                subcategory2_close.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        }).create().show();
                KalendriaAppController.hideSoftKeyboard(getActivity());
            }
        });


        how_often_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, oftenArray);
                new AlertDialog.Builder(getActivity())
                        .setTitle("Choose")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                how_often_txt.setText(oftenArray.get(which).toString());
                                sendOften = how_often_txt.getText().toString();
                                often_close.setVisibility(View.VISIBLE);

                                dialog.dismiss();
                            }
                        }).create().show();

                KalendriaAppController.hideSoftKeyboard(getActivity());

            }
        });

        filderGenderAutoCompletedText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, gender);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Choose")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                filderGenderAutoCompletedText.setText(gender.get(which).toString());
                                sendToServerGender = filderGenderAutoCompletedText.getText().toString();
                                gender_close.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        }).create().show();

                KalendriaAppController.hideSoftKeyboard(getActivity());

            }
        });

        filter_location_home_and_garden.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, regionTextArray);

                new AlertDialog.Builder(getActivity())
                        .setTitle("Choose")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                filter_location_home_and_garden.setText(regionTextArray.get(which).toString());
                                sendCommunity = filter_location_home_and_garden.getText().toString();
                                sendCommunityID = arrCommunity.get(which).getId();
                                community_close.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            }
                        }).create().show();

                KalendriaAppController.hideSoftKeyboard(getActivity());

            }
        });

        filter_location_city.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                showCityPopup(2);

            }
        });

        filter_keyword_close_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filderKeyWordToSerachTxt.setText("");
                filter_keyword_close_txt.setVisibility(View.INVISIBLE);
                KalendriaAppController.hideSoftKeyboard(getActivity());

            }
        });


        filter_close_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KalendriaAppController.hideSoftKeyboard(getActivity());
                myDialog.dismiss();
            }
        });
        category_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // KalendriaAppController.hideKeyboar(getActivity());

                categoiresAuctoCompletedText.setText("");
                subCategoriesChildAutoCompletedText.setText("");
                subCategoreisHeaderAutoCompletedText.setText("");

                category_close.setVisibility(View.INVISIBLE);
                subCategoryHeaderLL.setVisibility(View.GONE);
                subCategoriyChildLL.setVisibility(View.GONE);
                KalendriaAppController.hideSoftKeyboard(VenueFragment.this.getActivity());
            }
        });
        subcategory_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //KalendriaAppController.hideKeyboar(getActivity());
                subCategoreisHeaderAutoCompletedText.setText("");
                subCategoriesChildAutoCompletedText.setText("");
                subCategoriyChildLL.setVisibility(View.GONE);
                subcategory_close.setVisibility(View.INVISIBLE);
                KalendriaAppController.hideSoftKeyboard(VenueFragment.this.getActivity());
            }
        });
        subcategory2_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // KalendriaAppController.hideKeyboar(getActivity());
                subCategoriesChildAutoCompletedText.setText("");
                subcategory2_close.setVisibility(View.INVISIBLE);
                KalendriaAppController.hideSoftKeyboard(VenueFragment.this.getActivity());
            }
        });
        date_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filderDateTxt.setText("");
                date_close.setVisibility(View.INVISIBLE);
                KalendriaAppController.hideSoftKeyboard(VenueFragment.this.getActivity());
            }
        });
        time_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_close.setVisibility(View.INVISIBLE);
                filderTimeTxt.setText("");
                KalendriaAppController.hideSoftKeyboard(getActivity());

            }
        });
        often_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                how_often_txt.setText("");
                often_close.setVisibility(View.INVISIBLE);
                KalendriaAppController.hideSoftKeyboard(getActivity());
            }
        });
        community_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSector.equalsIgnoreCase("2"))
                    filter_location_home_and_garden.setText("Where you are located ?");
                else
                    filter_location_home_and_garden.setText("Community");
                community_close.setVisibility(View.INVISIBLE);
                KalendriaAppController.hideSoftKeyboard(getActivity());
            }
        });
        gender_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filderGenderAutoCompletedText.setText("");
                gender_close.setVisibility(View.INVISIBLE);
                KalendriaAppController.hideSoftKeyboard(getActivity());
            }
        });


        price_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filderPriceFrom.setText("");
                price_close.setVisibility(View.INVISIBLE);
                KalendriaAppController.hideSoftKeyboard(getActivity());
            }
        });
        price_to_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filderPriceTo.setText("");
                price_to_close.setVisibility(View.INVISIBLE);
                KalendriaAppController.hideSoftKeyboard(getActivity());
            }
        });


        filter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //========== Get the subcategorieschild Name start  ============//

                setFilterValues();

                //==== Filter Adapter start end
                myDialog.show();
                /*
                if (selectedSector.equalsIgnoreCase("2") || selectedSector.equalsIgnoreCase("4"))
                    getRegions(sendCityID, selectedSector, "Venu");
                else
                    getRegions(sendCityID, selectedSector, "");
                    */

                changeRegion(sendCityID);
            }
        });


        btnnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        filderDateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KalendriaAppController.hideSoftKeyboard(getActivity());
                toDatePickerDialog.show();
            }
        });

        filderTimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KalendriaAppController.hideSoftKeyboard(getActivity());
                Calendar currenttime = Calendar.getInstance();
                int hour = currenttime.get(Calendar.HOUR_OF_DAY);
                int minutes = currenttime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                // have to work
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        filderTimeTxt.setText(hourOfDay + ":" + minute);
                        time_close.setVisibility(View.VISIBLE);

                    }
                }, hour, minutes, true);

                timePickerDialog.show();
            }
        });
    }


    public void showCityPopup(final int type) {
        cityTextArray = CommonSingleton.getInstance().getCityList();
        final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, cityTextArray);

        new AlertDialog.Builder(getActivity())
                .setTitle("Choose")
                .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        ArrayList<RegisterSpinner> cityModel = CommonSingleton.getInstance().getCityMode();
                        filter_location_city.setText(cityTextArray.get(which).toString());
                        sendCity = filter_location_city.getText().toString();
                        sendCityID = cityModel.get(which).getId();

                        sendCommunityID = "";
                        sendCommunity = "";
                        community_close.setVisibility(View.INVISIBLE);
                        filter_location_home_and_garden.setText("");

                        if (type == 1)//Location change
                        {
                            selected_text.setText(sendCity);
                            setFilterValues();
                            getFilterResponce();
                        } else {
                            changeRegion(sendCityID);
                        }
                        dialog.dismiss();

                    }
                }).create().show();

        KalendriaAppController.hideSoftKeyboard(getActivity());
    }


    private void getDOB() {
        Calendar newCalendar = Calendar.getInstance();
        //newCalendar.add(Calendar.DATE, -1);
        //long a=System.currentTimeMillis()-1;
        //System.out.println("yesterdays date is:"+a);

        toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(year, monthOfYear, dayOfMonth - 1);
                String dayOfWeek = simpledateformat.format(date);
                System.out.println("day-->" + dayOfWeek);
                dayName = dayOfWeek;
                filderDateTxt.setText(dateFormatter.format(newDate.getTime()));
                date_close.setVisibility(View.VISIBLE);
            }

        }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        // toDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        toDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 12 * 60 * 60 * 1000);


    }


    private void getFiler() {

        try {

            String s = mjsonResonceSingletone.get(0);
            System.out.println("categories list--> " + s);
            if (s != null) {
                JSONObject jsonObject_category = new JSONObject(s);
                JSONArray jsonArray = jsonObject_category.getJSONArray("categorys");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String level = jsonObject.getString("level");

                    boolean isVisible3 = SafeParser.getBoolen(jsonObject, "isVisible", true);
                    boolean isSearch3 = SafeParser.getBoolen(jsonObject, "search", true);
                    if (isSearch3 == false) {
                        Log.d("Subcategory", jsonObject.getString("name") + " Breaking..");
                        continue;
                    }
                    if (level.equalsIgnoreCase("type")) {
                        DeshBoard deshBoard_filder = new DeshBoard();
                        deshBoard_filder.setTypeId(jsonObject.getString("id"));
                        deshBoard_filder.setTypeName(jsonObject.getString("name"));
                        deshBoard_filder.sector = SafeParser.getString(jsonObject, "sector", "0");
                        typeStringArray.add(jsonObject.getString("name"));
                        typeModelArray.add(deshBoard_filder);

                    } else if (level.equalsIgnoreCase("category")) {
                        Category categoryPage_model = new Category();
                        categoryPage_model.setCategoryId(jsonObject.getString("id"));
                        categoryPage_model.setCategoryName(jsonObject.getString("name"));
                        categoryPage_model.setCategoryImages(jsonObject.getString("parent"));
                        categoriesStringArray.add(jsonObject.getString("name"));
                        categoryPage_model.sector = SafeParser.getString(jsonObject, "sector", "0");
                        categoriesModelArray.add(categoryPage_model);

                    } else if (level.equalsIgnoreCase("sub_category 1")) {
                        SubCategoryHeader subcategoryHeader = new SubCategoryHeader();
                        subcategoryHeader.setSubcategoryHeaderId(jsonObject.getString("id"));
                        subcategoryHeader.setSubcategoryHeaderName(jsonObject.getString("name"));
                        subcategoryHeader.setSubcategoryHeaderParent(jsonObject.getString("parent"));
                        subcategoriesHeaderstring.add(jsonObject.getString("name"));
                        subcategoryHeader.sector = SafeParser.getString(jsonObject, "sector", "0");
                        subcategoriesHeadermodel.add(subcategoryHeader);

                    } else if (level.equalsIgnoreCase("sub_category 2")) {
                        SubCategoryChild subcategory = new SubCategoryChild();
                        subcategory.setSubcategoryId(jsonObject.getString("id"));
                        subcategory.setSubcategoryName(jsonObject.getString("name"));
                        subcategory.setParentId(jsonObject.getString("parent"));
                        subcategoriesChildStringArray.add(jsonObject.getString("name"));
                        subcategoriesChildmodelArray.add(subcategory);
                        subcategory.sector = SafeParser.getString(jsonObject, "sector", "0");
                        String ChildId = Constant.subCategeryChildId(getActivity());
                        if (subcategory.getSubcategoryId().equalsIgnoreCase(ChildId))
                            selected_category.setText(subcategory.getSubcategoryName());

                    }

                }
                setFilterValues();
                Log.d("Venue Frag:", "Type count:" + typeModelArray.size());

            } else {
                // if responce is null write your commants here
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    double math_PI = 3.14159265358979323846;
    double Earth_Radius = 3960.0;
    double adjratio = 0.70727;

    double deg2rad(double degrees) {
        return degrees * math_PI / 180.0;
    }

    double rad2deg(double radians) {
        return radians * 180.0 / math_PI;
    }


    double GetDistanceMeter(double Long1, double Lat1, double Long2, double Lat2) {
        double Distance = Math.acos(Math.sin(deg2rad(Lat1)) *
                Math.sin(deg2rad(Lat2)) +
                Math.cos(deg2rad(Lat1)) *
                        Math.cos(deg2rad(Lat2)) *
                        Math.cos(deg2rad(Long1 - Long2))) *
                6371393;

        return Distance;
    }

    double GetDistance(double Long1, double Lat1, double Long2, double Lat2) {
        double Distance = Math.acos(Math.sin(deg2rad(Lat1)) *
                Math.sin(deg2rad(Lat2)) +
                Math.cos(deg2rad(Lat1)) *
                        Math.cos(deg2rad(Lat2)) *
                        Math.cos(deg2rad(Long1 - Long2))) *
                3959;


        return Distance;
    }

    double lat2 = KalendriaAppController.getInstance().getLatitude();
    double long2 = KalendriaAppController.getInstance().getLongitude();

    private void makeJsonObjectRequest(String city) {


        if (!KalendriaAppController.getInstance().isNetworkConnected(getActivity())) {
            Toast.makeText(getActivity(), "Please Check your internet connection", Toast.LENGTH_LONG).show();
            return;
        }
        showpDialog();
        String TypeId = selectedTypeId;
        String categoryId = selectedCategoryId;
        String Headerid = selectedHeaderId;
        String HeaderName = Constant.subCategeryHeaderName(getActivity());
        String ChildId = selectedChildId;
        String sector = selectedSector;

        selected_category.setText(HeaderName);


        String param = "category=";
        if (categoryId != null && categoryId.length() > 0)
            param += categoryId;

        param += "&category_service=";
        if (ChildId != null && ChildId.length() > 0)
            param += ChildId;

        param += "&category_type=";
        if (TypeId != null && TypeId.length() > 0)
            param += TypeId;

        param += "&day=";
        param += "&limit=20";

        if (sector != null && !sector.equalsIgnoreCase("null") && sector.length() > 0)
            param += "&sector=" + sector;
        else param += "&sector=1";

        param += "&skip=0";
        param += "&sub_category=" + Headerid;
        param += "&city=3";
        param += "&date=";
        param += "&keyword=";
        param += "&max_price=";
        param += "&min_price=";
        param += "&region=";
        param += "&time=";
        param += "&audience=";
        param += "&how_often=";
        param += "&user_location=";


        String url = Constant.HOST + "api/v1/search/search?" + param;
        Log.d(TAG, "--->" + url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "--->" + response);
                try {


                    //custum_list.clear();
                    ArrayList tempArray = new ArrayList();
                    JSONArray jsonArray = new JSONArray(response);
                    //System.out.println("murugankldjkdkd"+jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ServiceModal selectedModel = new ServiceModal();
                        selectedModel.setBuisnessId(jsonObject.getString("id"));
                        selectedModel.setBuisnessName(jsonObject.getString("name"));
                        selectedModel.setCity(jsonObject.getString("city"));
                        selectedModel.setRegion(jsonObject.getString("region"));
                        selectedModel.setDescription(jsonObject.getString("description"));
                        selectedModel.setLat(jsonObject.getString("lat"));
                        selectedModel.setLongId(jsonObject.getString("long"));
                        //selectedModel.setLongId(jsonObject.getString("long"));
                        selectedModel.setSector(SafeParser.getString(jsonObject, "sector", "1"));
                        selectedModel.setOverallRating(jsonObject.getString("overall_rating"));

                        double lat1 = SafeParser.getDouble(jsonObject, "lat", 0);
                        double long1 = SafeParser.getDouble(jsonObject, "long", 0);
                        selectedModel.distance = GetDistance(long1, lat1, long2, lat2);


                        String nameValue = jsonObject.getString("media");
                        if (nameValue != "null") {
                            System.out.println("i am in side");
                            JSONObject jsonObject1 = new JSONObject(nameValue);
                            selectedModel.setImageUrl(jsonObject1.getString("url"));
                            selectedModel.setImageUrlThumb(jsonObject1.getString("thumb"));

                        }

                        String service = jsonObject.getString("services");
                        if (service != "null") {


                            JSONArray jsonArray1 = new JSONArray(service);
                            JSONObject object = jsonArray1.getJSONObject(0);

                            selectedModel.setBuisnessId(object.getString("business"));
                            selectedModel.setServiceId(object.getString("id"));
                            selectedModel.setServiceName(object.getString("name"));
                            selectedModel.setPrice(object.getString("price"));
                            selectedModel.setDiscount(object.getString("discount"));
                            selectedModel.setDiscounted_price(object.getString("discounted_price"));
                            selectedModel.setDuration(object.getString("duration"));


                        }
                        tempArray.add(selectedModel);

                    }

                    custum_list.clear();
                    custum_list.addAll(tempArray);

                    showStatusLabel(custum_list.size() == 0);
                    adapter1.sector = selectedSector;
                    adapter1.notifyDataSetChanged();


                } catch (JSONException e) {
                    System.out.println(TAG + e.getMessage());
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.d(TAG, "Error : " + error.getMessage());
                Toast.makeText(getActivity(), KalendriaAppController.getErrorMessage(), Toast.LENGTH_SHORT).show();
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

    private void getFilterResponce() {


        myDialog1.dismiss();

        if (!KalendriaAppController.getInstance().isNetworkConnected(getActivity())) {
            Toast.makeText(getActivity(), "Please Check your internet connection", Toast.LENGTH_LONG).show();
            return;
        }

        showpDialog();

        sendCommunity = "";
        sendCommunityID = "";


        sentTosubcategories2 = "";
        sentTo_categories_act = "";
        sendTypeIdToServer = "";
        sendTo_subcategories = "";

        for (int i = 0; i < typeModelArray.size(); i++) {
            if (typeAutoCompletedText.getText().toString().equalsIgnoreCase(typeModelArray.get(i).getTypeName())) {
                sendTypeIdToServer = typeModelArray.get(i).getTypeId();

            }
        }

        if (categoiresAuctoCompletedText.getText().toString().length() > 0) {
            for (int i = 0; i < categoriesModelArray.size(); i++) {
                if (categoiresAuctoCompletedText.getText().toString().equalsIgnoreCase(categoriesModelArray.get(i).getCategoryName())) {
                    sentTo_categories_act = categoriesModelArray.get(i).getCategoryId();

                }
            }
        }


        if (subCategoreisHeaderAutoCompletedText.getVisibility() == View.VISIBLE && subCategoreisHeaderAutoCompletedText.getText().toString().length() > 0) {
            for (int i = 0; i < subcategoriesHeadermodel.size(); i++) {
                if (subCategoreisHeaderAutoCompletedText.getText().toString().equalsIgnoreCase(subcategoriesHeadermodel.get(i).getSubcategoryHeaderName())) {
                    sendTo_subcategories = subcategoriesHeadermodel.get(i).getSubcategoryHeaderId();

                }
            }
        }


        if (subCategoriesChildAutoCompletedText.getVisibility() == View.VISIBLE && subCategoriesChildAutoCompletedText.getText().toString().length() > 0) {
            for (int i = 0; i < subcategoriesChildmodelArray.size(); i++) {
                if (subCategoriesChildAutoCompletedText.getText().toString().equalsIgnoreCase(subcategoriesChildmodelArray.get(i).getSubcategoryName())) {
                    sentTosubcategories2 = subcategoriesChildmodelArray.get(i).getSubcategoryId();

                }
            }
        }

        sendOften = how_often_txt.getText().toString();

        //StringBuilder s = new StringBuilder();
        //s.append("something");

/*
 categoiresAuctoCompletedText.setText("");
                category_close.setVisibility(View.GONE);
                subCategoryHeaderLL.setVisibility(View.GONE);
                subCategoriyChildLL.setVisibility(View.GONE);
                filderGenderAutoCompletedText.setText("");
                filderKeyWordToSerachTxt.setText("");
                filderPriceTo.setText("");
                filderPriceFrom.setText("");
                filderDateTxt.setText("");
                filderTimeTxt.setText("");
                date_close.setVisibility(View.INVISIBLE);
                time_close.setVisibility(View.INVISIBLE);
 */
        String keyword = filderKeyWordToSerachTxt.getText().toString();
        String filter_price_from1 = filderPriceFrom.getText().toString();
        String filter_price_to1 = filderPriceTo.getText().toString();
        String filter_date1 = filderDateTxt.getText().toString();
        String filter_time1 = filderTimeTxt.getText().toString();

        selectedCategoryId = sentTo_categories_act;
        selectedTypeId = sendTypeIdToServer;
        selectedChildId = sentTosubcategories2;
        selectedHeaderId = sendTo_subcategories;

        /*
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("type_id", sendTypeIdToServer);
        //editor.putString("category_id", sentTo_categories_act);
        editor.putString("HeaderId", sendTo_subcategories);
        editor.putString("ChildId", sentTosubcategories2);
        //editor.putString("venueID", "");
        editor.commit();
        */

        //String sendToServerGender = filderGenderAutoCompletedText.getText().toString();

        if (keyword == null) {
            keyword = "";
        }
        if (filter_price_from1 == null) {
            filter_price_from1 = "";
        }
        if (filter_price_to1 == null) {
            filter_price_to1 = "";
        }
        if (filter_date1 == null) {
            filter_date1 = "";
        }
        if (filter_time1 == null) {
            filter_time1 = "";
        }
        String filter_weekday = "";

        String param = "category=";
        if (sentTo_categories_act != null && sentTo_categories_act.length() > 0)
            param += sentTo_categories_act;

        param += "&category_service=";
        if (sentTosubcategories2 != null && sentTosubcategories2.length() > 0)
            param += sentTosubcategories2;

        param += "&category_type=";
        if (sendTypeIdToServer != null && sendTypeIdToServer.length() > 0)
            param += sendTypeIdToServer;


        if (filter_weekday != null && !filter_weekday.equalsIgnoreCase("null"))
            param += "&day=" + filter_weekday;
        else param += "&day=";

        param += "&limit=20";
        String sector = selectedSector;
        if (sector != null && !sector.equalsIgnoreCase("null") && sector.length() > 0)
            param += "&sector=" + sector;
        else param += "&sector=1";

        param += "&skip=0";
        param += "&sub_category=" + sendTo_subcategories;


        param += "&city=";
        if (sendCityID != null && !sendCityID.equalsIgnoreCase("null"))
            param += sendCityID;

        if (filter_date1 != null && !filter_date1.equalsIgnoreCase("null"))
            param += "&date=" + filter_date1;
        else
            param += "&date=";

        if (keyword != null && !keyword.equalsIgnoreCase("null"))
            param += "&keyword=" + keyword.replaceAll(" ", "+");
        else
            param += "&keyword=";

        if (filter_price_to1 != null) {
            try {
                int Max = Integer.parseInt(filter_price_to1);
                param += "&max_price=" + Max;
            } catch (Exception ex) {
                param += "&max_price=";

            }
        } else
            param += "&max_price=";

        if (filter_price_from1 != null) {
            try {
                int Max = Integer.parseInt(filter_price_from1);
                param += "&min_price=" + Max;
            } catch (Exception ex) {
                param += "&min_price=";

            }
        } else
            param += "&min_price=";


        param += "&time=";
        if (filter_time1 != null && !filter_time1.equalsIgnoreCase("null"))
            param += filter_time1;


        param += "&audience=";
        if (sendToServerGender != null && !sendToServerGender.equalsIgnoreCase("null"))
            param += sendToServerGender;
        else


            param += "&how_often=";
        if (sendOften != null && !sendOften.equalsIgnoreCase("null"))
            param += URLEncoder.encode(sendOften);


        param += "&pet_attribute=";
        if (selectedSector.equalsIgnoreCase("4"))
            param += selectedVenueType;

        param += "&beauty_attribute=";
        if (selectedSector.equalsIgnoreCase("1"))
            param += selectedVenueType;


        String uloc = "";
        String reg = "";
        if (sendCommunityID != null && !sendCommunityID.equalsIgnoreCase("null")) {
            //   loc = sendCommunityID;
            if (selectedSector == "2") {
                uloc = sendCommunityID;

            } else {
                reg = sendCommunityID;

            }
        }

        param += "&user_location=" + uloc;
        param += "&region=" + reg;


        param = param.replaceAll("null", "");


        if (keyForSharting != null && keyForSharting.length() > 0) {

            String sortingURL = "";
            if (keyForSharting.equalsIgnoreCase("1")) {
                sortingURL = "&sort=discounted_price+ASC";

            } else if (keyForSharting.equalsIgnoreCase("2")) {
                sortingURL = "&sort=discounted_price+DESC";

            } else if (keyForSharting.equalsIgnoreCase("3")) {
                sortingURL = "&sort=overall_rating+DESC";


            } else if (keyForSharting.equalsIgnoreCase("4")) {

                sortingURL = "&sort=discount+DESC";
            } else if (keyForSharting.equalsIgnoreCase("5")) {
                sortingURL = "&sort=";

            }
            param += sortingURL;
        }


        //String va= https://dev.api.kalendria.com/api/v1/search/search?audience=Male&category=2&category_service=36&category_type=1&city=3&limit=10&sector=1&skip=0
        //String va="audience="+sendToServerGender+"&category="+sentTo_categories_act+"&category_service="+sentTosubcategories2+
        // "&category_type="+sendTypeIdToServer+"&city=3&date="+filter_date1+"+00:00:00.0000&day="+dayName+"&keyword="+keyword+"&limit=10
        // &max_price="+filter_price_from1+"&min_price="+filter_price_to1+"&sector=1&skip=0&sub_category="+sendTo_subcategories+"&time="+filter_time1;


        String url = Constant.VENUE_FILTER + param;
        Log.d(TAG, "--->" + url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "--->" + response);
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    //System.out.println("murugankldjkdkd"+jsonArray);
                    ArrayList tempArray = new ArrayList();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        ServiceModal selectedModel = new ServiceModal();
                        selectedModel.setBuisnessId(jsonObject.getString("id"));
                        selectedModel.setBuisnessName(jsonObject.getString("name"));
                        selectedModel.setCity(jsonObject.getString("city"));
                        selectedModel.setRegion(jsonObject.getString("region"));
                        selectedModel.setDescription(jsonObject.getString("description"));
                        selectedModel.setLat(jsonObject.getString("lat"));
                        selectedModel.setLongId(jsonObject.getString("long"));
                        selectedModel.setOverallRating(jsonObject.getString("overall_rating"));

                        double lat1 = SafeParser.getDouble(jsonObject, "lat", 0);
                        double long1 = SafeParser.getDouble(jsonObject, "long", 0);
                        selectedModel.distance = GetDistance(long1, lat1, long2, lat2);


                        String nameValue = jsonObject.getString("media");
                        if (nameValue != "null") {
                            System.out.println("i am in side");
                            JSONObject jsonObject1 = new JSONObject(nameValue);
                            selectedModel.setImageUrl(jsonObject1.getString("url"));

                        }

                        String service = jsonObject.getString("services");
                        if (service != "null") {
                            JSONArray jsonArray1 = new JSONArray(service);

                            JSONObject object = jsonArray1.getJSONObject(0);
                            selectedModel.setBuisnessId(object.getString("business"));
                            selectedModel.setServiceName(object.getString("name"));
                            selectedModel.setServiceId(object.getString("id"));
                            selectedModel.setPrice(object.getString("price"));
                            selectedModel.setDiscount(object.getString("discount"));
                            selectedModel.setDiscounted_price(object.getString("discounted_price"));
                            selectedModel.setDuration(object.getString("duration"));

                        }
                        tempArray.add(selectedModel);

                    }

                    //adapter1 = new VenueAdapter(custum_list,getActivity());
                    custum_list.clear();
                    custum_list.addAll(tempArray);
                    showStatusLabel(custum_list.size() == 0);
                    adapter1.sector = selectedSector;
                    adapter1.notifyDataSetChanged();
                    //list.setAdapter(adapter1);

                } catch (JSONException e) {
                    System.out.println(TAG + e.getMessage());
                }

                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.d(TAG, "Error : " + error.getMessage());
                Toast.makeText(getActivity(), KalendriaAppController.getErrorMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
