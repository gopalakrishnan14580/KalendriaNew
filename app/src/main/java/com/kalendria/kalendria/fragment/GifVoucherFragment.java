package com.kalendria.kalendria.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.PayFortWebView;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.utility.KAInputFilter;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.Validator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class GifVoucherFragment extends Fragment {

    RadioButton evoucher_rb, hardcopy_rb;
    EditText gift_voucher_amount_et, gift_voucher_who;
    EditText gift_voucher_amount_email, gift;
    private ProgressDialog pDialog;
    EditText gift_voucher_addres, gift_voucher_phone, gift_voucher_msg;
    private boolean isHardCopy = false;
    FrameLayout layout_phonenumber, layout_address, layout_emailaddress;
    ListView gift_voucher_lv;
    Button ly_bar_bottom, giftvoucher_back_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.giftvouchermain, container, false);

        evoucher_rb = (RadioButton) rootView.findViewById(R.id.evoucher_rb);
        giftvoucher_back_btn = (Button) rootView.findViewById(R.id.giftvoucher_back_btn);
        hardcopy_rb = (RadioButton) rootView.findViewById(R.id.hardcopy_rb);
        gift_voucher_amount_et = (EditText) rootView.findViewById(R.id.gift_voucher_amount_et);
        gift_voucher_who = (EditText) rootView.findViewById(R.id.gift_voucher_who);
        gift_voucher_msg = (EditText) rootView.findViewById(R.id.gift_voucher_msg);
        ly_bar_bottom = (Button) rootView.findViewById(R.id.giftvoucher_submit_bt);


        gift_voucher_amount_et.setFilters(new InputFilter[]{ new KAInputFilter("0", "20000")});
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        gift_voucher_addres = (EditText) rootView.findViewById(R.id.gift_voucher_addres);
        gift_voucher_amount_email = (EditText) rootView.findViewById(R.id.gift_voucher_amount_email);
        gift_voucher_phone = (EditText) rootView.findViewById(R.id.gift_voucher_phone);

        layout_phonenumber = (FrameLayout) rootView.findViewById(R.id.layout_phonenumber);
        layout_address = (FrameLayout) rootView.findViewById(R.id.layout_address);
        layout_emailaddress = (FrameLayout) rootView.findViewById(R.id.layout_emailaddress);

        giftvoucher_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        switchView(false);
        allActions();
        textEvents();
        validate();
        return rootView;
    }


    private void switchView(boolean isHardCopy) {
        if (isHardCopy) {
            layout_address.setVisibility(View.VISIBLE);
            layout_phonenumber.setVisibility(View.VISIBLE);
            layout_emailaddress.setVisibility(View.GONE);
            gift_voucher_msg.setNextFocusDownId(R.id.gift_voucher_addres);
        } else {
            layout_address.setVisibility(View.GONE);
            layout_phonenumber.setVisibility(View.GONE);
            layout_emailaddress.setVisibility(View.VISIBLE);
            gift_voucher_msg.setNextFocusDownId(R.id.gift_voucher_amount_email);
        }
        this.isHardCopy = isHardCopy;
    }


    private void allActions() {



        hardcopy_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    evoucher_rb.setChecked(false);
                    switchView(true);
                }
                validate();
            }
        });
        evoucher_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    hardcopy_rb.setChecked(false);
                    switchView(false);
                }
                validate();
            }
        });

        ly_bar_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPayPortRequest();
            }
        });


    }

    private void textEvents() {

        gift_voucher_addres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                validate();
            }
        });

        gift_voucher_amount_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                validate();
            }
        });

        gift_voucher_who.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                validate();
            }
        });

        gift_voucher_amount_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int amount=0;
                try {
               amount = Integer.parseInt(gift_voucher_amount_et.getText().toString());
                    if (amount > 20000)
                         gift_voucher_amount_et.setText("20000");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                validate();
            }
        });

        gift_voucher_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                validate();
            }
        });

        /*

        gift_voucher_amount_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                validate();

            }
        });

        gift_voucher_who.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                validate();

            }
        });

        gift_voucher_addres.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                validate();

            }
        });

        gift_voucher_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                validate();

            }
        });

        gift_voucher_amount_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                validate();

            }
        });
        */

    }

    private void validate() {
        boolean isValid = true;
        int amount=0;
        if (gift_voucher_amount_et.getText().toString().length() <= 0) {
            isValid = false;
        }
        else {
            try {


                 amount = Integer.parseInt(gift_voucher_amount_et.getText().toString());
                //if (amount > 20000)
               //     gift_voucher_amount_et.setText("20000");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (amount == 0) {
            isValid = false;
        }

        if (gift_voucher_who.getText().toString().length() <= 0)
            isValid = false;


        if (isHardCopy) {
            if (gift_voucher_addres.getText().toString().length() <= 0)
                isValid = false;
            if (gift_voucher_phone.getText().toString().length() <= 0)
                isValid = false;
        } else {
            if (gift_voucher_amount_email.getText().toString().length() <= 0)
                isValid = false;
           else  if (!Validator.isEmailValid(gift_voucher_amount_email.getText().toString())) {
               isValid=false;
            }
        }



        ly_bar_bottom.setEnabled(isValid);

        if (isValid)
            ly_bar_bottom.setBackgroundColor(KalendriaAppController.getResColor(R.color.colorSkyBlue));
        else
            ly_bar_bottom.setBackgroundColor(KalendriaAppController.getResColor(R.color.colorGrey));


    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private String Trim(String val) {
        return val;
    }

    private JSONObject createParam() {

        JSONObject dictMainService = new JSONObject();

        try {


            dictMainService.put("platform", "mobile");
            dictMainService.put("type", "gift");
            dictMainService.put("provider", "payfort");

            int amount = 0;
            try {
                amount = Integer.parseInt(gift_voucher_amount_et.getText().toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            JSONArray arrData = new JSONArray();

            for (int i = 0; i < 1; i++) {
                try {


                    JSONObject dictCoupon = new JSONObject();
                    dictCoupon.put("value", amount);
                    dictCoupon.put("discount_type", "cash");
                    dictCoupon.put("type", "gift");

                    JSONObject dictData = new JSONObject();
                    dictData.put("from", Constant.getUserId(getActivity()));

                    dictData.put("name", Trim(gift_voucher_who.getText().toString()));
                    dictData.put("message", Trim(gift_voucher_msg.getText().toString()));

                    dictData.put("status", "not used");

                    if (isHardCopy == false) {

                        dictData.put("type", "evoucher");
                        dictData.put("email", Trim(gift_voucher_amount_email.getText().toString()));

                    } else {

                        dictData.put("type", "courier");
                        dictData.put("address", Trim(gift_voucher_addres.getText().toString()));
                        dictData.put("phone", Trim(gift_voucher_phone.getText().toString()));

                    }

                    dictData.put("coupon", dictCoupon);


                    arrData.put(dictData);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            dictMainService.put("data", arrData);


            long order_Id = Calendar.getInstance().getTimeInMillis();

            JSONObject dictPayfort = new JSONObject();
            if (Constant.isLiveBuild) {
                dictPayfort.put("access_code", "lGQLYLUl7MMRk3NcGVqn");
                dictPayfort.put("merchant_identifier", "jxekSPTl");
            }
            else {
                dictPayfort.put("access_code", "QsCnIFWy1cHVZ1KVfPI9");
                dictPayfort.put("merchant_identifier","epsBcBYT" );
            }
            //dictPayfort.put("access_code", "QsCnIFWy1cHVZ1KVfPI9");
            dictPayfort.put("command", "PURCHASE");
            dictPayfort.put("currency", "AED");
            dictPayfort.put("eci", "ECOMMERCE");
            dictPayfort.put("language", "en");
            //dictPayfort.put("merchant_identifier", "epsBcBYT");

            int paidAmt =0;
            if (isHardCopy == false)
                paidAmt =amount * 100;
            else
                paidAmt =( amount+20) * 100;

            dictPayfort.put("amount", paidAmt);
            dictPayfort.put("customer_email", Constant.getEmail());
            dictPayfort.put("customer_name", Constant.getFirstName());
            dictPayfort.put("merchant_reference", order_Id);
            dictPayfort.put("order_description", order_Id);

            String returnUrl = Constant.HOST ; //"api/v1/payment/execute?provider=payfort&type=order&merchant_reference=" + order_Id;
            returnUrl+= "api/v1/payment/execute?provider=payfort&type=gift&merchant_reference="+order_Id;
            dictPayfort.put("return_url", returnUrl);


            dictMainService.put("payfort", dictPayfort);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dictMainService;

    }

    private void doPayPortRequest() {


        String url = Constant.HOST + "api/v1/payment/create";


        JSONObject parameter = null;
        try {


            parameter = createParam();


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
                KalendriaAppController.getInstance().HTMLString = response;
                Log.d("PAY PORT", response);
                Intent intent = new Intent(getActivity(), PayFortWebView.class);
                intent.putExtra("isOrderKey", false);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
