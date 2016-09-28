package com.kalendria.kalendria.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;
import com.kalendria.kalendria.utility.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Login extends FragmentActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button singup_btn,login_btn,password, google_Login;
    EditText mybrands_totalPoints_text_textview,password_et;
    TextView forgot_password_txt,singup_txt;
    LoginButton login;
    private ProgressDialog pDialog;
    JSONObject gcm_device_id = null;
    public static String Tag = Login.class.getSimpleName();
    private SharedPreferences sharedPref;
    GoogleApiClient mGoogleApiClient;

    //Fb Login
    LoginButton login_facebook_button;
    CallbackManager callbackManager;
    Button fb;



    /*Google plus*/

    //Signin button
    private SignInButton signInButton;


    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Fb Login start
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        //Fb Login end
        setContentView(R.layout.main);


        init();
        onClick_button();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        sharedPref = getApplicationContext().getSharedPreferences(Constant.MyPREFERENCES,0);


        //Initializing signinbutton
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(KalendriaAppController.getInstance().gso.getScopeArray());


//Initializing google api client

        mGoogleApiClient =  KalendriaAppController.getInstance().getGoogleClient();

        //Setting onclick listener to signing button
        signInButton.setOnClickListener(this);

        //Fb login Methed start==

        callbackManager = CallbackManager.Factory.create();
        fb = (Button) findViewById(R.id.fb);
        // Social Buttons
        login_facebook_button = (LoginButton) findViewById(R.id.login_facebook_button);


        login_facebook_button.setReadPermissions(Arrays.asList("public_profile","email"));
        login_facebook_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("onSuccess", "--------" + loginResult.getAccessToken());
                Log.e("Token", "--------" + loginResult.getAccessToken().getToken());
                Log.e("Permision", "--------" + loginResult.getRecentlyGrantedPermissions());

                Log.e("OnGraph", "------------------------");
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                Log.e("GraphResponse", "-------------" + object.toString());

                                try {

                                    if (object != null) {
                                        String id = object.getString("id");
                                       /* try {
                                            URL profile_pic = new URL("http://graph.facebook.com/"+id+"/picture?type=large");
                                            Log.i("profile_pic",profile_pic + "");

                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }*/
                                        String name = SafeParser.getString(object, "name", "");
                                        String email = SafeParser.getString(object, "email", "");
                                        //System.out.println("name"+"  " +name +" "  + email +" " + id);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("fbid", SafeParser.getString(object, "id", ""));
                                        editor.putString("fbfirst_name", SafeParser.getString(object, "first_name", ""));
                                        editor.putString("fblast_name", SafeParser.getString(object, "last_name", ""));
                                        editor.putString("fbemail", SafeParser.getString(object, "email", ""));
                                        editor.putString("fbgender", SafeParser.getString(object, "gender", ""));
                                        editor.commit();
                                        //String url = Constant.LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_FB + id;
                                       // checkBySocialMedia(url, "fb");


                                        if(email!=null && email.length()>0){
                                            String url=Constant.LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_EMAIL+email;
                                            checkBySocialMedia(url,"fb");
                                        }else{
                                            String url=Constant.LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_FB+id;
                                            checkBySocialMedia(url,"fb");
                                        }




                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                //=====above method is used to get the fb values ==//

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,first_name,last_name,location,locale,timezone");
                request.setParameters(parameters);
                request.executeAsync();


                Log.e("Total Friend in List", "----------------------");
                new GraphRequest(loginResult.getAccessToken(), "/me/friends", null, HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {

                                Log.e("Friend in List", "-------------" + response.toString());
                            }
                        }
                ).executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("FacebookException", "-------------" + exception.toString());
            }
        });


        KalendriaAppController.hideStatusBar(false,this);

    }





    @Override
    protected void onActivityResult(int requestCode, int responseCode,Intent data) {
        super.onActivityResult(requestCode, responseCode, data);



        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
        else
            callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    public void onClick(View v) {
        if (v == fb) {
            LoginManager.getInstance().logOut();
            login_facebook_button.performClick();
        }

        if (v == google_Login) {
            //Calling signin
            signInButton.setOnClickListener(this);
            signIn();
        }
    }
    private void init(){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        singup_btn= (Button) findViewById(R.id.singup_btn);
        login_btn= (Button) findViewById(R.id.login_btn);
        mybrands_totalPoints_text_textview=(EditText)findViewById(R.id.mybrands_totalPoints_text_textview);
        password_et=(EditText)findViewById(R.id.password_ed);

        forgot_password_txt=(TextView)findViewById(R.id.forgot_password_txt);

        google_Login = (Button)findViewById(R.id.google_pluse);


    }


    private void onClick_button(){
        singup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String   email = mybrands_totalPoints_text_textview.getText().toString().trim();
                String  passcode = password_et.getText().toString().trim();


                if(TextUtils.isEmpty(email) )
                {
                    mybrands_totalPoints_text_textview.setError("Please enter a valid email id");
                    mybrands_totalPoints_text_textview.requestFocus();
                }else if(!Validator.isEmailValid(email)){
                    mybrands_totalPoints_text_textview.setError("Please enter a valid email id");
                    mybrands_totalPoints_text_textview.requestFocus();
                }else if(passcode.isEmpty()){
                    password_et.setError("Please enter Your Password!");
                    password_et.requestFocus();
                }
                else {

                    View view = Login.this.getWindow().getCurrentFocus();
                    if (view!= null) {
                        InputMethodManager imm = (InputMethodManager) Login.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                    if (KalendriaAppController.getInstance().isNetworkConnected(Login.this)) {
                        makeJsonObjectRequest(email,passcode);
                    }else{
                        Toast.makeText(getApplicationContext(),"Please Check your internet connection",Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        forgot_password_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });



    }



    private void makeJsonObjectRequest(final String email,String password) {



        try {
            gcm_device_id = new JSONObject();
            gcm_device_id.put("identifier", email);
            gcm_device_id.put("password", password);
            System.out.println("dfddf" + gcm_device_id);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constant.LOGIN_URL, gcm_device_id, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(Tag, response.toString());

                System.out.println("login"+response.toString());


                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        String  code = response.getString("isVerified");

                        //code="true";
                        if(!code.equalsIgnoreCase("false")){

                            String  id = response.getString("id");
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userId", response.getString("id"));

                            editor.commit();

                            String gender = response.getString("gender");
                            String phone = response.getString("phone");
                            String address = response.getString("address");
                            String points = response.getString("points");
                            String wallets = response.getString("credit");

                            Constant.setEmail(email);
                            Constant.setFirstName(SafeParser.getString(response, "first_name", ""));
                            Constant.setFirstName(SafeParser.getString(response, "last_name", ""));
                            Constant.setPoints(SafeParser.getString(response, "points", "0"));

                            Constant.savedData(gender, "kGenderKey");
                            Constant.savedData(address, "kAddressKey");
                            Constant.savedData(phone, "kphoneKey");
                            Constant.savedData(points, "kLoyalityKey");
                            Constant.savedData(wallets, "kWalletsKey");

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
                            if(Constant.getUserId(getApplicationContext())!=null){
                                Intent newIntent = new Intent(getApplicationContext(), DashBoard.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(newIntent);
                            }else{
                                Toast.makeText(Login.this, "Error. userID", Toast.LENGTH_SHORT).show();
                            }

                        }else{

                            // Toast.makeText(getApplicationContext(), "Please check your mail to verify your mail", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(Login.this);
                            dlgAlert.setMessage("Your account has not yet been activated. Please click the verfication link sent to your registered email id ");
                            dlgAlert.setTitle("Kalendria ");
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

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);

                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);

                                String message = jsonObject.getString("message");
                                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Login.this);
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Kalendria ");
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
                        Log.e("Error 111", e.getMessage());
                    }
                }
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


    private void checkBySocialMedia(String url,final String key) {
        showpDialog();
        System.out.println("check social media by email and fb_id-->" + url);
        JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray responseArr) {
                Log.d(Tag, responseArr.toString());
                hidepDialog();

                try {

                    if(responseArr.length()>0)
                    {
                        JSONObject response = (JSONObject)responseArr.get(0);
                        String  code = response.getString("isVerified");
                        String  id = response.getString("id");

                        updateFB_ID(id,key);
                        String email = response.getString("email");
                        code="true";// we are hardcoding this
                        if(!code.equalsIgnoreCase("false")){


                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userId", response.getString("id"));

                            editor.commit();

                            String gender = response.getString("gender");
                            String phone = response.getString("phone");
                            String address = response.getString("address");
                            String points = response.getString("points");
                            String wallets = response.getString("credit");


                            Constant.setEmail(email);
                            Constant.setFirstName(SafeParser.getString(response, "first_name", ""));
                            Constant.setLastName(SafeParser.getString(response, "last_name", ""));
                            Constant.setPoints(SafeParser.getString(response, "points", "0"));

                            Constant.savedData(gender, "kGenderKey");
                            Constant.savedData(address, "kAddressKey");
                            Constant.savedData(phone, "kphoneKey");
                            Constant.savedData(points, "kLoyalityKey");
                            Constant.savedData(wallets, "kWalletsKey");

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
                            if(Constant.getUserId(getApplicationContext())!=null){
                                Intent newIntent = new Intent(getApplicationContext(), DashBoard.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(newIntent);
                            }else{
                                Toast.makeText(Login.this, "Error. userID", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                    else {
                        Intent intent = new Intent(Login.this,  Register.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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


    /*Gplus update token id in progres waitting for service*/
    private void updateFB_ID(String userid, String key) {

        String url = Constant.HOST + "api/v1/user/" + userid;
        JSONObject parameter = new JSONObject();
        try {
            parameter.put("isVerified", "true");
            if (key.equalsIgnoreCase("fb")) {

                parameter.put("facebook", Constant.getfbID(this));
            } else {
                parameter.put("google", Constant.getGPlusID(this));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d("LOGIN","PARA :"+parameter.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, parameter, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(Tag, response.toString());
                if (response != null) {
                    /*
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("fbid", "");
                    editor.putString("fbfirst_name", "");
                    editor.putString("fblast_name", "");
                    editor.putString("fbemail", "");
                    editor.putString("fbgender", "");
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                    startActivity(intent);
                    */
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);

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

/*Google pluse*/

    //This function will option signing intent
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(KalendriaAppController.getInstance().getGoogleClient());
        startActivityForResult(signInIntent, RC_SIGN_IN);



    }



    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

            if(KalendriaAppController.getInstance().getGoogleClient().isConnected())
            {
                Log.d("TEST","Data connected");
            }
            //Displaying name and email
            System.out.println("ID -------------> " + acct.getId());
           // System.out.println("Token -------------> "+ acct.getIdToken());
            //System.out.println("ServerAuthCode -------------> "+ acct.getServerAuthCode());
            System.out.println("DisplayName -------------> " + acct.getDisplayName());
            System.out.println("Email -------------> " + acct.getEmail());

            String   name=(acct.getDisplayName());
            String   email=(acct.getEmail());
            String   id=(acct.getId());

            String fname ="";
            String lname ="";
            String names[] = name.split(" ");
            if(names.length>1)
            {
                fname=names[0];
                lname=names[1];
            }
            else fname=name;

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("gplusID", id);
            editor.putString("gplusemail", email);
            editor.putString("gplusfname", fname);
            editor.putString("gpluslname", lname);

            editor.commit();
            /*
            if(email!=null){
                String url=Constant.LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_GPLUS+email;
                checkBySocialMedia(url,"gplus");
            }else{
                String url=Constant.LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_GPLUS+id;
                checkBySocialMedia(url,"gplua");
            }
            */
            String url=Constant.LOGIN_URL_CHECK_SOCIAL_MEDIA_BY_GPLUS+id;
            checkBySocialMedia(url,"gplua");


        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
