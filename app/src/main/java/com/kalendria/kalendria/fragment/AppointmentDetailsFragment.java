package com.kalendria.kalendria.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.CheckOut;
import com.kalendria.kalendria.adapter.AppointmentCancelServiceListAdapter;
import com.kalendria.kalendria.adapter.AppointmentOderDetailsAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AppointmentDetailModel;
import com.kalendria.kalendria.model.AppointmentOderList;
import com.kalendria.kalendria.model.DeshBoard;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;


/**
 * Created by murugan on 1/06/2016.
 */
public class AppointmentDetailsFragment extends Fragment {

    // geting the mapview start ==//
    double latitude, longitude;
    GoogleMap mMap; // Might be null if Google Play services APK is not
    GoogleMap map;
    // available.
    Marker marker;
    Hashtable<String, String> markers;
    SupportMapFragment fragment;

    Button appointment_back_btn;
    /*add the addres field here */

    private ProgressDialog pDialog;
    ImageView thumbnail;
    TextView oder_buisiness_name, order_city_region, oder_name_sub, oder_address, oder_phone, oder_email, order_website, oder_parking;
    TextView order_venu, order_checkout, cancel_textview;
    TextView order_status, order_total, payment_type, payment_status;
    TextView venue_message;
    TextView textvenu, txtcheckout, txthow_often,cancel_textView_label;
    ArrayList<AppointmentDetailModel> custum_list = new ArrayList<AppointmentDetailModel>();
    ArrayList<JSONObject> arrServiceParam= new ArrayList<>();

    AppointmentOderDetailsAdapter adapter1;
    AppointmentCancelServiceListAdapter cancelListAdapter;
    ListView appointment_list,serviceListView;
    LinearLayout cancelLaout;

    TextView textpromo, textgift;
    TableRow tableRowPromo, tableRowGift, tableRowCheckout, tableRowVenu;
    TableLayout tableLayoutOften;

    private AlertDialog myDialog;
    private View alertView;
    Button cross_image,cancelOrderButton;
    RadioButton radio1,radio2,radio3,radio4,radio5;



