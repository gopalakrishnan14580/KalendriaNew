package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.PaymentActivity;
import com.kalendria.kalendria.adapter.AppointmentConfirmationAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.KACoupen;
import com.kalendria.kalendria.model.RegisterSpinner;
import com.kalendria.kalendria.utility.CommonSingleton;
import com.kalendria.kalendria.utility.KAJsonArrayRequest;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by rajaganapathi on 1/14/2016.
 */
public class AppointmentConfirmFragment extends Fragment {


    public static String Tag = AppointmentConfirmFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    AppointmentConfirmationAdapter adapter1;
    private SharedPreferences sharedPref;
    FrameLayout email_layout,phone_layout,city_layout,zip_layout,address_layout;
    TextView appointment_back_btn, appointment_total_price_txt, appointment_promo_code_btn, appointment_gift_voucher_btn;
    EditText appointment_email_et, appointment_username_et, appointment_phone_et, appointment_city_et;
    EditText appointment_zipcode_et, appointment_address_et, appointment_promo_code_et, appointment_gift_voucher_et;
    TextView appointment_discount_price_txt;
    TextView appointment_venu_name_txt, appointment_venu_city_and_regien;
    TextView txt_promocde_value,txt_gift_value;
    TextView appointment_often_name,appointment_other_label;
    LinearLayout appointment_other_layout;

    Button appointment_proceed_btn;
    ListView appointment_list;
    AddToCardVenueModel selectedAddToCardVenueModel;
    ArrayList arrCheckedItems;
    LinearLayout personalInfoView;

    LinearLayout voucherLayout,promocodeLayout;

    boolean isGiftFirst = false;
    boolean isVoucherFirst = false;
    boolean isGift = false;
    boolean isVoucher = false;

    public int discount =0;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        View rootView = inflater.inflate(R.layout.appointment_confirmation, container, false);

        personalInfoView = (LinearLayout)rootView.findViewById(R.id.personalInfoView);
        appointment_back_btn = (TextView) rootView.findViewById(R.id.appointment_back_btn);

        appointment_total_price_txt = (TextView) rootView.findViewById(R.id.appointment_total_price_txt);
        appointment_email_et = (EditText) rootView.findViewById(R.id.appointment_email_et);
        appointment_username_et = (EditText) rootView.findViewById(R.id.appointment_username_et);
        appointment_phone_et = (EditText) rootView.findViewById(R.id.appointment_phone_et);
        appointment_city_et = (EditText) rootView.findViewById(R.id.appointment_city_et);
        appointment_zipcode_et = (EditText) rootView.findViewById(R.id.appointment_zipcode_et);
        appointment_address_et = (EditText) rootView.findViewById(R.id.appointment_address_et);
        appointment_promo_code_et = (EditText) rootView.findViewById(R.id.appointment_promo_code_et);
        appointment_gift_voucher_et = (EditText) rootView.findViewById(R.id.appointment_gift_voucher_et);
        appointment_promo_code_btn = (TextView) rootView.findViewById(R.id.appointment_promo_code_btn);
        appointment_gift_voucher_btn = (TextView) rootView.findViewById(R.id.appointment_gift_voucher_btn);
        appointment_discount_price_txt = (TextView) rootView.findViewById(R.id.appointment_discount_price_txt);

        appointment_often_name = (TextView) rootView.findViewById(R.id.appointment_often_name);
        appointment_other_label = (TextView) rootView.findViewById(R.id.appointment_other_label);
        appointment_other_layout = (LinearLayout)rootView.findViewById(R.id.appointment_other_layout);
        voucherLayout = (LinearLayout)rootView.findViewById(R.id.voucher_ll);
        promocodeLayout = (LinearLayout)rootView.findViewById(R.id.promocode_ll);

        txt_gift_value = (TextView) rootView.findViewById(R.id.gift_value);
        txt_promocde_value = (TextView) rootView.findViewById(R.id.promocode_value);


        //Layouts
        email_layout = (FrameLayout)rootView.findViewById(R.id.fragment_email);
        phone_layout = (FrameLayout)rootView.findViewById(R.id.fragment_phone);
        address_layout = (FrameLayout)rootView.findViewById(R.id.fragment_address);
        city_layout = (FrameLayout)rootView.findViewById(R.id.fragment_city);
        zip_layout = (FrameLayout)rootView.findViewById(R.id.fragment_zipcode);

