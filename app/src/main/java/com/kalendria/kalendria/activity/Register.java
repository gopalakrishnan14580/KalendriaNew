package com.kalendria.kalendria.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.adapter.RegisterSpinner;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.utility.CommonSingleton;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;
import com.kalendria.kalendria.utility.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by murugan on 17/02/16.
 */
public class Register extends Activity {
    private ProgressDialog pDialog;
    Button register_back_btn,register_register_btn;
    TextView terms_condition_text, cityText;
    EditText register_email_et,register_username_et,register_phone_et,register_password_et,register_re_password_et,register_lastname_et,register_address_et;
    //Spinner cityText;
    RadioGroup register_radiogroup;

    //RadioButton male_Button, female_Button;

    boolean isSocial=false;
    int type =3;

    public static String Tag = Register.class.getSimpleName();
    ArrayList<com.kalendria.kalendria.model.RegisterSpinner> cityModelArray =new ArrayList<com.kalendria.kalendria.model.RegisterSpinner>();
    RegisterSpinner adapter1;
    JSONObject gcm_device_id = null;
    private SharedPreferences sharedPref;

    int pos;
    String radiogroup_value;
    String spinner_selected_id="",spinner_name="",spinner_type="",spinner_parent="";
    List<String> cityTextArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
        onClick_button();
        radiogroup_value = "Male";
        get_radio_group_values();
        if(KalendriaAppController.isNetworkConnected(getApplicationContext())){
            getCityList();
        }else{
            Toast.makeText(Register.this, "Plese check your Internet!", Toast.LENGTH_SHORT).show();
        }

        sharedPref = getApplicationContext().getSharedPreferences(Constant.MyPREFERENCES,0);


        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String key = bundle.getString("key");
                if (key != null) {
                    isSocial = true;
                    if (key.equalsIgnoreCase("fb")) {
                        type = 1;
                        register_email_et.setText(Constant.getfbEmail(getApplicationContext()));
                        register_username_et.setText(Constant.getfbFirstName(getApplicationContext()));
                        register_lastname_et.setText(Constant.getfbLastName(getApplicationContext()));
                    } else {
                        type = 2;
                        register_email_et.setText(Constant.getGplusEmail(getApplicationContext()));
                        register_username_et.setText(Constant.getgPlusFirstName(getApplicationContext()));
                        register_lastname_et.setText(Constant.getgPlusLastName(getApplicationContext()));
                        // register_lastname_et.setText(Constant.getfbLastName(getApplicationContext()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void init(){
        pDialog = new ProgressDialog(Register.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        terms_condition_text=(TextView)findViewById(R.id.terms_condition_text);
        register_email_et=(EditText)findViewById(R.id.register_email_et);
        register_username_et=(EditText)findViewById(R.id.register_username_et);
        register_phone_et=(EditText)findViewById(R.id.register_phone_et);
        register_password_et=(EditText)findViewById(R.id.register_password_et);
        register_re_password_et=(EditText)findViewById(R.id.register_re_password_et);
        register_lastname_et=(EditText)findViewById(R.id.register_lastname_et);
        cityText =(TextView) findViewById(R.id.register_spinner);
        register_radiogroup=(RadioGroup)findViewById(R.id.register_radiogroup);
        register_back_btn=(Button)findViewById(R.id.register_back_btn);
        register_register_btn=(Button)findViewById(R.id.register_register_btn);
        register_address_et=(EditText)findViewById(R.id.register_address_et);

        //male_Button = (RadioButton)findViewById(R.id.male_radio_btn);
        //female_Button = (RadioButton)findViewById(R.id.female_radio_btn);


    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void get_radio_group_values() {


        register_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected


                RadioButton mbutton = (RadioButton) group.getChildAt(0);
                if (mbutton.isChecked()) {
                    radiogroup_value = "Male";
                } else
                    radiogroup_value = "Female";



            }

        });


    }
    private void onClick_button(){

        cityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cityTextArray==null) {
                    cityTextArray = CommonSingleton.getInstance().getCityList();
                    cityModelArray=CommonSingleton.getInstance().getCityMode();
                }