    boolean isLoyalty, isIroning, isCancelled, isCurrent;
    String howOftenVal;
    int sector;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.appointment_details_page, container, false);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        thumbnail = (ImageView) rootView.findViewById(R.id.thumbnail);
        oder_buisiness_name = (TextView) rootView.findViewById(R.id.oder_buisiness_name);
        order_city_region = (TextView) rootView.findViewById(R.id.order_city_region);
        oder_name_sub = (TextView) rootView.findViewById(R.id.oder_name_sub);
        oder_address = (TextView) rootView.findViewById(R.id.oder_address);
        oder_phone = (TextView) rootView.findViewById(R.id.oder_phone);
        oder_email = (TextView) rootView.findViewById(R.id.oder_email);
        order_website = (TextView) rootView.findViewById(R.id.order_website);
        oder_parking = (TextView) rootView.findViewById(R.id.oder_parking);
        venue_message = (TextView) rootView.findViewById(R.id.venue_message);
        cancelLaout = (LinearLayout) rootView.findViewById(R.id.cancelLayout);

        order_venu = (TextView) rootView.findViewById(R.id.textvenu);
        cancel_textview = (TextView) rootView.findViewById(R.id.cancel_textView);

        order_total = (TextView) rootView.findViewById(R.id.order_total);
        order_status = (TextView) rootView.findViewById(R.id.order_status);
        payment_status = (TextView) rootView.findViewById(R.id.payment_status);
        payment_type = (TextView) rootView.findViewById(R.id.payment_type);
        order_checkout = (TextView) rootView.findViewById(R.id.txtcheckout);

        textvenu = (TextView) rootView.findViewById(R.id.textvenu);
        txtcheckout = (TextView) rootView.findViewById(R.id.txtcheckout);
        cancel_textView_label = (TextView) rootView.findViewById(R.id.cancel_textView_label);

        tableRowPromo = (TableRow) rootView.findViewById(R.id.tableRowPromo);
        tableRowGift = (TableRow) rootView.findViewById(R.id.tableRowGift);
        tableRowCheckout = (TableRow) rootView.findViewById(R.id.tableRow2);
        tableRowVenu = (TableRow) rootView.findViewById(R.id.tableRow1);
        tableLayoutOften = (TableLayout) rootView.findViewById(R.id.tableLayoutOften);

        textgift = (TextView) rootView.findViewById(R.id.textgift);
        textpromo = (TextView) rootView.findViewById(R.id.textpromo);
        txthow_often = (TextView) rootView.findViewById(R.id.how_often);

        appointment_list = (ListView) rootView.findViewById(R.id.appointment_list);
        appointment_back_btn = (Button) rootView.findViewById(R.id.appointment_back_btn);

        markers = new Hashtable<String, String>();
        setUpMapIfNeeded();

        adapter1 = new AppointmentOderDetailsAdapter(getActivity(), custum_list);
        appointment_list.setAdapter(adapter1);

        if (mMap != null) //coded by Magesh to avoid unwanted crash
            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        if (KalendriaAppController.isNetworkConnected(getActivity())) {
            getOderServiceDetails(true);
        } else {
            Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
        }

        cancel_textView_label.setText(Html.fromHtml("Plan Changed? <font color=\"#0097db\">Cancel</font> your order" ));
        initDialog();
        appointment_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        isCurrent = Constant.getisCurrent(getActivity());

        System.out.println("buisinessId" + Constant.getMyOderBusinessId(getActivity()));
        System.out.println("oderId" + Constant.getMyOderID(getActivity()));
        return rootView;
    }


    ArrayList<String> arrCancelService = new ArrayList<>();
    public void initDialog() {

	/*set the tag for book now button */

        Context context= getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflateralert = LayoutInflater.from(context);
        alertView = inflateralert.inflate(R.layout.appointment_cancel_popup, null);
        builder.setView(alertView);


        cross_image = (Button) alertView.findViewById(R.id.cancel_close_button);
        cancelOrderButton= (Button) alertView.findViewById(R.id.cancelOrderButton);
        radio1 =(RadioButton)alertView.findViewById(R.id.radio1);
        radio2 =(RadioButton)alertView.findViewById(R.id.radio2);
        radio3 =(RadioButton)alertView.findViewById(R.id.radio3);
        radio4 =(RadioButton)alertView.findViewById(R.id.radio4);
        serviceListView=(ListView)alertView.findViewById(R.id.serviceListView);
        cancelListAdapter = new AppointmentCancelServiceListAdapter(getActivity(),custum_list);
        serviceListView.setAdapter(cancelListAdapter);
        //radio5 =(RadioButton)alertView.findViewById(R.id.radio5);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.GRAY)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getActivity())
                .load(R.drawable.bg)
                .placeholder(R.drawable.bg)
                .noFade()
                .transform(transformation)
                .fit()
                .skipMemoryCache()
                .into(thumbnail);

        myDialog = builder.create();
        builder.setCancelable(true);

        cancelOrderButton.setBackgroundColor(KalendriaAppController.getResColor(R.color.colorGrey));
        cross_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

            }
        });

        cancelOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

                String reason ="";
                if(radio1.isChecked())
                    reason=getActivity().getResources().getString(R.string.reason1);
                else  if(radio2.isChecked())
                    reason=getActivity().getResources().getString(R.string.reason2);
                else  if(radio3.isChecked())
                    reason=getActivity().getResources().getString(R.string.reason3);
                else  if(radio4.isChecked())
                    reason=getActivity().getResources().getString(R.string.reason4);

                doCancelOrder(reason,arrCancelService);


            }
        });

        cancel_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.show();
                cancelListAdapter.notifyDataSetChanged();

            }
        });

        cancelListAdapter.delegate= new AppointmentCancelServiceListAdapter.AppointmentCancelServiceListDelegate() {
            @Override
            public void appointmentRadioButtonTapped(int position) {

                boolean isEnabled=false;
                for(int i=0;i<custum_list.size();i++)
                {
                    AppointmentDetailModel model = custum_list.get(i);
                    if(i==position)
                    {

                        model.isSelected=! model.isSelected;

                    }


                    if(model.isSelected) {
                        isEnabled = true;
                        arrCancelService.add(model.serviceName);
                    }
                    else
                    {
                        arrCancelService.remove(model.serviceName);
                    }

                }

                cancelOrderButton.setEnabled(isEnabled);
                if(isEnabled) {

                    cancelOrderButton.setBackgroundColor(KalendriaAppController.getResColor(R.color.colorSkyBlue));
                }
                else
                {
                    cancelOrderButton.setBackgroundColor(KalendriaAppController.getResColor(R.color.colorGrey));
                }
                cancelListAdapter.notifyDataSetChanged();

            }
        };




    }
    private void setUpMapIfNeeded() {
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_appointment_details);

        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_appointment_details, fragment).commit();
        }

        if (map == null) {
            mMap = fragment.getMap();
            //map = fragment.getMap();
            // mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
            if (mMap != null) {
                //zooming method will be here
            }
        }
    }

    public LatLng getLatLng() {
       /* latitude = Double.parseDouble(Constant.getVenueLat(getActivity()));
        longitude = Double.parseDouble(Constant.getVenulont(getActivity()));*/

        if (this.latitude == 0 || this.longitude == 0) {
            return null;
        }
        return new LatLng(this.latitude, this.longitude);
    }

    private void setmMap() {

       /* latitude = Double.parseDouble(Constant.getVenueLat(getActivity()));
        longitude = Double.parseDouble(Constant.getVenulont(getActivity()));*/

        String title = Constant.getVenueName(getActivity());
        // String address = (regienName + cityName +"\n"+ mobile);

       /* Log.i("#######address","&*****"+address);
        Log.i("#######title","&*****"+title);*/


        final Marker kiel = mMap.addMarker(new MarkerOptions()
                .position(getLatLng())
                .title(title)
                .snippet(oder_address.getText().toString())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        markers.put(kiel.getId(), Constant.getVenuSelecedImageUrl(getActivity()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLng()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLatLng(), 10));


    }


    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (AppointmentDetailsFragment.this.marker != null
                    && AppointmentDetailsFragment.this.marker.isInfoWindowShown()) {
                AppointmentDetailsFragment.this.marker.hideInfoWindow();
                AppointmentDetailsFragment.this.marker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            AppointmentDetailsFragment.this.marker = marker;
            String url = null;

            if (marker.getId() != null && markers != null && markers.size() > 0) {
                if (markers.get(marker.getId()) != null
                        && markers.get(marker.getId()) != null) {
                    url = markers.get(marker.getId());
                }
            }
            final ImageView image = ((ImageView) view.findViewById(R.id.badge));

            if (url != null && !url.equalsIgnoreCase("null")
                    && !url.equalsIgnoreCase("")) {

                Picasso.with(getActivity()).load(url).into(image);
                getInfoContents(marker);

            } else {
                image.setImageResource(R.drawable.ic_cast_disabled_light);
            }

            final String title = marker.getTitle();
            final TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                titleUi.setText(title);
            } else {
                titleUi.setText("");
            }

            final String snippet = marker.getSnippet();
            final TextView snippetUi = ((TextView) view
                    .findViewById(R.id.snippet));
            if (snippet != null) {
                snippetUi.setText(snippet);
            } else {
                snippetUi.setText("");
            }

            return view;
        }
    }


    private void getOderServiceDetails(final boolean isVenueInfo) {
        showpDialog();
        String url = Constant.HOST + "api/v1/booking?order=" + Constant.getMyOderID(getActivity()) + "&populate=service,employee";
        JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Tag", response.toString());
                hidepDialog();
                getGiftInfo();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ssss");
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE, d MMM, yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


                ArrayList temp = new ArrayList();
                int cancelCount = 0;
                try {

                    if (response.length() > 0) {

                        arrServiceParam.clear();
                        for (int i = 0; i < response.length(); i++) {

                            JSONObject object = response.getJSONObject(i);
                            AppointmentOderList appointmentOderList = new AppointmentOderList();

                            appointmentOderList.setOderId(object.getString("id"));
                            appointmentOderList.setServiceName(object.getString("service_name"));
                            appointmentOderList.setOrderStatus(object.getString("status"));
                            String MyDate1 = "";
                            Date MyDate = null;

                            try {
                                String resultFinal = null;
                                try {
                                    String s1 = object.getString("scheduledAt");
                                    String s = s1.replace("-", "/");
                                    String start1 = StringUtils.substringBefore(s, " "); // returns "abc"

                                    String year = null;
                                    String month = null;
                                    String date = null;
                                    try {
                                        String[] parts = start1.split("/"); // escape .
                                        year = parts[0];
                                        month = parts[1];
                                        date = parts[2];
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String result = date + "/" + month + "/" + year;
                                    resultFinal = result.replace("\"", "");

                                    //String resultFinal=result.substring(1,result.indexOf("")-1);
                                    System.out.println("size of array" + resultFinal);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                MyDate = newDateFormat.parse(resultFinal);
                                newDateFormat.applyPattern("EEEE d MMM yyyy");
                                MyDate1 = newDateFormat.format(MyDate);
                                System.out.println("day-->" + MyDate1);
                                appointmentOderList.setDay(MyDate1.toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }




                            appointmentOderList.setStarTime(object.getString("time"));
                            String serviceStartTime = object.getString("time");


                            JSONObject jsonObject = object.getJSONObject("employee");
                            appointmentOderList.setStaffName(jsonObject.getString("first_name"));

                            JSONObject service = object.getJSONObject("service");
                            String serviceEndTime = service.getString("duration");
                            //String price=service.getString("price");


                            JSONObject dictServiceInfo = service;
                            JSONObject dictOrderService = object;
                            arrServiceParam.add(dictOrderService);
                            String customer_email = SafeParser.getString(dictOrderService, "customer_email", "");
                            String customer_name = SafeParser.getString(dictOrderService, "customer_name", "");

                            String customer_phone = SafeParser.getString(dictOrderService, "customer_phone", "");

                            int discount = SafeParser.getInt(dictOrderService, "discount", 0);
                            int price = SafeParser.getInt(dictOrderService, "price", 0);


                            int business = SafeParser.getInt(dictOrderService, "business", 0);

                            String sheduleAt = SafeParser.getString(dictOrderService, "scheduledAt", "");
                            String endAt = SafeParser.getString(dictOrderService, "endAt", "");


                            String startTime = null;
                            String endTime = null;


                            String sheduleDay = null;


                            Date sh_date1 = null, end_date = null;
                            try {
                                sh_date1 = dateFormat.parse(sheduleAt);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                end_date = dateFormat.parse(endAt);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            if (sh_date1 == null)
                                sh_date1 = MyDate;

                            try {
                                if (sh_date1 != null) {
                                    sheduleDay = dayFormat.format(sh_date1);
                                    startTime = timeFormat.format(sh_date1);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            try {
                                if (end_date != null) {

                                    endTime = timeFormat.format(end_date);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                            String timing = startTime + " - " + endTime;

                            JSONObject dictEmpoyee = SafeParser.getObject(dictOrderService, "employee");
                            JSONObject dictEmpProfile = SafeParser.getObject(dictEmpoyee, "profile_image");


                            String imgUrl = "";
                            String imgMedium = "";
                            String imgThumb = "";

                            if (dictEmpProfile != null) {
                                imgUrl = SafeParser.getString(dictEmpProfile, "url", "");
                                imgMedium = SafeParser.getString(dictEmpProfile, "medium", "");
                                imgThumb = SafeParser.getString(dictEmpProfile, "thumb", "");

                            }


                            String empFirstName = SafeParser.getString(dictEmpoyee, "first_name", "");
                            int empId = SafeParser.getInt(dictEmpoyee, "id", 0);
                            String empLastName = SafeParser.getString(dictEmpoyee, "last_name", "");


                            String serviceName = SafeParser.getString(dictServiceInfo, "name", "");

                            int serviceId = SafeParser.getInt(dictServiceInfo, "id", 0);
                            String businessName = SafeParser.getString(dictServiceInfo, "business_name", "");


                            int serviceCity = SafeParser.getInt(dictServiceInfo, "city", 0);
                            int serviceRegion = SafeParser.getInt(dictServiceInfo, "region", 0);

                            String status = SafeParser.getString(dictOrderService, "status", "");
                            String time = SafeParser.getString(dictOrderService, "time", "");

                            String payAt = SafeParser.getString(dictOrderService, "pay_at", "");

                            if (payAt.equalsIgnoreCase("points")) {
                                isLoyalty = true;
                            } else {
                                isLoyalty = false;
                            }

                            int voucher_used_value = SafeParser.getInt(dictOrderService, "voucher_used_value", 0);
                            int gift_used_value = SafeParser.getInt(dictOrderService, "gift_used_value", 0);
                            int amount_paid = SafeParser.getInt(dictOrderService, "amount_paid", 0);

                            boolean isPaid = SafeParser.getBoolen(dictOrderService, "is_paid", false);

                            int points = SafeParser.getInt(dictOrderService, "points", 0);

                            boolean enable_points = SafeParser.getBoolen(dictServiceInfo, "enable_loyalty_points", false);


                            isIroning = SafeParser.getBoolen(dictOrderService, "is_iron", false);
                            howOftenVal = SafeParser.getString(dictOrderService, "how_often", "");
                            txthow_often.setText(howOftenVal);
                            sector = SafeParser.getInt(dictOrderService, "sector", 0);

                            if (sector == 2) {
                                txthow_often.setText(howOftenVal);
                                tableLayoutOften.setVisibility(View.VISIBLE);
                            } else
                                tableLayoutOften.setVisibility(View.GONE);
                            /*
                            String isIroning = "";

                                if(isIron == true){
                                    isIroning = "Yes";
                                }else{
                                    isIroning = "No";
                                }
                                */

                            isCancelled = false;

                            if (status.equalsIgnoreCase("Cancelled")) {

                                isCancelled = true;

                            }

                            if (status.equalsIgnoreCase("Cancelled")) {
                                cancelCount++;
                            }


                            AppointmentDetailModel dictService = new AppointmentDetailModel();
                            dictService.business = business;
                            dictService.customer_email = customer_email;
                            dictService.customer_name = customer_name;
                            dictService.customer_phone = customer_phone;
                            dictService.discount = discount;
                            dictService.price = price;
                            dictService.EmpImgUrl = imgUrl;
                            dictService.EmpImgMedium = imgMedium;
                            dictService.EmpImgThumb = imgThumb;
                            dictService.empFirstName = empFirstName;
                            dictService.empId = empId;
                            dictService.empLastName = empLastName;
                            dictService.serviceName = serviceName;
                            dictService.serviceId = serviceId;
                            dictService.intServiceCity = serviceCity;
                            dictService.intServiceRegion = serviceRegion;

                            dictService.isSelected = false;
                            dictService.businessName = businessName;
                            dictService.status = status;
                            dictService.time = time;
                            dictService.sheduleAt = sheduleAt;
                            dictService.endAt = endAt;

                            dictService.pay_at = payAt;
                            dictService.voucher_used_value = voucher_used_value;
                            dictService.gift_used_value = gift_used_value;
                            dictService.amount_paid = amount_paid;
                            dictService.isPaid = isPaid;

                            dictService.enable_points = enable_points;

                            dictService.timing = timing;

                            if (sh_date1 != null)
                                dictService.shedule_date = sh_date1.getTime();
                            else
                                dictService.shedule_date = 0;
                            dictService.sheduleDay = sheduleDay;

                            dictService.points = points;

                            temp.add(dictService);


/*
                            System.out.println("start and end time"+serviceStartTime+serviceEndTime);
                            try {
                                String s = serviceStartTime.replaceAll(":",".");
                                String e = serviceEndTime.replaceAll(":",".");
                                double start = Double.parseDouble(s);
                                double end = Double.parseDouble(e);
                                double totalTime = start+end;

                                System.out.println("dourition"+totalTime);
                                appointmentOderList.setDurition(String.valueOf(truncateDecimal(totalTime, 2)));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            custum_list.add(appointmentOderList);
                            */
                        }

                        custum_list.clear();
                        custum_list.addAll(temp);


                        Collections.sort(custum_list, new Comparator<AppointmentDetailModel>() {
                            @Override
                            public int compare(AppointmentDetailModel o1, AppointmentDetailModel o2) {
                                return (int) (o1.shedule_date - o2.shedule_date);
                            }
                        });
                        adapter1.notifyDataSetChanged();

                        if (cancelCount == custum_list.size()) {
                            isCurrent = false;

                        }

                        if (isVenueInfo == true) {

                            getBusinessInfo();

                        } else {
                            getUserInformation();

                            //self.tblVw.reloadData()
                            //resizeView();

                        }

                        if (isCurrent)
                            cancelLaout.setVisibility(View.VISIBLE);
                        else
                            cancelLaout.setVisibility(View.GONE);

                        filterPaymentInfo();






                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Tag", "Error: " + error.getMessage());

                hidepDialog();

                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);

                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);
                                Log.d(TAG, jsonObject.toString());

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }

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


    public JSONArray createParamater(String reason, ArrayList<String> arrCancelService) {

        JSONArray arrPramsService = new JSONArray();

        for (JSONObject dictServ : arrServiceParam) {

            try {
                String service_name = dictServ.getString("service_name");


                if (arrCancelService.contains(service_name)) {
                    dictServ.put("status", "Cancelled");
                    dictServ.put("reason", reason);

                    arrPramsService.put(dictServ);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
        return arrPramsService;
    }

    private JSONArray createParam(String reason, ArrayList arrCancelService) {

        // let arrPramsService = NSMutableArray()
        JSONArray arrServiceParam1 = new JSONArray();

        for (JSONObject dictServ : arrServiceParam) {
            arrServiceParam1.put(dictServ);
            try {
                String service_name = dictServ.getString("service_name");

                if (arrCancelService.contains(service_name)) {
                    dictServ.put("status", "Cancelled");
                    dictServ.put("reason", reason);


                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return arrServiceParam1;
    }

String TAG = "AppointmentDetailsFragment";
    public void doCancelOrder(final String reason, final ArrayList arrCancelService) {


        //var url : String = WEBSERVICEURL
        String url = Constant.HOST + "api/v1/external/booking/cancel";
        showpDialog();

        JSONArray arrPrm = createParamater(reason, arrCancelService);


        JSONObject dictMain = new JSONObject();

        try {
            dictMain.put("userid", Constant.getUserId());
            dictMain.put("bookings", arrPrm);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, dictMain, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonResult) {
                Log.d("Tag", jsonResult.toString());
                hidepDialog();

                if (jsonResult != null) {

                    try {
                        String message = jsonResult.getString("message");

                        if (message != null) {

                            if (message.equalsIgnoreCase("Booking updated successfully")) {
                                doUpdateOrder(reason, arrCancelService);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {

                    Toast.makeText(getActivity(), "Failed!, unable to cancel your order", Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Tag", "Error: " + error.getMessage());

                hidepDialog();

                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);

                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);
                                Log.d(TAG, jsonObject.toString());

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    private void doUpdateOrder(String reason, ArrayList arrCancelService) {

        showpDialog();
        String orderId = Constant.getMyOderID(getActivity());
        String businessId = Constant.getMyOderBusinessId(getActivity());


        String url = Constant.HOST;
        url += "api/v1/order/" + orderId;


        JSONArray arrPrm = createParam(reason, arrCancelService);
        int foundCount =0;
        for(int i=0;i<arrPrm.length();i++)
        {
            try {
                JSONObject object1 = arrPrm.getJSONObject(i);
                String status = object1.getString("status");
                if(status.equalsIgnoreCase("Cancelled"))foundCount++;


            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        JSONObject dictMain = new JSONObject();

        try {
            dictMain.put( "bookings",arrPrm);
            if (foundCount == arrServiceParam.size()) {
                dictMain.put("status", "Cancelled");
                dictMain.put("reason", reason);
            } else {
                dictMain.put("status", "Confirmed");
            }


            dictMain.put("id", orderId);
            dictMain.put("order_id", giftOrderId);
            dictMain.put("business", businessId);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, dictMain, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonResult) {
                Log.d("Tag", jsonResult.toString());
                hidepDialog();

                boolean success = false;
                if (jsonResult != null) {

                    if (jsonResult.names().length() > 5) {
                        getOderServiceDetails(false);
                        success = true;
                        //  appDelegate.showMessage("Cancel")
                    }
                }
                if (success == false) {

                    Toast.makeText(getActivity(), "Failed!, unable to cancel your order", Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Tag", "Error: " + error.getMessage());

                hidepDialog();
                getOderServiceDetails(false);


                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);

                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);
                                Log.d(TAG, jsonObject.toString());

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);


    }


    ArrayList<String> arrStaticPay = new ArrayList<>();
    ArrayList<String> arrValuesPay = new ArrayList<>();

    private void filterPaymentInfo() {

        int overAllPaid = 0;
        int overAllGift = 0;
        int overAllVoucher = 0;
        int overAllPrice = 0;

        int overAllPoints = 0;

        boolean isVoucher = false;
        boolean isGift = false;


        int i = 0;

        ArrayList arrStatus = new ArrayList();

        ArrayList<AppointmentDetailModel> arrServices = custum_list;
        for (AppointmentDetailModel dictPay : arrServices) {

            String statusShow = "";

            String status = dictPay.status;
            if (status != null) {
                arrStatus.add(status);

                if (status.equalsIgnoreCase("Cancelled")) {
                    i = i + 1;
                }


            }

            String pay_at = dictPay.pay_at;

            if (pay_at != null && pay_at.length() > 0) {

                if (arrStaticPay.contains("Payment Type") == false) {
                    arrStaticPay.add("Payment Type");
                    if (pay_at.equalsIgnoreCase("cash")) {
                        arrValuesPay.add("Pay at venue");
                    } else if (pay_at.equalsIgnoreCase("card")) {
                        arrValuesPay.add("Pay with card");
                    } else if (pay_at.equalsIgnoreCase("wallet")) {
                        arrValuesPay.add("Pay with wallet");
                    } else if (pay_at.equalsIgnoreCase("points")) {
                        arrValuesPay.add("Pay with loyalty");
                    } else if (pay_at.equalsIgnoreCase("gift_voucher")) {
                        arrValuesPay.add("Pay with gift/promo");
                    } else if (pay_at.equalsIgnoreCase("total")) {
                        arrValuesPay.add("Total");
                    } else {
                        arrValuesPay.add("Incomplete");
                    }
                }
            }

            boolean isPaid = dictPay.isPaid;

            boolean enable_points = dictPay.enable_points;

            if (pay_at.equalsIgnoreCase("cash")) {

                if (isPaid == true) {
                    if (arrStaticPay.contains("Payment Status") == false) {
                        arrStaticPay.add("Payment Status");
                        arrValuesPay.add("Completed");
                    }

                } else {
                    if (arrStaticPay.contains("Payment Status") == false) {
                        arrStaticPay.add("Payment Status");
                        arrValuesPay.add("Incomplete");

                    }
                }


            } else {
                if (enable_points == true) {

                    if (arrStaticPay.contains("Payment Status") == false) {
                        arrStaticPay.add("Payment Status");
                        arrValuesPay.add("Completed");
                    }

                } else {
                    if (isPaid == true) {
                        if (arrStaticPay.contains("Payment Status") == false) {
                            arrStaticPay.add("Payment Status");
                            arrValuesPay.add("Completed");
                        }

                    } else {
                        if (arrStaticPay.contains("Payment Status") == false) {
                            arrStaticPay.add("Payment Status");
                            arrValuesPay.add("Incomplete");

                        }
                    }
                }
            }

            int amount_Paid = dictPay.amount_paid;
            int gift_paid = dictPay.gift_used_value;
            int voucher_paid = dictPay.voucher_used_value;


            overAllPaid = overAllPaid + amount_Paid;


            if (gift_paid > 0) {
                isGift = true;
                overAllGift = overAllGift + gift_paid;
            }

            if (voucher_paid > 0) {
                isVoucher = true;
                overAllVoucher = overAllVoucher + voucher_paid;
            }


            if (pay_at.equalsIgnoreCase("points")) {

                int points = dictPay.points;

                if (points > 0) {
                    overAllPoints = overAllPoints + points;
                }
                venue_message.setVisibility(View.GONE);

            } else {
                venue_message.setVisibility(View.VISIBLE);
                String price = dictPay.price + "";
                String dis = dictPay.discount + "";

                int priceAmt = dictPay.price;
                int dictAmt = dictPay.discount;


                double calAmt1 = (double) (priceAmt) / 100;
                Double calAmt = calAmt1 * (double) (dictAmt);

                int discountAmount = calAmt.intValue();
                int remainAmount = 0;

                remainAmount = priceAmt - discountAmount;
                overAllPrice = overAllPrice + remainAmount;

            }

        }


        if (arrStaticPay.contains("Order Status") == false) {

            if (i == arrServices.size()) {
                arrStaticPay.add(0, "Order Status");
                arrValuesPay.add(0, "Cancelled");

            } else {


                if (arrStatus.contains("Confirmed")) {

                    arrStaticPay.add(0, "Order Status");
                    arrValuesPay.add(0, "Confirmed");

                } else if (arrStatus.contains("Completed")) {

                    arrStaticPay.add(0, "Order Status");
                    arrValuesPay.add(0, "Completed");

                } else if (arrStatus.contains("No Show")) {

                    arrStaticPay.add(0, "Order Status");
                    arrValuesPay.add(0, "No Show");
                } else {

                    arrStaticPay.add(0, "Order Status");
                    arrValuesPay.add(0, "Completed");

                }

            }

        } else {
            arrStaticPay.remove(0);
            arrValuesPay.remove(0);

            if (i == arrServices.size()) {
                arrStaticPay.add(0, "Order Status");
                arrValuesPay.add(0, "Cancelled");

            } else {


                if (arrStatus.contains("Confirmed")) {

                    arrStaticPay.add(0, "Order Status");
                    arrValuesPay.add(0, "Confirmed");

                } else if (arrStatus.contains("Completed")) {

                    arrStaticPay.add(0, "Order Status");
                    arrValuesPay.add(0, "Completed");

                } else if (arrStatus.contains("No Show")) {

                    arrStaticPay.add(0, "Order Status");
                    arrValuesPay.add(0, "No Show");
                } else {

                    arrStaticPay.add(0, "Order Status");
                    arrValuesPay.add(0, "Completed");

                }

            }
        }

        AppointmentDetailModel dictPayment = arrServices.get(0);
        String pay_at = dictPayment.pay_at;


        if (pay_at.equalsIgnoreCase("points")) {

            if (arrStaticPay.contains("Order Total") == false) {
                arrStaticPay.add("Order Total");
                String overAll = overAllPoints + " Points";
                arrValuesPay.add(overAll);
            }

        } else {

            if (arrStaticPay.contains("Order Total") == false) {
                arrStaticPay.add("Order Total");
                String overAll = overAllPrice + " AED";
                arrValuesPay.add(overAll);
            }

            if (isGift == true) {
                if (arrStaticPay.contains("Gift Voucher Value") == false) {
                    arrStaticPay.add("Gift Voucher Value");
                    String overAll = overAllGift + " AED";
                    arrValuesPay.add(overAll);
                }
            }

            if (isVoucher == true) {
                if (arrStaticPay.contains("Promo Code Value") == false) {
                    arrStaticPay.add("Promo Code Value");
                    String overAll = overAllVoucher + " AED";
                    arrValuesPay.add(overAll);
                }
            }


            if (!pay_at.equalsIgnoreCase("cash")) {

                if (pay_at.equalsIgnoreCase("card") || pay_at.equalsIgnoreCase("wallet")) {

                    if (arrStaticPay.contains("Pay at Checkout") == false) {
                        arrStaticPay.add("Pay at Checkout");
                        String overAll = overAllPaid + " AED";
                        arrValuesPay.add(overAll);
                    }

                } else {
                    if (arrStaticPay.contains("Pay at Checkout") == false) {
                        arrStaticPay.add("Pay at Checkout");
                        arrValuesPay.add("0 AED");
                    }
                }

            }

            if (pay_at.equalsIgnoreCase("cash")) {

                if (arrStaticPay.contains("Pay at Venue") == false) {
                    arrStaticPay.add("Pay at Venue");
                    String overAll = overAllPrice + " AED";
                    arrValuesPay.add(overAll);
                }

            } else {
                if (arrStaticPay.contains("Pay at Venue") == false) {
                    arrStaticPay.add("Pay at Venue");
                    arrValuesPay.add("0 AED");
                }

            }


        }
        setPaymentDetail();

    }

public String giftOrderId="";

    private void getGiftInfo(){

showpDialog();
        String orderId = Constant.getMyOderID(getActivity());

        String url =Constant.HOST;
        url += "api/v1/order/"+orderId+"?populate=gift,voucher";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("DashBoard", response.toString());
                hidepDialog();
                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        giftOrderId  = SafeParser.getString(response,"order_id","0");


                    }
                    else
                        Toast.makeText(getActivity(), "Sorry unable to process your request! ", Toast.LENGTH_LONG).show();


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Sorry unable to process your request! ", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                Toast.makeText(getActivity(), "Sorry unable to process your request! ", Toast.LENGTH_LONG).show();
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

        // }
    }


    private void setPaymentDetail() {

        boolean hasgift = false, hasPromo = false, hasCheckout = false, hasVenu = false;
        for (int i = 0; i < arrStaticPay.size(); i++) {
            String label = arrStaticPay.get(i);
            String value = arrValuesPay.get(i);

            if (label.equalsIgnoreCase("Order Status")) {
                order_status.setText(": " + value);
            } else if (label.equalsIgnoreCase("Pay at Venue")) {
                hasVenu = true;
                textvenu.setText(": " + value);
            } else if (label.equalsIgnoreCase("Pay at Checkout")) {
                txtcheckout.setText(": " + value);
                hasCheckout = true;
            } else if (label.equalsIgnoreCase("Promo Code Value")) {
                hasPromo = true;
                textpromo.setText(": " + value);
            } else if (label.equalsIgnoreCase("Gift Voucher Value")) {
                hasgift = true;
                textgift.setText(": " + value);
            } else if (label.equalsIgnoreCase("Order Total")) {
                order_total.setText(": " + value);
            } else if (label.equalsIgnoreCase("Payment Type")) {
                payment_type.setText(": " + value);

            } else if (label.equalsIgnoreCase("Payment Status")) {
                payment_status.setText(": " + value);
            }


        }


        if (hasVenu)
            tableRowVenu.setVisibility(View.VISIBLE);
        else
            tableRowVenu.setVisibility(View.GONE);


        if (hasCheckout)
            tableRowCheckout.setVisibility(View.VISIBLE);
        else
            tableRowCheckout.setVisibility(View.GONE);


        if (hasgift)
            tableRowGift.setVisibility(View.VISIBLE);
        else
            tableRowGift.setVisibility(View.GONE);

        if (hasPromo)
            tableRowPromo.setVisibility(View.VISIBLE);
        else
            tableRowPromo.setVisibility(View.GONE);

    }

    private static BigDecimal truncateDecimal(double x, int numberofDecimals) {
        if (x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }

    private void getBusinessInfo() {

        showpDialog();

        String url = Constant.HOST + "api/v1/business/" + Constant.getMyOderBusinessId(getActivity()) + "?populate=city,region";
        System.out.println("getOrderConfirmDetail-->" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("DashBoard", response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        String name = response.getString("name");
                        String address = response.getString("address");
                        String lat = response.getString("lat");
                        String longid = response.getString("long");
                        String phone = response.getString("phone");
                        String email = response.getString("email");
                        String website = response.getString("website");

                        latitude = Double.parseDouble(lat);
                        longitude = Double.parseDouble(longid);

                        oder_buisiness_name.setText(name);
                        oder_name_sub.setText(name);


                        oder_phone.setText(phone);


                        if(email==null || email.equalsIgnoreCase("null") || email.length()==0)
                            oder_email.setVisibility(View.GONE);
                        else
                            oder_email.setText(email);



                        if(website==null || website.equalsIgnoreCase("null") || website.length()==0)
                            order_website.setVisibility(View.GONE);
                        else
                            order_website.setText(website);


                        String city = response.getString("city");
                        String region = response.getString("region");
                        String parking = response.getString("parking");
                        String media = response.getString("media");

                        String cityName = "", regionName = "";
                        if (!city.equalsIgnoreCase("null") && city.length()>0) {

                            JSONObject cityObj = response.getJSONObject("city");
                            cityName = cityObj.getString("name");
                        }

                        if (!region.equalsIgnoreCase("null") && region.length()>0) {

                            JSONObject regionObj = response.getJSONObject("region");
                            regionName = regionObj.getString("name");
                        }


                        oder_buisiness_name.setText(name);
                        oder_parking.setText(regionName + ", " + cityName);

                        order_city_region.setText(regionName + ", " + cityName);


                        if(address==null || address.equalsIgnoreCase("null") || address.length()==0)
                            oder_address.setText(regionName + ", " + cityName);
                        else
                            oder_address.setText(address);


                        if (!parking.equalsIgnoreCase("null") && parking.length()>0) {

                            JSONObject parkingObj = response.getJSONObject("parking");
                            oder_parking.setText(parkingObj.getString("info"));
                        }

                        if (!media.equalsIgnoreCase("null") && media.length()>0) {

                            JSONObject mediaObj = response.getJSONObject("media");
                            String thumbImage = mediaObj.getString("thumb");

                            if (thumbImage != "null" && !"".equals(thumbImage)) {
                                //getName is valid
                                try {
                                    Transformation transformation = new RoundedTransformationBuilder()
                                            .borderColor(Color.GRAY)
                                            .borderWidthDp(1)
                                            .cornerRadiusDp(30)
                                            .oval(false)
                                            .build();
                                    Picasso.with(getActivity())
                                            .load(thumbImage)
                                            .noFade()
                                            .transform(transformation)
                                            .fit()
                                            .into(thumbnail);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }



                        setmMap();

                        // getOderServiceDetails();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
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

    private void getUserInformation() {

        String url = Constant.GET_RROFILE + Constant.getUserId(getActivity());
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
                                if (profile.has("thumb")) {
                                    String profile_image_thump = profile.getString("thumb");
                                    Constant.setProfileImage(profile_image_thump);
                                }
                                if (profile.has("medium")) {
                                    String profile_image_medium = profile.getString("medium");

                                    Constant.setProfileImageMedium(profile_image_medium);
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        Constant.setCity(city);
                        Constant.setFirstName(first_name);
                        Constant.setLastName(last_name);
                        Constant.setEmail(email);

                        Constant.savedData(gender, "kGenderKey");
                        Constant.savedData(address, "kAddressKey");
                        Constant.savedData(phone, "kphoneKey");
                        Constant.savedData(points, "kLoyalityKey");
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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