        appointment_venu_name_txt = (TextView) rootView.findViewById(R.id.appointment_venu_name_txt);
        appointment_venu_city_and_regien = (TextView) rootView.findViewById(R.id.appointment_venu_city_and_regien);

        appointment_proceed_btn = (Button) rootView.findViewById(R.id.appointment_proceed_btn);

        appointment_list = (ListView) rootView.findViewById(R.id.appointment_list);

        selectedAddToCardVenueModel= KalendriaAppController.getInstance().selectedAddToCardVenueModel;
        arrCheckedItems= selectedAddToCardVenueModel.getItems();

        if(selectedAddToCardVenueModel.isFromLoyality)
        {
            voucherLayout.setVisibility(View.GONE);
            promocodeLayout.setVisibility(View.GONE);
        }


        System.out.println("size of appointmentConfirmationList" + arrCheckedItems.size());
        adapter1 = new AppointmentConfirmationAdapter(getActivity(), arrCheckedItems);
        appointment_list.setAdapter(adapter1);

        appointment_email_et.setEnabled(false);
        appointment_username_et.setEnabled(false);
        appointment_other_label.setTextColor(Color.parseColor("#0097db"));


        isGiftFirst = false;
        isVoucherFirst = false;
        isGift = false;
        isVoucher = false;

