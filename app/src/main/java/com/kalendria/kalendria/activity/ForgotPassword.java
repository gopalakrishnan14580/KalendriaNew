package com.kalendria.kalendria.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

/**
 * Created by murugan on 18/02/16.
 */
public class ForgotPassword extends Activity {

    Button back_btn,resentpassworn_btn;
    TextView forgot_password_edittext;
    private ProgressDialog pDialog;
    JSONObject gcm_device_id = null;
    public static String Tag = Login.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.forgot_password);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        back_btn=(Button)findViewById(R.id.back_btn);
        resentpassworn_btn=(Button)findViewById(R.id.resentpassworn_btn);

        forgot_password_edittext=(EditText)findViewById(R.id.forgot_password_edittext);

        onClick_button();
    }

    private void onClick_button(){
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ForgotPassword.this,  Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        resentpassworn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String   email = forgot_password_edittext.getText().toString().trim();
                if(TextUtils.isEmpty(email) )
                {
                    forgot_password_edittext.setError("Please enter a valid email id");
                    forgot_password_edittext.requestFocus();
                }else if(!Validator.isEmailValid(email)){
                    forgot_password_edittext.setError("Please enter a valid email id");
                    forgot_password_edittext.requestFocus();
                }
                else {

                    if (KalendriaAppController.getInstance().isNetworkConnected(ForgotPassword.this)) {
                        makeJsonObjectRequest();
                    }else{
                        Toast.makeText(getApplicationContext(),"Please Check your internet connection",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }
    private void makeJsonObjectRequest() {
        try {
            gcm_device_id = new JSONObject();
            gcm_device_id.put("identifier",forgot_password_edittext.getText().toString().trim());
            System.out.println("dfddf" + gcm_device_id);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constant.FORGATPASSWORD, gcm_device_id, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(Tag, response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        String  code = response.getString("message");
                        forgot_password_edittext.setText("");
                       // Toast.makeText(getApplicationContext(), "" + code, Toast.LENGTH_LONG).show();
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ForgotPassword.this);
                        dlgAlert.setMessage("Password reset link has been sent to your email address");
                        dlgAlert.setTitle("Forgot Password ");
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                        dialog.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                    }
                                });
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
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


               // Toast.makeText(ForgotPassword.this, getErrorMessage(getApplicationContext(),error), Toast.LENGTH_SHORT).show();
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("forgot",json);

                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {

                                JSONObject jsonObject=new JSONObject(json);

                                //String message=jsonObject.getString("message");
                                // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ForgotPassword.this);
                                dlgAlert.setMessage("User doesn't exists");
                                dlgAlert.setTitle("Forgot ");
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
                // hide the progress dialog
                hidepDialog();
            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public static String getErrorMessage(Context context, VolleyError error) {
        String errorMsg = "";
        Resources resources = context.getResources();
        if (error instanceof TimeoutError) {
            errorMsg = resources.getString(R.string.net_error_timeout);
        } else if(error instanceof NoConnectionError) {
            if(error.getCause() instanceof UnknownHostException ||
                    error.getCause() instanceof EOFException) {
                errorMsg = resources.getString(R.string.net_error_connect_network);
            } else {
                if(error.getCause().toString().contains("Network is unreachable")) {
                    errorMsg = resources.getString(R.string.net_error_no_network);
                } else {
                    errorMsg = resources.getString(R.string.net_error_connect_network);
                }
            }
        } else if(error instanceof NetworkError) {
            errorMsg = resources.getString(R.string.net_error_connect_network);
        } else if(error instanceof AuthFailureError) {
            errorMsg = resources.getString(R.string.net_error_auth_failure);
        } else if(error instanceof ServerError) {
            errorMsg = resources.getString(R.string.net_error_server);
        } else if(error.getCause() instanceof NullPointerException){
            errorMsg = resources.getString(R.string.net_error_null_pointer);
        } else {
            errorMsg = resources.getString(R.string.net_error_unkown);
        }

        return errorMsg;
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
