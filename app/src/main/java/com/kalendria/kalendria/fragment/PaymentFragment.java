package com.kalendria.kalendria.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Login;
import com.kalendria.kalendria.activity.MyOrderActivity;
import com.kalendria.kalendria.activity.PayFortWebView;
import com.kalendria.kalendria.activity.PaymentActivity;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.KACoupen;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.KAJsonArrayRequest;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.SimpleFormatter;


public class PaymentFragment extends Fragment {

    View view;
    Button buttonVenu, buttonPayPal, buttonWallet, buttonContinue, buttonLoyality;
    public boolean isVoucher;
    public boolean isGift;
    public int totAmtPaid;
    public int totOriginalAmt;
    public boolean isLoyalty;
    public AddToCardVenueModel selectedAddToCardVenueModel;
    private ArrayList arrServiceList;
    private ProgressDialog pDialog;
    int giftRemainValue=0;

    public PaymentFragment() {
        // Required empty public constructor


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.payment, container, false);

        buttonVenu = (Button) view.findViewById(R.id.paywithvenu);
        buttonPayPal = (Button) view.findViewById(R.id.paypaybtn);
        buttonWallet = (Button) view.findViewById(R.id.paywithvalletbtn);
        buttonLoyality = (Button) view.findViewById(R.id.paywithloyality);
        buttonContinue = (Button) view.findViewById(R.id.continuebtn);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

       Button backButton = (Button) view.findViewById(R.id.register_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        allActions();
        udpateUI();
        getClientId();
        return view;
    }