                if(cityTextArray==null)
                {
                    getCityList();
                    return;
                }
                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_dropdown_item, cityTextArray);

                new AlertDialog.Builder(Register.this)
                        .setTitle("City")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                cityText.setText(cityTextArray.get(which).toString());
                                spinner_selected_id= cityModelArray.get(which).getId();
                                spinner_name= cityModelArray.get(which).getName();
                                spinner_type= cityModelArray.get(which).getType();
                                spinner_parent= cityModelArray.get(which).getParent();
                                //String imc_met = cityText.getSelectedItem().toString();

                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });
        register_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        terms_condition_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Register.this,  TermsConditions.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        register_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = register_email_et.getText().toString().trim();
                String username = register_username_et.getText().toString().trim();
                String register_lastname = register_lastname_et.getText().toString().trim();
                String phone = register_phone_et.getText().toString().trim();
                String password = register_password_et.getText().toString().trim();
                String repassword = register_re_password_et.getText().toString().trim();
                String address = register_address_et.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    register_email_et.setError("Please enter a valid email id");
                    register_email_et.requestFocus();
                } else if (!Validator.isEmailValid(email)) {
                    register_email_et.setError("Please enter a valid email id");
                    register_email_et.requestFocus();
                } else if (TextUtils.isEmpty(username)) {
                    register_username_et.setError("Please enter your first name");
                    register_username_et.requestFocus();
                } else if (TextUtils.isEmpty(register_lastname)) {
                    register_lastname_et.setError("Please enter your last name");
                    register_lastname_et.requestFocus();
                } else if (spinner_selected_id.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select your city", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(phone) || phone.length() < 9) {
                    register_phone_et.setError("Your phone number should have minimum 9 number");
                    register_phone_et.requestFocus();
                } else if (TextUtils.isEmpty(password) || password.length() < 8) {
                    register_password_et.setError("Your password should have minimum 8 character");
                    register_password_et.requestFocus();
                } else if (TextUtils.isEmpty(repassword) || password.length() < 8) {
                    register_re_password_et.setError("Please enter your confirm password!");
                    register_re_password_et.requestFocus();
                } else if (!password.equals(repassword)) {
                    register_re_password_et.setError("Password does not match!");
                    register_re_password_et.requestFocus();
                }
              /*  else if(TextUtils.isEmpty(address)){
                    register_address_et.setError("Please enter Your First Name");
                    register_address_et.requestFocus();
                }*/

                else {

                    if (KalendriaAppController.getInstance().isNetworkConnected(Register.this)) {
                        makeJsonObjectRequest(email, username, register_lastname, spinner_selected_id, radiogroup_value, phone, password, address);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Check your internet connection", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }

    private void makeJsonObjectRequest(String email,String username,String register_lastname, String spinner_id,String gender_name,String phone,String password,String address) {

        JSONObject parentData = new JSONObject();
        JSONObject childData = new JSONObject();
        JSONObject childData1 = new JSONObject();

        try {

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String lastLogin = format.format(cal.getTime()) +"+0000";

            parentData.put("email", email);
            parentData.put("first_name",username);
            parentData.put("last_name",register_lastname);
            parentData.put("password",password);
            parentData.put("confirm_password",password);
            parentData.put("phone",phone);
            parentData.put("gender",gender_name);
            parentData.put("last_login", lastLogin);
            parentData.put("type","customer");
            parentData.put("address",address);

            if(!Constant.getfbID(getApplicationContext()).isEmpty() ||!Constant.getfbEmail(getApplicationContext()).isEmpty()){
                parentData.put("isVerified","true");
            }

            if(isSocial == true){


                if(type == 1){
                    parentData.put("facebook", Constant.getfbID(getApplicationContext()));

                }else if(type == 2){
                    parentData.put("google", Constant.getGPlusID(getApplicationContext()));

                }
                parentData.put("isVerified","true");
            }

            childData.put("createdAt", "");
            childData.put("id",spinner_id);
            childData.put("lat","");
            childData.put("long","");
            childData.put("name",spinner_name);
            childData.put("parent",spinner_parent);
            childData.put("type",spinner_type);
            childData.put("updatedAt","");
            parentData.put("city", childData);

            childData1.put("business", "0");
            childData1.put("createdAt","");
            childData1.put("description","");
            childData1.put("id","3");
            childData1.put("isDeleted","false");
            childData1.put("name","Customer");
            childData1.put("type","Mobile");
            childData1.put("updatedAt","");
            parentData.put("role", childData1);
            System.out.println("Register json object-->"+parentData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constant.REGISTER, parentData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(Tag, response.toString());


                try{
                    hidepDialog();
                    if(response!=null)
                    {

                        JSONObject dictData = SafeParser.getObject(response,"data");
                        String code = SafeParser.getString(response, "code", "");

                        if(dictData != null && code != null && code == "E_VALIDATION"){
                            JSONArray arrEmail =response.getJSONArray("email");
                                    //SafeParser.getString(response, "email", "");


                            if(arrEmail != null){
                                //  let dictMsg = arrEmail?.objectAtIndex(0) as? NSMutableDictionary
                                Toast.makeText(Register.this, "User email already exists. Please use another emai", Toast.LENGTH_SHORT).show();

                            }
                        }else{
                            if(response.names().length() > 5){
                                boolean isVerified =SafeParser.getBoolen(response, "isVerified", false);

                                if(isVerified == true){


                                    int city = SafeParser.getInt(response, "city", 3);

                                    String address = SafeParser.getString(response, "address", "");
                                    String zipCode = SafeParser.getString(response, "zip", "");
                                    String phone = SafeParser.getString(response, "phone", "");
                                    String points = SafeParser.getString(response, "points", "");
                                    String wallets = SafeParser.getString(response, "credit", "");
                                    String id = SafeParser.getString(response, "id", "");
                                    String email = SafeParser.getString(response, "email", "");
                                    String firstName = SafeParser.getString(response, "first_name", "");
                                    String lastName = SafeParser.getString(response, "last_name", "");


                                    Constant.setCity("" + city);
                                    Constant.setAddress(address);
                                    Constant.setZipCode(zipCode);
                                    Constant.setPhone(phone);
                                    Constant.savedData(phone, "kphoneKey");
                                    Constant.savedData(points, "kLoyalityKey");
                                    Constant.savedData(wallets, "kWalletsKey");
                                    Constant.setEmail(email);
                                    Constant.setFirstName(firstName);
                                    Constant.setLastName(lastName);

                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("userId", id);

                                    editor.commit();


                                    Intent newIntent = new Intent(getApplicationContext(), DashBoard.class);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(newIntent);


                                }else{

                                    if(!Constant.getfbID(getApplicationContext()).isEmpty()){
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("userId", response.getString("id"));
                                        editor.putString("fbid", "");
                                        editor.putString("fbfirst_name", "");
                                        editor.putString("fblast_name", "");
                                        editor.putString("fbemail", "");
                                        editor.putString("fbgender", "");
                                        editor.commit();
                                        Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                                        startActivity(intent);
                                    }

                                    newUserRegistred();
                                    //Toast.makeText(Register.this, "You have received a verification email. Please verify your account. Check your junk/unwanted folder if you don't see the Kalendria email.", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }

                    }



                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


                hidepDialog();
            }


        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
               // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error 111",json);


                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject=new JSONObject(json);
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                                JSONArray jsonArray=jsonObject1.getJSONArray("email");
                                JSONObject jsonObject2=jsonArray.getJSONObject(0);
                                String message=jsonObject2.getString("message");
                               // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Register.this);
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Register ");
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
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111",e.getMessage());
                    }

                }

                hidepDialog();
            }


        }) {
           /* @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("user",userAccount.getUsername());
                params.put("pass",userAccount.getPassword());
                params.put("comment", Uri.encode(comment));
                params.put("comment_post_ID",String.valueOf(postId));
                params.put("blogId",String.valueOf(blogId));

                return params;
            }*/


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

    private void newUserRegistred()
    {
        //System.out.println("fb-->"+Constant.getfbID(getApplicationContext()));



        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Register.this);
        dlgAlert.setMessage("You have received a verification email. Please verify your account. Check your junk/unwanted folder if you don't see the Kalendria email.");
        dlgAlert.setTitle("Kalendria ");
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss the dialog
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    }
                });
        dlgAlert.setCancelable(true);

        dlgAlert.create().show();



    }
    private void getCityList() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Constant.REGISTER_SPINNER, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(Tag, response.toString());

                hidepDialog();
                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {


                    JSONArray jsonArray = response.getJSONArray("locations");
                        System.out.println("size of json array"+jsonArray.length());

                        cityTextArray = new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++) {

                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            com.kalendria.kalendria.model.RegisterSpinner homePage_model=new com.kalendria.kalendria.model.RegisterSpinner();

                            homePage_model.setType(jsonObject.getString("type"));
                            String type=jsonObject.getString("type");

                            if(type.equalsIgnoreCase("city")  )
                            {
                                homePage_model.setParent(jsonObject.getString("parent"));
                                homePage_model.setId(jsonObject.getString("id"));
                                homePage_model.setName(jsonObject.getString("name"));
                                cityTextArray.add(jsonObject.getString("name"));
                                cityModelArray.add(homePage_model);

                            }
                        }

                        /*adapter1 = new RegisterSpinner(cityModelArray,getApplicationContext());


                        cityText.setAdapter(adapter1);
                        cityText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        // TODO Auto-generated method stub
                                        cityText.setPrompt("Choose your city");
                                    }

                                    @Override
                                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                                    {
                                         int  position1 = cityText.getSelectedItemPosition();
                                        spinner_selected_id= cityModelArray.get(position1).getId();
                                        spinner_name= cityModelArray.get(position1).getName();
                                        spinner_type= cityModelArray.get(position1).getType();
                                        spinner_parent= cityModelArray.get(position1).getParent();
                                        //String imc_met = cityText.getSelectedItem().toString();

                                    }

                                });*/

                    }else{
                        // if responce is null write your commants here
                    }

                }
                catch (JSONException e)
                {

                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
               // hidepDialog();
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