        KalendriaAppController.getInstance().dictGift=null;
        KalendriaAppController.getInstance().dictPromo=null;
        getUserInforamtion();
        updateUserInfo();
        allActions();
        setTotalPrice();
        reOrderView();
        assignValue();
        appointment_phone_et.setEnabled(true);
        return rootView;
    }
    @Override
    public void onPause() {
        super.onPause();
        KalendriaAppController.hideSoftKeyboard(getActivity());
    }
    public void checkValidation()
    {
        if(!validateField())
        {
            appointment_proceed_btn.setBackgroundColor(KalendriaAppController.getResColor(R.color.colorLightTextColor));
            appointment_proceed_btn.setEnabled(false);
        }
        else
        {
            appointment_proceed_btn.setBackgroundColor(Color.parseColor("#0097db"));
            appointment_proceed_btn.setEnabled(true);
        }
    }
    public boolean validateField()
    {

        if(selectedAddToCardVenueModel.getUserSector().equalsIgnoreCase("2")){

            if(appointment_phone_et.getText().toString().length()<5){
                return false;
            }

            if(appointment_zipcode_et.getText().toString().length()<=2){
               return false;
            }



            if(appointment_address_et.getText().toString().length()<1){

                return false;
            }

        }else{

            if(appointment_phone_et.getText().toString().length()<5){
                return false;
            }
        }

        return true;
    }
    public void  updateUserInfo()
    {
        appointment_email_et.setText(Constant.getEmail());
        appointment_username_et.setText(Constant.getFirstName());
        appointment_phone_et.setText(Constant.getPhone());

        appointment_venu_name_txt.setText(selectedAddToCardVenueModel.getVenueName());
        appointment_venu_city_and_regien.setText(selectedAddToCardVenueModel.getRegion() + ", " + selectedAddToCardVenueModel.getCity());


    }
    public void reOrderView()
    {
        String sector = selectedAddToCardVenueModel.getUserSector();


        String strCity = "";
        ArrayList<RegisterSpinner> cityModelArray = CommonSingleton.getInstance().getCityMode();
        for (RegisterSpinner spinner : cityModelArray)
        {
            if(spinner.getId().equalsIgnoreCase(Constant.getCity()))
            {
                strCity= spinner.getName();
                break;
            }

        }
        appointment_city_et.setEnabled(false);
        if(sector.equalsIgnoreCase("2")){
            city_layout.setVisibility(View.VISIBLE);
            zip_layout.setVisibility(View.VISIBLE);
            address_layout.setVisibility(View.VISIBLE);

            appointment_city_et.setText(strCity);
            appointment_zipcode_et.setText(Constant.getZipCode());

            appointment_address_et.setText(Constant.getAdress());
            appointment_phone_et.setText(Constant.getPhone());
          //  vwIroning.hidden = false
            zip_layout.setVisibility(View.GONE);
        }else{
            city_layout.setVisibility(View.GONE);
            zip_layout.setVisibility(View.GONE);
            address_layout.setVisibility(View.GONE);
        }

        txt_promocde_value.setVisibility(View.GONE);
        txt_gift_value.setVisibility(View.GONE);

        String often =selectedAddToCardVenueModel.getHow_often();
        if(sector.equalsIgnoreCase("2") )
        {
            if(often!=null && often.length()>0)
            appointment_often_name.setText("How often: "+often);
            else
                appointment_often_name.setText("How often: "+often);
            appointment_other_layout.setVisibility(View.VISIBLE);
        }
        else
        {
            appointment_other_layout.setVisibility(View.GONE);
        }



    }
    public void setTotalPrice()
    {

        selectedAddToCardVenueModel.isVoucher = false;
        selectedAddToCardVenueModel.isGift = false;
        int totalAmount=0;
        boolean isFromLoyality=false;
        for (int i = 0; i < arrCheckedItems.size(); i++) {


            try {
                AddToCardServiceModel addToCardServiceModel = (AddToCardServiceModel) arrCheckedItems.get(i);
                isFromLoyality = addToCardServiceModel.isFromLoyality;
                if (isFromLoyality)
                    totalAmount += addToCardServiceModel.getIntServicePoints();
                else
                    totalAmount += addToCardServiceModel.remainAmount;

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        if(isFromLoyality)
            appointment_total_price_txt.setText(totalAmount+" Points");
        else
            appointment_total_price_txt.setText(totalAmount + " AED");


        selectedAddToCardVenueModel.totAmtPaid = totalAmount;

    }

    public void allActions() {

       addTextFieldListner();



        appointment_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


        appointment_promo_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedAddToCardVenueModel.isVoucher) {
                    selectedAddToCardVenueModel.isVoucher = false;
                    selectedAddToCardVenueModel.coupen = null;
                    appointment_promo_code_et.setText("");
                    appointment_promo_code_et.setEnabled(true);
                    appointment_promo_code_btn.setText("Apply");
                    appointment_promo_code_btn.setTextColor(KalendriaAppController.getResColor(R.color.colorSkyBlue));
                    txt_promocde_value.setVisibility(View.GONE);
                    assignValue();
                } else {
                    if (appointment_promo_code_et.getText().toString().length() > 0)
                        validatePromoCode();
                    else {
                        Toast.makeText(AppointmentConfirmFragment.this.getActivity(), "Please enter your promocode", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        appointment_gift_voucher_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedAddToCardVenueModel.isGift) {
                    txt_gift_value.setVisibility(View.GONE);

                    selectedAddToCardVenueModel.isGift = false;
                    selectedAddToCardVenueModel.gift = null;
                    appointment_gift_voucher_et.setText("");
                    appointment_gift_voucher_et.setEnabled(true);
                    appointment_gift_voucher_btn.setText("Apply");
                    appointment_gift_voucher_btn.setTextColor(KalendriaAppController.getResColor(R.color.colorSkyBlue));
                    assignValue();
                } else {
                    if (appointment_gift_voucher_et.getText().toString().length() > 0)
                        validateGiftVoucher();
                    else {
                        Toast.makeText(AppointmentConfirmFragment.this.getActivity(), "Please enter your giftvoucher", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        appointment_proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               doUpdateUserInfo();
            }
        });

    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private boolean  validation()
    {
        return true;
    }

    String TAG = "CheckOutFragment";
    public void getUserInforamtion() {

        showpDialog();
        String userId = Constant.getUserId(getActivity());
        String url =Constant.HOST+"api/v1/user?id="+userId;
        //System.out.println("VeneItemFragement-->"+url);
        Log.d(TAG, url);
        KAJsonArrayRequest jsonObjReq = new KAJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());


                // Parsing json object response response will be a json object
                if (response != null) {


                    if (response.length() > 0) {
                        try {
                            JSONObject jsonResult = response.getJSONObject(0);


                            if (jsonResult.names().length() >= 7) {

                                boolean isVerfied = SafeParser.getBoolen(jsonResult, "isVerified", false);

                                if (isVerfied == true) {


                                    String city = SafeParser.getString(jsonResult, "city", "");
                                    String address = SafeParser.getString(jsonResult, "address", "");
                                    String zipCode = SafeParser.getString(jsonResult, "zip", "");
                                    String phone = SafeParser.getString(jsonResult, "phone", "");

                                    if (city.length() > 0) {
                                        Constant.setCity(city);
                                    }

                                    if (address != null) {
                                        Constant.setAddress(address);
                                    }

                                    if (zipCode != null) {
                                        Constant.setZipCode(zipCode);
                                    }

                                    if (phone != null) {
                                        Constant.setPhone(phone);
                                    }


                                    int points = SafeParser.getInt(jsonResult, "points", 0);
                                    int wallet = SafeParser.getInt(jsonResult, "credit", 0);

                                    Constant.savedData("" + points, "kLoyalityKey");
                                    Constant.savedData("" + wallet, "kWalletsKey");

                                    updateUserInfo();
                                    if (validation() == true) {
                                        //KalendriaAppController.getInstance().selectedAddToCardVenueModel = selectedAddToCardVenueModel;
                                        //Intent intent = new Intent(getActivity(), PaymentActivity.class);
                                        //startActivity(intent);
                                    /*
                                        let paymentVC = self.storyboard?.instantiateViewControllerWithIdentifier("PaymentVC") as! PaymentVC
                                        paymentVC.dictBusiness = self.dictBusiness
                                        paymentVC.isVoucher = self.isVoucher
                                        paymentVC.isGift = self.isGift
                                        paymentVC.totAmtPaid = self.totPaidAmt
                                        paymentVC.arrService = self.arrServices
                                        self.navigationController?.pushViewController(paymentVC, animated: true)
                                        */
                                    }


                                    try {


                                        if (jsonResult.has("profile_image") && jsonResult.getJSONObject("profile_image") instanceof JSONObject) {
                                            JSONObject dictImage = jsonResult.getJSONObject("profile_image");
                                            if (dictImage != null && dictImage.names()!=null && dictImage.names().length() > 0) {
                                                String url = SafeParser.getString(dictImage, "url", "");
                                                Constant.setProfileImage(url);
                                            }
                                        }
                                    }catch (Exception ex){ex.printStackTrace();}
                                } else {

                                    String message = "Your account has not yet been activated. Please click the verfication link sent to your registered email id.";
                                    Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_LONG).show();


                                }


                            } else {

                                String message = SafeParser.getString(jsonResult, "message", "");
                                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Toast.makeText(getActivity(), "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }
                }
                hidepDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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

    private  String Trim(String input)
    {
        return input;
    }


    private void doUpdateUserInfo(){

     KalendriaAppController.hideSoftKeyboard(getActivity());

        if(!KalendriaAppController.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_LONG);

            return;

        }

       showpDialog();


        String url = Constant.HOST+ "api/v1/user/" + Constant.getUserId(getActivity());

        JSONObject dictMain  = new JSONObject();
        try {

            /*
            KalendriaAppController.getInstance().selectedAddress=appointment_address_et.getText().toString();
            KalendriaAppController.getInstance().selectedCity=appointment_city_et.getText().toString();
            KalendriaAppController.getInstance().selectedZipcode=appointment_zipcode_et.getText().toString();
            KalendriaAppController.getInstance().selectedPhone=appointment_phone_et.getText().toString();
            */

            if (selectedAddToCardVenueModel.getUserSector().equalsIgnoreCase("2")) {
                String addrss = "";

                if (Trim(appointment_address_et.getText().toString()).length() > 0) {
                    addrss = Trim(appointment_address_et.getText().toString());
                }

                dictMain.put("address", addrss);
                dictMain.put("zip", Trim(appointment_zipcode_et.getText().toString()));


            }
            dictMain.put("phone", Trim(appointment_phone_et.getText().toString()));
        }catch (JSONException ex){ex.printStackTrace();}


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, dictMain, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonResult) {
                Log.d(TAG, jsonResult.toString());

                hidepDialog();
                try {

                    if (jsonResult != null) {

                        if (jsonResult.names().length() > 0) {



                                boolean isVerfied = SafeParser.getBoolen(jsonResult,"isVerified",false);

                                if(isVerfied == true){

                                    int city = SafeParser.getInt(jsonResult,"city",0);
                                    String address = SafeParser.getString(jsonResult,"address");
                                    String zipCode = SafeParser.getString(jsonResult, "zip");
                                    String phone = SafeParser.getString(jsonResult, "phone");
                                    String email = SafeParser.getString(jsonResult,"email");
                                    String firstName = SafeParser.getString(jsonResult, "first_name");
                                    String lastName = SafeParser.getString(jsonResult, "last_name");

                                    Constant.setEmail(email);
                                    Constant.setFirstName(firstName);
                                    Constant.setLastName(lastName);

                                    if(city >0){
                                       Constant.setCity(""+city);
                                    }

                                    if(address != null){
                                        Constant.setAddress(address);

                                    }

                                    if(zipCode != null){
                                        Constant.setZipCode(zipCode);
                                    }

                                    if(phone != null){
                                       Constant.setPhone(phone);
                                    }

                                    int points =  SafeParser.getInt(jsonResult,"points",0);
                                    int wallet =  SafeParser.getInt(jsonResult,"credit",0);

                                    Constant.setWallet("" + wallet);
                                    Constant.setPoints("" + points);


                                    Intent intent = new Intent(getActivity(), PaymentActivity.class);
                                    startActivity(intent);


                                }else{
                                    Toast.makeText(getActivity(), "Your account has not yet been activated. Please click the verfication link sent to your registered email id.", Toast.LENGTH_LONG).show();

                                }




                        }


                    } else {

                        Toast.makeText(getActivity(), "Sorry, unable to process your request ", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }


                hidepDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if(error.networkResponse.statusCode==400)
                {
                    Toast.makeText(getActivity(), "Sorry, your promo code is invalid", Toast.LENGTH_LONG).show();
                }
                else
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void addTextFieldListner()
    {
        appointment_phone_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {


                if (cs.length() > 0 && cs.charAt(cs.length() - 1) == '\n') {


                   KalendriaAppController.getInstance().hideSoftKeyboard(getActivity());
                   checkValidation();
                    //delegate.OnAddNewTextOption(position,option,editTextField.getText().toString().trim() );
                }


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                checkValidation();
            }

        });

        appointment_zipcode_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {


                if (cs.length() > 0 && cs.charAt(cs.length() - 1) == '\n') {


                    KalendriaAppController.getInstance().hideSoftKeyboard(getActivity());
                    checkValidation();
                    //delegate.OnAddNewTextOption(position,option,editTextField.getText().toString().trim() );
                }


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                checkValidation();
            }

        });


        appointment_address_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {


                if (cs.length() > 0 && cs.charAt(cs.length() - 1) == '\n') {


                    KalendriaAppController.getInstance().hideSoftKeyboard(getActivity());
                    checkValidation();
                    //delegate.OnAddNewTextOption(position,option,editTextField.getText().toString().trim() );
                }


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                checkValidation();
            }

        });

    }

    private void assignValue()
    {

        checkValidation();
        int totAmount = selectedAddToCardVenueModel.totAmtPaid;
        int paidAmount = 0;
        boolean havingOffer=false;
        if(selectedAddToCardVenueModel.isFromLoyality)
        {
            appointment_discount_price_txt.setText("");
            appointment_total_price_txt.setText(totAmount+" Points");
            return;
        }
        paidAmount = totAmount;
        if(selectedAddToCardVenueModel.isGift == true){

            KACoupen dictGift = selectedAddToCardVenueModel.gift;
            int valueGift = dictGift.value;
            paidAmount = totAmount - valueGift;
            havingOffer=true;
        }

        if(selectedAddToCardVenueModel.isVoucher == true){

            havingOffer=true;
            KACoupen dictPromo = selectedAddToCardVenueModel.coupen;

            int valueAmt = dictPromo.value;
            String discount_type = dictPromo.discount_type;

            if(discount_type.equalsIgnoreCase("discount")){
                double calAmt1 = (double)((double)valueAmt / 100);
                Double calAmt = calAmt1 * (double)paidAmount;
                int discountAmount = calAmt.intValue();

                paidAmount = paidAmount - discountAmount;

            }else{

                paidAmount = paidAmount - valueAmt;

            }


        }
        // we dont have any offer gift or vochur so no discount
        if (havingOffer == false) {

            appointment_total_price_txt.setText(totAmount + " " + "AED");
            appointment_discount_price_txt.setText("");
            selectedAddToCardVenueModel.discountedNetPayable = totAmount;
            return;

        }

        if(paidAmount < 0){

            String totValues = totAmount + " " + "AED";
            appointment_discount_price_txt.setText(totValues);
            appointment_discount_price_txt.setPaintFlags(appointment_discount_price_txt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            int discountVal = 0;
            String disValues = discountVal + " " + "AED";
            appointment_total_price_txt.setText(disValues);

            selectedAddToCardVenueModel.discountedNetPayable = discountVal;

        }else{

            String totValues = totAmount + " " + "AED";
            appointment_discount_price_txt.setText(totValues);
            appointment_discount_price_txt.setPaintFlags(appointment_discount_price_txt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String disValues = paidAmount + " " + "AED";
            appointment_total_price_txt.setText(disValues);

            selectedAddToCardVenueModel.discountedNetPayable = paidAmount;

        }

/* commented by magesh to show discount value
        if(discount>0)

        appointment_discount_price_txt.setText(""+discount+" AED");
        else
            appointment_discount_price_txt.setText("");

        appointment_discount_price_txt.setVisibility(View.VISIBLE);
        */
    }


    private void validatePromoCode(){

       showpDialog();

        String url =Constant.HOST+"api/v1/coupon/validate";

        String business = selectedAddToCardVenueModel.getVenueID();
        String user = Constant.getUserId();
        String code = appointment_promo_code_et.getText().toString();
        String tot_amt = ""+selectedAddToCardVenueModel.totAmtPaid;

        JSONArray arrServ = new JSONArray();


        for (AddToCardServiceModel serviceModel : selectedAddToCardVenueModel.getItems()){
            try {
                String serviceId = serviceModel.getServiceId2();
                int sn = Integer.parseInt(serviceId);
                //let numb = NSNumber(integer: serviceId!)
                arrServ.put(sn);
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }

        JSONObject dictMain = new JSONObject();
        try {
            dictMain.put("business", business);
            dictMain.put("user", user);
            dictMain.put("code", code);
            dictMain.put("total_amount", tot_amt);
            dictMain.put("services", arrServ);
        }
        catch (Exception ex){ex.printStackTrace();}

       Log.d(TAG,dictMain.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, dictMain, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    if (response != null) {


                        try {


                            String msg = SafeParser.getString(response, "code", "");

                            if (msg != null && msg.length() > 0) {
                                Toast.makeText(getActivity(), "Sorry, your promo voucher is invalid", Toast.LENGTH_LONG).show();
                                return;
                            }


                            String mssg = SafeParser.getString(response, "message", "");

                            if (mssg != null && mssg.length() > 0) {

                                if (mssg.equalsIgnoreCase("Invalid coupon - found coupon:") || mssg.equalsIgnoreCase("You haved already used this service") || response.names().length() == 1) {

                                    Toast.makeText(getActivity(), "Sorry, your promo code is invalid", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }catch (Exception ex){ex.printStackTrace();}


                        if (response.names().length() > 0) {
                            JSONObject dictCoupon = response.getJSONObject("coupon");


                            if (dictCoupon != null) {
                                String type = dictCoupon.getString("type");

                                if (type.equalsIgnoreCase("voucher")) {

                                    KalendriaAppController.getInstance().dictPromo=response;
                                    selectedAddToCardVenueModel.isVoucher = true;

                                    String discount_type = dictCoupon.getString("discount_type");
                                    int value = dictCoupon.getInt("value");
                                    int couponId = dictCoupon.getInt("id");
                                    int voucherId = response.getInt("id");

                                    KACoupen newCoupen = new KACoupen();
                                    newCoupen.value=value;
                                    newCoupen.couponId=couponId;
                                    newCoupen.voucherId=voucherId;
                                    newCoupen.discount_type=discount_type;
                                    selectedAddToCardVenueModel.coupen=newCoupen;

                                    appointment_promo_code_et.setEnabled(false);

                                    appointment_promo_code_btn.setText("Remove");
                                    appointment_promo_code_btn.setTextColor(Color.RED);


                                    String promoValue= "Value :";


                                    if (discount_type.equalsIgnoreCase("cash")) {
                                        promoValue += " " + value + " AED";

                                    } else {
                                        promoValue += " " + value + "  %";

                                    }
                                    txt_promocde_value.setText(promoValue);
                                    txt_promocde_value.setVisibility(View.VISIBLE);

                                    assignValue();


                                } else {
                                    Toast.makeText(getActivity(), "Sorry, your promo code is invalid", Toast.LENGTH_LONG).show();

                                }

                            }

                        }


                    } else {

                        Toast.makeText(getActivity(), "Sorry, unable to verify your promocode ", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }


                hidepDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if(error.networkResponse.statusCode==400)
                {
                    Toast.makeText(getActivity(), "Sorry, your promo code is invalid", Toast.LENGTH_LONG).show();
                }
                else
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


    public void validateGiftVoucher() {



        showpDialog();

        String url =Constant.HOST+"api/v1/coupon/validate";



        String business = selectedAddToCardVenueModel.getVenueID();
        String user = Constant.getUserId();
        String code = appointment_gift_voucher_et.getText().toString();
        String tot_amt = ""+selectedAddToCardVenueModel.totAmtPaid;

        JSONArray arrServ = new JSONArray();


        for (AddToCardServiceModel serviceModel : selectedAddToCardVenueModel.getItems()){
            try {
                String serviceId = serviceModel.getServiceId2();
                int sn = Integer.parseInt(serviceId);
                //let numb = NSNumber(integer: serviceId!)
                arrServ.put(sn);
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }

        JSONObject dictMain = new JSONObject();
        try {
            dictMain.put("business", business);
            dictMain.put("user", user);
            dictMain.put("code", code);
            dictMain.put("total_amount", tot_amt);
            dictMain.put("services", arrServ);
        }
        catch (Exception ex){ex.printStackTrace();}

        Log.d(TAG, dictMain.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, dictMain, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {

                    if (response != null) {


                        try {


                            String msg = SafeParser.getString(response, "code", "");

                            if (msg != null && msg.length() > 0) {
                                Toast.makeText(getActivity(), "Sorry, your promo voucher is invalid", Toast.LENGTH_LONG).show();
                                return;
                            }


                            String mssg = SafeParser.getString(response, "message", "");

                            if (mssg != null && mssg.length() > 0) {

                                if (mssg.equalsIgnoreCase("Invalid coupon - found coupon:") || mssg.equalsIgnoreCase("You haved already used this service") || response.names().length() == 1) {

                                    Toast.makeText(getActivity(), "Sorry, your promo code is invalid", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }catch (Exception ex){ex.printStackTrace();}


                        if (response.names().length() > 0) {
                            JSONObject dictCoupon = response.getJSONObject("coupon");


                            if (dictCoupon != null) {
                                String type = dictCoupon.getString("type");

                                if (type.equalsIgnoreCase("gift")) {

                                    selectedAddToCardVenueModel.isGift = true;
                                    KalendriaAppController.getInstance().dictGift=response;
                                    String discount_type = dictCoupon.getString("discount_type");
                                    int value = dictCoupon.getInt("value");
                                    int couponId = dictCoupon.getInt("id");
                                    int voucherId = response.getInt("id");
                                    KACoupen newCoupen = new KACoupen();
                                    newCoupen.value=value;
                                    newCoupen.couponId=couponId;
                                    newCoupen.voucherId=voucherId;
                                    newCoupen.discount_type=discount_type;
                                    selectedAddToCardVenueModel.gift=newCoupen;
                                    appointment_gift_voucher_et.setEnabled(false);

                                    appointment_gift_voucher_btn.setText("Remove");
                                    appointment_gift_voucher_btn.setTextColor(Color.RED);


                                    String promoValue= "Value :"+ value + " AED";
                                    txt_gift_value.setText(promoValue);
                                    txt_gift_value.setVisibility(View.VISIBLE);

                                    assignValue();


                                } else {
                                    Toast.makeText(getActivity(), "Sorry, your gift voucher is invalid", Toast.LENGTH_LONG).show();

                                }

                            }

                        }


                    } else {

                        Toast.makeText(getActivity(), "Sorry, unable to verify gift voucher ", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }


                hidepDialog();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if(error!=null && error.networkResponse.statusCode==400)
                {
                    Toast.makeText(getActivity(), "Sorry, your gift voucher is invalid", Toast.LENGTH_LONG).show();
                }
                else
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



}