    public void allActions() {

        buttonPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPayPortRequest(2);
            }
        });

        buttonVenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPay(1);
            }
        });

        buttonWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doPay(3);
            }
        });

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doPay(5);
            }
        });

        buttonLoyality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPay(4);
            }
        });


    }

    private void checkVenueHide(){

        if(isGift == true || isVoucher == true){

            //vwPayVenue.backgroundColor = UIColor.lightGrayColor()
            //vwPayVenue.userInteractionEnabled = false
            buttonVenu.setVisibility(View.GONE);

        }else{
            buttonVenu.setVisibility(View.VISIBLE);
            //vwPayVenue.userInteractionEnabled = true
            //vwPayVenue.backgroundColor = UIColor(hexString: "#34B4B7")

        }


    }
    private void udpateUI() {
        selectedAddToCardVenueModel = KalendriaAppController.getInstance().selectedAddToCardVenueModel;
        arrServiceList = selectedAddToCardVenueModel.getItems();

        isLoyalty = KalendriaAppController.getInstance().selectedAddToCardVenueModel.isFromLoyality;
        totOriginalAmt=KalendriaAppController.getInstance().selectedAddToCardVenueModel.totAmtPaid;
        totAmtPaid = KalendriaAppController.getInstance().selectedAddToCardVenueModel.discountedNetPayable;
        isGift = KalendriaAppController.getInstance().selectedAddToCardVenueModel.isGift;
        isVoucher = KalendriaAppController.getInstance().selectedAddToCardVenueModel.isVoucher;

        buttonPayPal.setVisibility(View.GONE);
        buttonVenu.setVisibility(View.GONE);
        buttonWallet.setVisibility(View.GONE);
        buttonContinue.setVisibility(View.GONE);
        buttonLoyality.setVisibility(View.GONE);


        if (isLoyalty == true) {
            buttonLoyality.setVisibility(View.VISIBLE);
            checkLoayty();
        } else {
            if (totAmtPaid == 0) {
                buttonContinue.setVisibility(View.VISIBLE);

            } else {
                buttonPayPal.setVisibility(View.VISIBLE);
                buttonVenu.setVisibility(View.VISIBLE);
                buttonWallet.setVisibility(View.VISIBLE);
                checkVenueHide();
                checkWallet();
            }
        }
    }

    public void checkLoayty() {
        String points = Constant.getPoints();

        int pointsVal = 0;
        try {

            pointsVal = Integer.parseInt(points);
        } catch (Exception ex) {
            pointsVal = 0;
            ex.printStackTrace();
        }
        int totPointsVal = totOriginalAmt;


        buttonLoyality.setEnabled(true);
        buttonLoyality.setBackgroundColor(Color.parseColor("#34B4B7"));

        if (pointsVal < totPointsVal) {

            buttonLoyality.setEnabled(false);
            buttonLoyality.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorDisabledButton));

        }

        if (points != "") {
            buttonLoyality.setText("Pay with loyalty (" + points + ")");

        } else {
            buttonLoyality.setText("Pay with loyalty (00)");

        }
    }

    public void updateUserData(String type,String value){

        Log.d("updateUserData", "Type:" + type + " Value:" + value);


        KalendriaAppController.hideSoftKeyboard(getActivity());

        String userId = Constant.getUserId(getActivity());

        String searchStr = "api/v1/user/" + userId;

        String url = Constant.HOST + searchStr;


        JSONObject parameter = new JSONObject();
        try {

            parameter.put(type, value);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, parameter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                hidepDialog();
                // Parsing json object response response will be a json object
                if (response != null) {

                    if (isGift == false)
                        showOrderPage();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


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


                Toast.makeText(getActivity(), KalendriaAppController.getErrorMessage(), Toast.LENGTH_SHORT).show();
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



    public void updateGiftValue() {

        KACoupen coupen = selectedAddToCardVenueModel.gift;

        //let dictGift = dictBusiness.valueForKey("Gift") as? NSMutableDictionary
        int voucherId =coupen.voucherId;
        int couponId = coupen.couponId;

        JSONObject parameter = new JSONObject();

       try {

           JSONObject coupon = new JSONObject();
           coupon.put("id",couponId);
           coupon.put("value",giftRemainValue);
           parameter.put("coupon",coupon);
           parameter.put("id",voucherId);
           if(giftRemainValue==0)
           {
               parameter.put("status","used");
           }
           else
               parameter.put("status","not used");

       }catch (Exception ex)
       {
           ex.printStackTrace();
       }

        showpDialog();
        String url = Constant.HOST+ "api/v1/gift/"+voucherId;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, parameter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                hidepDialog();
                // Parsing json object response response will be a json object
                if (response != null) {
                         showOrderPage();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


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

    public void checkWallet() {

        String wallet = Constant.getWallet();
        // let totAmount = dictBusiness.valueForKey("totAmt") as? String
        //let totAmtVal = Int(totAmount!)

        int walletVal = 0;
        try {
            walletVal = Integer.parseInt(wallet);
        } catch (Exception ex) {
            walletVal = 0;
            ex.printStackTrace();
        }


        buttonWallet.setEnabled(true);
        buttonWallet.setBackgroundColor(Color.parseColor("#1E95D5"));


        if (walletVal < totAmtPaid) {
            buttonWallet.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorDisabledButton));
            buttonWallet.setEnabled(true);

        }

        if (walletVal > 0) {
            buttonWallet.setText("Pay with Wallet (" + wallet + ")");

        } else {
            buttonWallet.setText("Pay with Wallet (00)");
            buttonWallet.setEnabled(false);
        }

    }

    public void checkVenue() {

        if (isGift == true || isVoucher == true) {

            buttonVenu.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorDisabledButton));
            buttonVenu.setEnabled(false);

        } else {

            buttonVenu.setEnabled(true);
            buttonVenu.setBackgroundColor(Color.parseColor("#34B4B7"));


        }


    }

    public JSONObject getNormalRequestObject(int val) {
        JSONObject dictMainService = new JSONObject();
        try {


            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            int totDiscount = 0;
            int totOriginalPrice = 0;
            int totalAmtPaid = 0;
            String totEndTime = "";
            String totStartTime = "";

            JSONArray arrServ = new JSONArray();

            String business = selectedAddToCardVenueModel.getVenueID();
            //dictBusiness.valueForKey("business") as? String
            int client = 0;
            try {
                client = Integer.parseInt(clientID);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            String sector = selectedAddToCardVenueModel.getUserSector();


            String how_often = "";
            String is_iron = "";


            if (sector.equalsIgnoreCase("2")) {
                how_often = selectedAddToCardVenueModel.getHow_often();
                is_iron = selectedAddToCardVenueModel.getIs_iron();
            } else {
                how_often = "Just Once";
                is_iron = "false";
            }



            long order_Id = System.currentTimeMillis();

            Log.e("Payment", "Order ID:" + order_Id);

            dictMainService.put("order_id", order_Id);
            dictMainService.put("business", business);
            dictMainService.put("client", client);
            dictMainService.put("createdBy", Constant.getUserId());
            dictMainService.put("customer", Constant.getUserId());
            dictMainService.put("created_from", "customer");
            dictMainService.put("customer_email", Constant.getEmail());
            dictMainService.put("customer_name", Constant.getFirstName());
            dictMainService.put("customer_phone",Constant.getPhone());

            dictMainService.put("how_often", how_often);
            dictMainService.put("is_iron", is_iron);


            dictMainService.put("sector", sector);
            dictMainService.put("status", "Confirmed");


            for (int i = 0; i < arrServiceList.size(); i++) {


                try {


                    AddToCardServiceModel dictSer = (AddToCardServiceModel) arrServiceList.get(i);


                  // String discount = dictSer.getServiceDiscount();
                    int disInt = dictSer.getIntServiceDiscount();
                    totDiscount = totDiscount + disInt;

                    String discount = disInt+"";

                    String employee = dictSer.staffID;

                    // let today = NSDate()

                    String selectDate = dictSer.selectedDate;

                    Date SelDate = formatter.parse(selectDate);
                    String dateStr = formatter2.format(SelDate);


                    String timing = dictSer.selectedTime;
                    String[] arrTime = timing.split("-");
                    //timing.componentsSeparatedByString("-")

                    String startTime = "";
                    String EndTime = "";

                    if (arrTime.length > 0) {
                        startTime = arrTime[0];
                        EndTime = arrTime[1];
                    }


                    String scheduledAt = dateStr.trim() + " " + startTime.trim();

                    //scheduledAt = scheduledAt.stringByTrimmingCharactersInSet(NSCharacterSet.whitespaceCharacterSet())

                    scheduledAt = scheduledAt + ":00.0000";

                    String endAt = dateStr.trim() + " " + EndTime.trim();

                    //endAt = endAt.stringByTrimmingCharactersInSet(NSCharacterSet.whitespaceCharacterSet())

                    endAt = endAt.trim() + ":00.0000";

                    totEndTime = endAt;
                    if (i == 0) {
                        totStartTime = scheduledAt;
                    }

                    String price = dictSer.getServicePrice();
                    int priceInt=0;
                    try {
                        priceInt = Integer.parseInt(price);
                    }
                    catch (Exception ex){ex.printStackTrace();}


                    totOriginalPrice = totOriginalPrice + priceInt;

                    String service = dictSer.getServiceId2();
                    String servcie_name = dictSer.getServiceName();

                    JSONObject dictBus = new JSONObject();


                    int discountPrice = 0;
                    if (disInt != 0) {

                        double calAmt1 = (double) ((double) disInt / 100);
                        Double calAmt = calAmt1 * (double) (priceInt);
                        discountPrice = calAmt.intValue();

                        discountPrice = priceInt - discountPrice;

                    } else {
                        discountPrice = priceInt;
                    }

                    if (val == 2 || val == 3) {
                        dictBus.put("amount_paid", "" + discountPrice);

                        totalAmtPaid = totalAmtPaid + discountPrice;
                    }

                    if (val == 4) {
                        String points = dictSer.getServicePoints();
                        dictBus.put("points", points);
                    }

                    dictBus.put("business", business);
                    dictBus.put("client", client);
                    dictBus.put("createdBy", Constant.getUserId());
                    dictBus.put("customer", Constant.getUserId());
                    dictBus.put("created_from", "customer");
                    dictBus.put("customer_email", Constant.getEmail());
                    dictBus.put("customer_name", Constant.getFirstName());
                    dictBus.put("customer_phone",Constant.getPhone());

                    dictBus.put("employee", employee);
                    dictBus.put("endAt", endAt);
                    dictBus.put("endTime", EndTime);
                    dictBus.put("how_often", how_often);
                    dictBus.put("is_iron", is_iron);


                    if (val == 1) {
                        dictBus.put("pay_at", "cash");
                        dictBus.put("discount", discount);
                        dictBus.put("price", price);
                    } else if (val == 3) {
                        dictBus.put("pay_at", "wallet");
                        dictBus.put("discount", discount);
                        dictBus.put("price", price);
                        dictBus.put("is_paid", true);
                    } else if (val == 4) {
                        dictBus.put("pay_at", "points");

                    } else if (val == 2) {
                        dictBus.put("pay_at", "card");
                        dictBus.put("discount", discount);
                        dictBus.put("price", price);
                        dictBus.put("is_paid", true);
                    }


                    dictBus.put("scheduledAt", scheduledAt);
                    dictBus.put("sector", sector);
                    dictBus.put("service", service);
                    dictBus.put("service_name", servcie_name);
                    dictBus.put("startTime", startTime);
                    dictBus.put("status", "Confirmed");
                    dictBus.put("time", startTime);

                    arrServ.put(dictBus);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            dictMainService.put("endAt", totEndTime);
            dictMainService.put("scheduledAt", totStartTime);
            dictMainService.put("bookings", arrServ);
            dictMainService.put("discount", totDiscount);
            if (val == 1) {
                dictMainService.put("pay_at", "cash");
                dictMainService.put("price", totOriginalPrice);

            } else if (val == 3) {
                dictMainService.put("is_paid", true);
                dictMainService.put("pay_at", "wallet");
                dictMainService.put("is_paid", true);
                dictMainService.put("price", totOriginalPrice);
                dictMainService.put("total_amount_paid", totalAmtPaid);

            } else if (val == 4) {
                dictMainService.put("is_paid", true);
                dictMainService.put("pay_at", "points");
                dictMainService.put("is_paid", true);
                dictMainService.put("discount", "0");
                dictMainService.put("price", "0");
                dictMainService.put("points", selectedAddToCardVenueModel.totAmtPaid);
            }
            else if (val == 2) {
                dictMainService.put("is_paid", true);
                dictMainService.put("pay_at", "card");
                dictMainService.put("discount", "0");
                dictMainService.put("price", totOriginalPrice);
                dictMainService.put("total_amount_paid", totalAmtPaid);


                JSONObject dictPayfort = new JSONObject();
               //dictPayfort.put("access_code","QsCnIFWy1cHVZ1KVfPI9" );
                dictPayfort.put("command","PURCHASE" );
                dictPayfort.put("currency","AED");
                dictPayfort.put("eci","ECOMMERCE");
                dictPayfort.put("language","en");

                if (Constant.isLiveBuild) {
                    dictPayfort.put("access_code", "lGQLYLUl7MMRk3NcGVqn");
                    dictPayfort.put("merchant_identifier", "jxekSPTl");
                }
                else {
                    dictPayfort.put("access_code", "QsCnIFWy1cHVZ1KVfPI9");
                    dictPayfort.put("merchant_identifier","epsBcBYT" );
                }

                int padiAmt = totalAmtPaid * 100;

                dictPayfort.put("amount",padiAmt);


                dictPayfort.put("customer_email",Constant.getEmail());
                dictPayfort.put("customer_name",Constant.getFirstName());
                dictPayfort.put("merchant_reference",order_Id);
                dictPayfort.put("order_description",order_Id);


                String returnUrl =Constant.HOST+ "api/v1/payment/execute?provider=payfort&type=order&merchant_reference=" + order_Id;

                dictPayfort.put("return_url", returnUrl);

                JSONObject dictMainServicePayFort = new JSONObject();
                dictMainServicePayFort.put("platform","mobile");
                dictMainServicePayFort.put("type", "order");
                dictMainServicePayFort.put("provider", "payfort");

                dictMainServicePayFort.put("payfort",dictPayfort);
                dictMainServicePayFort.put("data",dictMainService);

                return dictMainServicePayFort;


            }
            else {
                dictMainService.put("is_paid", true);
                dictMainService.put("pay_at", "wallet");
                dictMainService.put("is_paid", true);
                dictMainService.put("price", totOriginalPrice);
                dictMainService.put("total_amount_paid", totalAmtPaid);



            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dictMainService;
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showOrderPage() {
        Intent newIntent = new Intent(getActivity(), MyOrderActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
    }


    private void parseClient(JSONObject response)
    {
        try {


            if (response.has("data") && response.getJSONObject("data") instanceof JSONObject && response.getJSONObject("data") != null) {
                JSONObject dictData = response.getJSONObject("data");
                JSONObject dictRaw = null;
                try {
                    if (dictData.has("raw") && dictData.getJSONObject("raw") instanceof JSONObject && dictData.getJSONObject("raw") != null)
                        dictRaw = dictData.getJSONObject("raw");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                if (dictRaw != null) {

                    JSONObject dictClient = SafeParser.getObject(dictRaw, "client");

                    if (dictClient != null) {

                        Log.d("Payment", "DicClient "+dictClient.toString());
                        clientID = SafeParser.getString(dictClient, "id", "0"); //dictClient !.valueForKey("id") ?.integerValue

                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String clientID="0";

    public void getClientId() {
        showpDialog();
        String url = Constant.HOST + "api/v1/client";

        JSONObject parameter = new JSONObject();

        try {


            parameter.put("address", Constant.getAdress());
            parameter.put("business", selectedAddToCardVenueModel.getVenueID());
            parameter.put("business_name", selectedAddToCardVenueModel.getVenueName());
            parameter.put("city",  Constant.getCity());
            parameter.put("email", Constant.getEmail());
            parameter.put("first_name", Constant.getFirstName());
            parameter.put("phone", Constant.getPhone());
            parameter.put("type", "customer");
            parameter.put("user", Constant.getUserId());
            parameter.put("last_name", Constant.getLastName());
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
Log.d("GET CLIENT ","RES: "+parameter.toString());
        //System.out.println("VeneItemFragement-->"+url);
        Log.d(TAG, url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, parameter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                hidepDialog();
                // Parsing json object response response will be a json object
                if (response != null) {

                    String cl_id = SafeParser.getString(response, "id", "");
                    if (cl_id.length() > 0) {
                        clientID = cl_id;
                        return;
                    }
                    parseClient(response);





                } else {

                    Toast.makeText(getActivity(), "Purchase failed, Try again later!", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


               // Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                String json;
                Boolean isValid=false;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);

                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);
                                parseClient(jsonObject);
                                isValid=true;
                                Log.d(TAG, jsonObject.toString());

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
                else
                {
                    if(error!=null)
                    Log.d("GET CLIENT ","ERR: "+error.getMessage());
                    else
                        Log.d("GET CLIENT ", "ERR: is NULL ");
                    Toast.makeText(getActivity(),"Unable to process your request, Try again later!", Toast.LENGTH_SHORT).show();
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


    private void doPayPortRequest(int val ){


        String url = Constant.HOST + "api/v1/payment/create";


        JSONObject parameter = null;
        try {
            if (isGift == true || isVoucher == true) {


                parameter = createStructVoucher(val);

            } else {

                parameter = getNormalRequestObject(val);

            }


        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        if (parameter == null) return;
        showpDialog();

        //System.out.println("VeneItemFragement-->"+url);
        Log.d("PAY FORT", url);
        Log.d("PAY FORT", parameter.toString());

        final String requestBody = parameter.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // do something...
                hidepDialog();
                KalendriaAppController.getInstance().HTMLString=response;
                Log.d("PAY PORT", response);
                Intent intent = new Intent(getActivity(), PayFortWebView.class);
                intent.putExtra("isOrderKey", true);
                startActivity(intent);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // do something...
                Toast.makeText(getActivity(), "Purchase failed, Try again later!", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                hidepDialog();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        KalendriaAppController.getInstance().addToRequestQueue(stringRequest);



    }


    String TAG = "PaymentFragment";

    public void doPay(final int val) {



        String url = Constant.HOST + "api/v1/order";
        JSONObject parameter = null;
        try {
            if(isGift == true || isVoucher == true){


                parameter = createStructVoucher(val);

            }else{

                parameter = getNormalRequestObject(val);

            }


        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        if(parameter==null) return;
        showpDialog();

        //System.out.println("VeneItemFragement-->"+url);
        Log.d(TAG, url);
        Log.d(TAG, parameter.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, parameter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                hidepDialog();
                // Parsing json object response response will be a json object
                if (response != null) {


                    AddToCardSingleTone singleTone = AddToCardSingleTone.getInstance();
                    singleTone.addToCardArrayList.remove(selectedAddToCardVenueModel);

                    String resultMessage = null;
                    if (val == 1) {
                        //Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);
                        resultMessage = "Appointment booked successfully. You chose to pay at the venue.";

                        showOrderPage();
                    } else if (val == 2) {
                        resultMessage = "Appointment booked successfully.";
                        showOrderPage();
                        //Online

                    } else if (val == 3) {
                        resultMessage = "Appointment booked successfully.";

                        String wallet = Constant.getWallet();
                        //   let totAmount = self.dictBusiness.valueForKey("totAmt") as? String

                        int walletVal =0;
                        try{
                            walletVal=Integer.parseInt(wallet);
                        }
                        catch (Exception ex){ex.printStackTrace();}

                        int totAmtVal = totAmtPaid;

                        int newWalletVal = walletVal - totAmtVal;
                        Constant.setWallet(newWalletVal + "");


                         updateUserData("credit", newWalletVal+"");


                        if (isGift == true) {

                            updateGiftValue();

                        }

                    } else if (val == 4) {



                        String wallet = Constant.getPoints();
                        int totAmtVal =  totOriginalAmt;


                        int walletVal =0;
                        try{
                            walletVal=  Integer.parseInt(wallet);
                        } catch (Exception ex){ex.printStackTrace();}

                        int newWalletVal = walletVal - totAmtVal;
                        Constant.setPoints(newWalletVal + "");

                        updateUserData("points", newWalletVal+"");
                        resultMessage = "Appointment booked successfully.";


                    } else if (val == 5) {

                        resultMessage = "Appointment booked successfully.";
                        if (isGift == true) {
                            updateGiftValue();
                        }
                        else
                        showOrderPage();
                    }

                    Toast.makeText(getActivity(), resultMessage, Toast.LENGTH_SHORT).show();

                    try {


                        if (response.has("id")) {
                            String orderId = response.getString("id");
                            Log.d("PAYMENT", "Ordered ID : " + orderId);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                } else {

                    Toast.makeText(getActivity(), "Purchase failed, Try again later!", Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


                Toast.makeText(getActivity(),"Unable to process your request, please try again", Toast.LENGTH_SHORT).show();
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



                // hide the progress dialog

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

    private JSONObject createStructVoucher( int val) {

        JSONObject dictMainService = new JSONObject();

        try {


            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            int totDiscount = 0;
            int totOriginalPrice = 0;
            int totalAmtPaid = 0;
            String totEndTime = "";
            String totStartTime = "";


            JSONArray arrServ = new JSONArray();

            String business = selectedAddToCardVenueModel.getVenueID();
            //dictBusiness.valueForKey("business") as? String
            int client = 0;
            try {
                client = Integer.parseInt(clientID);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            String sector = selectedAddToCardVenueModel.getUserSector();


            String how_often = "";
            String is_iron = "";


            if (sector.equalsIgnoreCase("2")) {
                how_often = selectedAddToCardVenueModel.getHow_often();
                is_iron = selectedAddToCardVenueModel.getIs_iron();
            } else {
                how_often = "Just Once";
                is_iron = "false";
            }


            long order_Id = System.currentTimeMillis();

            Log.e("Payment", "Order ID:" + order_Id);

            try {


                dictMainService.put("order_id", order_Id);
                dictMainService.put("business", business);
                dictMainService.put("client", client);
                dictMainService.put("createdBy", Constant.getUserId());
                dictMainService.put("customer", Constant.getUserId());
                dictMainService.put("created_from", "customer");
                dictMainService.put("customer_email", Constant.getEmail());
                dictMainService.put("customer_name", Constant.getFirstName());
                dictMainService.put("customer_phone", Constant.getPhone());

                dictMainService.put("how_often", how_often);
                dictMainService.put("is_iron", is_iron);


                dictMainService.put("sector", sector);
                dictMainService.put("status", "Confirmed");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            KACoupen gift = selectedAddToCardVenueModel.gift;
            KACoupen promo = selectedAddToCardVenueModel.coupen;


            //let dictGift = dictBusiness.valueForKey("Gift") as? NSMutableDictionary

            //let dictPromo = dictBusiness.valueForKey("Promo") as? NSMutableDictionary

            int voucherValue = 0;
            int giftValue = 0;


            if (isGift == true) {

                if (gift != null) {
                    giftValue = gift.value;

                    int GiftVocherId = gift.voucherId;
                    if (GiftVocherId != 0) {
                        try {
                            dictMainService.put("gift", GiftVocherId);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }
                }

            }
            if (isVoucher == true) {
                if (promo != null) {
                    voucherValue = promo.value;


                }
            }


            for (int i = 0; i < arrServiceList.size(); i++) {

                try {


                    AddToCardServiceModel dictSer = (AddToCardServiceModel) arrServiceList.get(i);


                    String discount = dictSer.getServiceDiscount();
                    int disInt =0;
                    try {
                         disInt = Integer.parseInt(discount);
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                    totDiscount = totDiscount + disInt;


                    String employee = dictSer.staffID;

                    // let today = NSDate()

                    String selectDate = dictSer.selectedDate;

                    Date SelDate = formatter.parse(selectDate);
                    String dateStr = formatter2.format(SelDate);


                    String timing = dictSer.selectedTime;
                    String[] arrTime = timing.split("-");
                    //timing.componentsSeparatedByString("-")

                    String startTime = "";
                    String EndTime = "";

                    if (arrTime.length > 0) {
                        startTime = arrTime[0];
                        EndTime = arrTime[1];
                    }


                    String scheduledAt = dateStr.trim() + " " + startTime.trim();

                    //scheduledAt = scheduledAt.stringByTrimmingCharactersInSet(NSCharacterSet.whitespaceCharacterSet())

                    scheduledAt = scheduledAt + ":00.0000";

                    String endAt = dateStr.trim() + " " + EndTime.trim();

                    //endAt = endAt.stringByTrimmingCharactersInSet(NSCharacterSet.whitespaceCharacterSet())

                    endAt = endAt.trim() + ":00.0000";

                    totEndTime = endAt;
                    if (i == 0) {
                        totStartTime = scheduledAt;
                    }

                    String price = dictSer.getServicePrice();
                    int priceInt=0;
                    try {
                        priceInt = Integer.parseInt(price);
                    }
                    catch (Exception ex){ex.printStackTrace();}

                    totOriginalPrice = totOriginalPrice + priceInt;


                    int giftUsedValue = 0;
                    int voucherUsedValue = 0;


                    JSONObject dictBus = new JSONObject();


                    int discountPrice = 0;
                    if (disInt != 0) {

                        double calAmt1 = (double) ((double) disInt / 100);
                        Double calAmt = calAmt1 * (double) (priceInt);
                        discountPrice = calAmt.intValue();

                        discountPrice = priceInt - discountPrice;

                    } else {
                        discountPrice = priceInt;
                    }

                    if (isGift == true) {

                        int giftId = gift.voucherId;//  dictGift?.valueForKey("voucherId") as? String
                        int giftCoupon = gift.couponId;// dictGift?.valueForKey("couponId") as? String


                        if (giftValue > 0 && discountPrice > 0) {

                            giftUsedValue = discountPrice;

                            discountPrice = discountPrice - giftValue;

                            if (discountPrice > 0) {
                                giftUsedValue = giftValue;

                            }


                            giftValue = giftValue - giftUsedValue;
                            dictBus.put("gift", giftId);
                            dictBus.put("gift_coupon", giftCoupon);
                            dictBus.put("gift_used_value", giftUsedValue);


                        } else if (giftValue <= 0) {
                            dictBus.put("gift", giftId);
                            dictBus.put("gift_coupon", giftCoupon);
                            dictBus.put("gift_used_value", "0");
                        }


                    }

                    if (isVoucher == true) {


                        int voucherId = promo.voucherId;// dictPromo?.valueForKey("voucherId") as? String
                        int voucherCounpon = promo.couponId;// dictPromo?.valueForKey("couponId") as? String


                        if (voucherValue > 0 && discountPrice > 0) {

                            if (promo.discount_type.equalsIgnoreCase("cash")) {

                                voucherUsedValue = discountPrice;

                                discountPrice = discountPrice - voucherValue;


                                if (discountPrice > 0) {
                                    voucherUsedValue = voucherValue;
                                }

                                voucherValue = voucherValue - voucherUsedValue;

                            } else {

                                double calAmt1 = ((double) (voucherValue) / 100);
                                Double calAmt = calAmt1 * (double) (discountPrice);
                                int discountAmount = calAmt.intValue();

                                voucherUsedValue = discountPrice;

                                discountPrice = discountPrice - discountAmount;






                                if (discountPrice > 0) {
                                    voucherUsedValue = discountAmount;
                                }
                            }


                            dictBus.put("voucher", voucherId);
                            dictBus.put("voucher_coupon", voucherCounpon);
                            dictBus.put("voucher_used_value", voucherUsedValue);


                        } else if (voucherValue <= 0) {

                            dictBus.put("voucher", voucherId);
                            dictBus.put("voucher_coupon", voucherCounpon);
                            dictBus.put("voucher_used_value", "0");

                        }

                    }

                    Log.d("Pay Fragment", "Discount:" + discountPrice);

                    if (val == 2 || val == 3) {

                        if (discountPrice < 0) {
                            dictBus.put("amount_paid", "0");

                        } else {

                            dictBus.put("amount_paid", "" + discountPrice);
                            totalAmtPaid = totalAmtPaid + discountPrice;
                        }

                    }


                    String service = dictSer.getServiceId2();
                    String servcie_name = dictSer.getServiceName();

                    if (val == 4) {
                        String points = dictSer.getServicePoints();
                        dictBus.put("points", points);

                    }

                    dictBus.put("business", business);
                    dictBus.put("client", client);
                    dictBus.put("createdBy", Constant.getUserId());
                    dictBus.put("customer", Constant.getUserId());
                    dictBus.put("created_from", "customer");
                    dictBus.put("customer_email", Constant.getEmail());
                    dictBus.put("customer_name", Constant.getFirstName());
                    dictBus.put("customer_phone", Constant.getPhone());

                    dictBus.put("employee", employee);
                    dictBus.put("endAt", endAt);
                    dictBus.put("endTime", EndTime);
                    dictBus.put("how_often", how_often);
                    dictBus.put("is_iron", is_iron);

                    if (val == 1) {
                        dictBus.put("pay_at", "cash");
                        dictBus.put("discount", discount);
                        dictBus.put("price", price);
                    } else if (val == 3) {
                        dictBus.put("pay_at", "wallet");
                        dictBus.put("discount", discount);
                        dictBus.put("price", price);
                        dictBus.put("is_paid", true);
                    } else if (val == 4) {
                        dictBus.put("pay_at", "points");

                    } else if (val == 2) {
                        dictBus.put("pay_at", "card");
                        dictBus.put("discount", discount);
                        dictBus.put("price", price);
                        dictBus.put("is_paid", true);
                    } else if (val == 5) {

                        dictBus.put("pay_at", "gift_voucher");
                        dictBus.put("discount", discount);
                        dictBus.put("price", price);
                        dictBus.put("is_paid", true);

                    }


                    dictBus.put("scheduledAt", scheduledAt);
                    dictBus.put("sector", sector);
                    dictBus.put("service", service);
                    dictBus.put("service_name", servcie_name);
                    dictBus.put("startTime", startTime);
                    dictBus.put("status", "Confirmed");
                    dictBus.put("time", startTime);

                    arrServ.put(dictBus);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


            try {


                if (promo != null) {
                  int  voucherFullValue = promo.value;
                    int promoId = promo.voucherId;
                    if (promoId > 0) {
                        if(voucherFullValue == voucherValue)
                        dictMainService.put("voucherId", "" + promoId);
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            if (giftValue <= 0) {
                giftRemainValue = 0;
            } else {
                giftRemainValue = giftValue;
            }

//totalAmtPaid = selectedAddToCardVenueModel.discountedNetPayable;
            dictMainService.put("platform", "Ios");
            dictMainService.put("endAt", totEndTime);
            dictMainService.put("scheduledAt", totStartTime);
            dictMainService.put("bookings", arrServ);
            dictMainService.put("discount", totDiscount);
            if (val == 1) {
                dictMainService.put("pay_at", "cash");
                dictMainService.put("price", totOriginalPrice);

            } else if (val == 3) {
                dictMainService.put("is_paid", true);
                dictMainService.put("pay_at", "wallet");
                dictMainService.put("price", totOriginalPrice);
                dictMainService.put("total_amount_paid", totalAmtPaid);

            } else if (val == 4) {
                dictMainService.put("is_paid", true);
                dictMainService.put("pay_at", "points");
                dictMainService.put("discount", "0");
                dictMainService.put("price", "0");
                dictMainService.put("points", selectedAddToCardVenueModel.totAmtPaid);
            } else if (val == 5) {
                dictMainService.put("is_paid", true);
                dictMainService.put("pay_at", "gift_voucher");
                dictMainService.put("price", totOriginalPrice);
                dictMainService.put("total_amount_paid", totalAmtPaid);

            }
            else if (val == 2) {

                dictMainService.put("is_paid", true);
                dictMainService.put("pay_at", "card");
                dictMainService.put("price", totOriginalPrice);
                dictMainService.put("total_amount_paid", totalAmtPaid);





                JSONObject dictPayfort = new JSONObject();
                if (Constant.isLiveBuild) {
                    dictPayfort.put("access_code", "lGQLYLUl7MMRk3NcGVqn");
                    dictPayfort.put("merchant_identifier", "jxekSPTl");
                }
                else {
                    dictPayfort.put("access_code", "QsCnIFWy1cHVZ1KVfPI9");
                    dictPayfort.put("merchant_identifier","epsBcBYT" );
                }
                dictPayfort.put("command","PURCHASE" );
                dictPayfort.put("currency","AED");
                dictPayfort.put("eci","ECOMMERCE");
                dictPayfort.put("language","en");


                int padiAmt = totalAmtPaid * 100;

                dictPayfort.put("amount",padiAmt);


                dictPayfort.put("customer_email",Constant.getEmail());
                dictPayfort.put("customer_name",Constant.getFirstName());
                dictPayfort.put("merchant_reference",order_Id);
                dictPayfort.put("order_description",order_Id);


                String returnUrl =Constant.HOST+"api/v1/payment/execute?provider=payfort&type=order&merchant_reference=" + order_Id;

                dictPayfort.put("return_url", returnUrl);

                JSONObject dictMainServicePayFort = new JSONObject();
                dictMainServicePayFort.put("platform","mobile");
                dictMainServicePayFort.put("type", "order");
                dictMainServicePayFort.put("provider", "payfort");
                dictMainServicePayFort.put("payfort",dictPayfort);
                dictMainServicePayFort.put("data",dictMainService);




               if(isGift == true){

                   JSONObject dictGift = KalendriaAppController.getInstance().dictGift;
                   JSONObject dictcoupon =SafeParser.getObject(dictGift, "coupon");

                   dictcoupon.put("value",giftRemainValue);
                   dictMainService.put( "gift",dictGift);

                }
                if(isVoucher == true){

                    JSONObject dictPromo = KalendriaAppController.getInstance().dictPromo;
                    dictMainService.put("voucher",dictPromo);
                }


                return dictMainServicePayFort;

            }

            else {
                dictMainService.put("is_paid", true);
                dictMainService.put("pay_at", "wallet");
                dictMainService.put("price", totOriginalPrice);
                dictMainService.put("total_amount_paid", totalAmtPaid);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return dictMainService;

    }


}



