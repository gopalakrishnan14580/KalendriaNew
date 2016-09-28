package com.kalendria.kalendria.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.kalendria.kalendria.adapter.PostReviewAdapter;
import com.kalendria.kalendria.adapter.PostReviewListViewAdater;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.ReviewPostModel;
import com.kalendria.kalendria.utility.KalendriaAppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by murugan on 17/02/16.
 */
public class PostReview extends Activity implements PostReviewListViewAdater.OnItemSelector  {

    private static final int DIALOG_LOGIN =1 ;
    public static String TAG = PostReview.class.getSimpleName();
    private ProgressDialog pDialog;

    public static String Tag = PostReview.class.getSimpleName();
    PostReviewAdapter adapter1;
    AutoCompleteTextView post_review_spinner_text;
    List<String> autosearchAdapterList;
    ArrayList<ReviewPostModel> listviewAdapterList;
    ArrayList<ReviewPostModel> totalItemsList;
    ArrayList<ReviewPostModel> sendPostToServer;
    Context mContext;
    ListView list_review;
    PostReviewListViewAdater adater;
    RatingBar overall_retting_bar,ambience_retting_bar,staff_retting_bar,clean_retting_bar,Value_retting_bar;
    TextView register_back_btn;
    Button post_review_btn;
    String overall_retting_bar_value,ambience_retting_v,staff_retting_barv,clean_retting_barv,Value_retting_barv;
    EditText post_review_edittext;
    ImageView category_autocompleted_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviewpost);
        pDialog = new ProgressDialog(PostReview.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        mContext = this;


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        post_review_spinner_text = (AutoCompleteTextView) findViewById(R.id.post_review_spinner_text);
        overall_retting_bar = (RatingBar) findViewById(R.id.overall_retting_bar);
        ambience_retting_bar = (RatingBar) findViewById(R.id.ambience_retting_bar);
        staff_retting_bar = (RatingBar) findViewById(R.id.staff_retting_bar);
        clean_retting_bar = (RatingBar) findViewById(R.id.clean_retting_bar);
        Value_retting_bar = (RatingBar) findViewById(R.id.Value_retting_bar);
        register_back_btn = (TextView) findViewById(R.id.back);
        post_review_btn = (Button) findViewById(R.id.post_review_btn);
        post_review_edittext = (EditText) findViewById(R.id.post_review_edittext);
        list_review = (ListView) findViewById(R.id.list_review);
        category_autocompleted_btn = (ImageView) findViewById(R.id.category_autocompleted_btn);

        post_review_btn.setEnabled(false);
        post_review_btn.setClickable(false);

        getservice();
        onClick_button();
        post_review_spinner_text.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(PostReview.this, android.R.layout.simple_spinner_dropdown_item, autosearchAdapterList);

                new AlertDialog.Builder(PostReview.this)
                        .setTitle("Kalendria")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                post_review_spinner_text.setText(autosearchAdapterList.get(which).toString());
                                if (!post_review_spinner_text.getText().toString().isEmpty()) {
                                    if (autosearchAdapterList.contains(post_review_spinner_text.getText().toString())) {
                                        autosearchAdapterList.remove(post_review_spinner_text.getText().toString());
                                        ReviewPostModel reviewPostModel = new ReviewPostModel();
                                        reviewPostModel.setServiceName(totalItemsList.get(which).getServiceName());
                                        reviewPostModel.setServiceID(totalItemsList.get(which).getServiceID());
                                        reviewPostModel.setBusinessName(totalItemsList.get(which).getBusinessName());
                                        reviewPostModel.setBusinessID(totalItemsList.get(which).getBusinessID());
                                        listviewAdapterList.add(reviewPostModel);
                                         adater = new PostReviewListViewAdater(listviewAdapterList, getApplicationContext(), PostReview.this);
                                        list_review.setAdapter(adater);
                                        totalItemsList.remove(which);
                                    }

                                } else {
                                    // write your  code here
                                }

                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });

        category_autocompleted_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(PostReview.this, android.R.layout.simple_spinner_dropdown_item, autosearchAdapterList);

                new AlertDialog.Builder(PostReview.this)
                        .setTitle("Service List")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                post_review_spinner_text.setText(autosearchAdapterList.get(which).toString());
                                if (!post_review_spinner_text.getText().toString().isEmpty()) {
                                    if (autosearchAdapterList.contains(post_review_spinner_text.getText().toString())) {
                                        autosearchAdapterList.remove(post_review_spinner_text.getText().toString());
                                        ReviewPostModel reviewPostModel = new ReviewPostModel();
                                        reviewPostModel.setServiceName(totalItemsList.get(which).getServiceName());
                                        reviewPostModel.setServiceID(totalItemsList.get(which).getServiceID());
                                        reviewPostModel.setBusinessName(totalItemsList.get(which).getBusinessName());
                                        reviewPostModel.setBusinessID(totalItemsList.get(which).getBusinessID());
                                        listviewAdapterList.add(reviewPostModel);
                                        adater = new PostReviewListViewAdater(listviewAdapterList, getApplicationContext(), PostReview.this);
                                        list_review.setAdapter(adater);
                                        totalItemsList.remove(which);
                                    }

                                } else {
                                    // write your  code here
                                }

                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });



    }

   /* @Override
    public void onClick(View v) {
        // Perform action on click
        switch(v.getId()) {
            case R.id.overall_retting_bar:

                break;
            case R.id.ambience_retting_bar:

                break;
        }

    };*/



    private void onClick_button() {


        post_review_edittext.addTextChangedListener(fullnameTextWatcher);
        post_review_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFavorite();
            }
        });

        overall_retting_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
               /* stars.getDrawable(0).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);*/
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                overall_retting_bar_value= String.valueOf(rating);


            }
        });
        ambience_retting_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

                ambience_retting_v= String.valueOf(rating);


            }
        });
        clean_retting_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

                clean_retting_barv= String.valueOf(rating);


            }
        });
        Value_retting_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                Value_retting_barv= String.valueOf(rating);


            }
        });
        staff_retting_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                staff_retting_barv= String.valueOf(rating);


            }
        });
        register_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(PostReview.this, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);*/
                finish();
            }
        });

    }


    TextWatcher fullnameTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            String text;
            text = post_review_edittext.getText().toString().trim();
            if (TextUtils.isEmpty(overall_retting_bar_value)) {
                Log.d(TAG, "String is empty or null!");
            }else if (TextUtils.isEmpty(ambience_retting_v)) {
                Log.d(TAG, "String is empty or null!");
            }else if (TextUtils.isEmpty(staff_retting_barv)) {
                Log.d(TAG, "String is empty or null!");
            }else if (TextUtils.isEmpty(clean_retting_barv)) {
                Log.d(TAG, "String is empty or null!");
            }else if (TextUtils.isEmpty(Value_retting_barv)) {
                Log.d(TAG, "String is empty or null!");
            }else if (TextUtils.isEmpty(text)) {
                post_review_edittext.setError("Please enter your rating!");
            }else  {

                boolean ratting_Values=true;
                for (int i = 0; i<listviewAdapterList.size(); i++) {
                   String review=listviewAdapterList.get(i).getRattingFromList();
                    System.out.println("kkkkkkkkk"+review);
                    if (TextUtils.isEmpty(review)){
                        ratting_Values=false;
                        System.out.println("kkkkkkkkk-->" +""+review);

                    }
                }
                if (Boolean.TRUE.equals(ratting_Values)) {
                    post_review_btn.setEnabled(true);
                    post_review_btn.setClickable(true);
                    post_review_btn.setBackgroundColor(Color.parseColor("#0097db"));

                }
            }

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };

    private void getservice() {


        showpDialog();
        String venueID = Constant.getVenueId(getApplicationContext());
        String url = Constant.HOST+"api/v1/service?business="+venueID;
        System.out.println("service-->" + url);

        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listviewAdapterList = new ArrayList<>();
                totalItemsList = new ArrayList<>();
                autosearchAdapterList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        ReviewPostModel ch = new ReviewPostModel();
                        ch.setBusinessID(obj.getString("business"));
                        ch.setBusinessName(obj.getString("business_name"));
                        ch.setServiceID(obj.getString("id"));
                        ch.setServiceName(obj.getString("name"));
                        autosearchAdapterList.add(obj.getString("name"));
                        totalItemsList.add(ch);

                    }
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, autosearchAdapterList);
                    post_review_spinner_text.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                VolleyLog.d(Tag, "Error : " + error.getMessage());
                final int statusCode = error.networkResponse.statusCode;
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
    public void OnItemSelected(int postion, String imageName) {

                    Log.i("Adapter", "star: " + postion);
                    Log.i("Adapter", "star position: " + imageName);

                    ReviewPostModel ch = new ReviewPostModel();
                    ch.setBusinessID(listviewAdapterList.get(postion).getBusinessID());
                    ch.setBusinessName(listviewAdapterList.get(postion).getBusinessName());
                    ch.setServiceID(listviewAdapterList.get(postion).getServiceID());
                    ch.setServiceName(listviewAdapterList.get(postion).getServiceName());
                    ch.setRattingFromList(imageName);
                   // listviewAdapterList.add(ch);
                    listviewAdapterList.set(postion,ch);
             System.out.println("listviewAdapterList size-->"+listviewAdapterList.size());



    }

    @Override
    public void OnItemSelected(int postion) {
       // Toast.makeText(PostReview.this, "Hi"+listviewAdapterList.size(), Toast.LENGTH_SHORT).show();
        for (int i=0;i<listviewAdapterList.size();i++) {
           System.out.println("mmmmmm-->"+listviewAdapterList.get(i).getServiceName().equals(listviewAdapterList.get(postion).getServiceName()));

            if (listviewAdapterList.get(i).getServiceName().equalsIgnoreCase(listviewAdapterList.get(postion).getServiceName())) {
                ReviewPostModel ch = new ReviewPostModel();
                ch.setBusinessID(listviewAdapterList.get(postion).getBusinessID());
                ch.setBusinessName(listviewAdapterList.get(postion).getBusinessName());
                ch.setServiceID(listviewAdapterList.get(postion).getServiceID());
                ch.setServiceName(listviewAdapterList.get(postion).getServiceName());
                totalItemsList.add(ch);
                autosearchAdapterList.add(listviewAdapterList.get(postion).getServiceName());
                listviewAdapterList.remove(postion);

            }
        }
        adater = new PostReviewListViewAdater(listviewAdapterList, getApplicationContext(),PostReview.this);
        list_review.setAdapter(adater);
        adater.notifyDataSetChanged();
        list_review.invalidateViews();

    }


    private void sendFavorite() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("overall", overall_retting_bar_value);
            jsonObject.put("ambiance", ambience_retting_v);
            jsonObject.put("staff", staff_retting_barv);
            jsonObject.put("cleanliness", clean_retting_barv);
            jsonObject.put("value", Value_retting_barv);
            jsonObject.put("message", "Great");
            jsonObject.put("user", Constant.getUserId(getApplicationContext()));
            jsonObject.put("business", Constant.getVenueId(getApplicationContext()));
            jsonObject.put("business_name", Constant.getVenueName(getApplicationContext()));

            JSONArray jsonArrayMessages = new JSONArray();
            for (int i=0;i<listviewAdapterList.size();i++) {

                JSONObject parentData = new JSONObject();
                JSONObject childData = new JSONObject();
                try {
                    parentData.put("value", listviewAdapterList.get(i).getRattingFromList());

                    childData.put("business", listviewAdapterList.get(i).getBusinessID());
                    childData.put("business_name",listviewAdapterList.get(i).getBusinessName());
                    childData.put("id",listviewAdapterList.get(i).getServiceID());
                    childData.put("name",listviewAdapterList.get(i).getServiceName());
                    childData.put("rating_value",listviewAdapterList.get(i).getRattingFromList());

                    parentData.put("service", childData);
                    jsonArrayMessages.put(parentData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }



            jsonObject.put("ratings", jsonArrayMessages);

            System.out.println("--->"+jsonObject); ;

        } catch (JSONException je) {
            Log.e("PostReview", "error in Write", je);
        }

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Constant.POST_REVIEW, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        //getfavorite();
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(PostReview.this);
                        dlgAlert.setMessage("Your review will be posted shortly ");
                        dlgAlert.setTitle("THANK YOU ");
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

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hidepDialog();
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
                                String message = jsonObject.getString("message");
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

}
