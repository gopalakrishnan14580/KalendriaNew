package com.kalendria.kalendria.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Category;
import com.kalendria.kalendria.activity.DashBoard;
import com.kalendria.kalendria.activity.GiftDetailView;
import com.kalendria.kalendria.activity.GiftVoucherActivity;
import com.kalendria.kalendria.activity.MyOrderActivity;
import com.kalendria.kalendria.activity.SubCategory;
import com.kalendria.kalendria.activity.Venue;
import com.kalendria.kalendria.activity.VenueItem;
import com.kalendria.kalendria.adapter.AddToCardAdapter;
import com.kalendria.kalendria.adapter.GridDetailViewAdapter;
import com.kalendria.kalendria.adapter.GridListViewAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.KAGift;
import com.kalendria.kalendria.model.MyorderModel;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GiftListViewFragment extends Fragment {

    View view;
    List<AddToCardVenueModel> addToCardSingletone;
    ListView listView;
    TextView statusLabel;
    ArrayList<KAGift> arrGifts = new ArrayList<>();
    GridListViewAdapter giftListAdapter;
    private ProgressDialog pDialog;
    int limit = 100;
    int pageIndex;

    public GiftListViewFragment() {
        // Required empty public constructor
        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();/*This line is used add to card venue name and servie list */

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.gift_listview_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        statusLabel = (TextView) view.findViewById(R.id.status_label);

        giftListAdapter = new GridListViewAdapter(getActivity(), arrGifts);
        listView.setAdapter(giftListAdapter);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        getGiftDetails();
        allActions();
        return view;
    }

    public void allActions()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), GiftDetailView.class);
               KalendriaAppController.getInstance().selectedGiftVoucher=arrGifts.get(position);
                getActivity().startActivity(intent);
            }
        });

        listView.setOnTouchListener(new ListView.OnTouchListener() {
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

    }

    @Override
    public void onResume() {
        super.onResume();


        try {


            String sizeofcard = " " + addToCardSingletone.size();
            Context context = getActivity();
            if (context instanceof DashBoard) {
                ((DashBoard) context).dispatchInformations(sizeofcard);
            } else if (context instanceof Venue) {
                ((Venue) context).dispatchInformations(sizeofcard);
            } else if (context instanceof MyOrderActivity) {
                ((MyOrderActivity) context).dispatchInformations(sizeofcard);
            } else if (context instanceof Category) {
                ((Category) context).dispatchInformations(sizeofcard);
            } else if (context instanceof SubCategory) {
                ((SubCategory) context).dispatchInformations(sizeofcard);
            } else if (context instanceof GiftVoucherActivity) {
                ((GiftVoucherActivity) context).dispatchInformations(sizeofcard);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void reloadListView() {

        giftListAdapter.notifyDataSetChanged();
        if (arrGifts.size() > 0) {

            if (statusLabel != null)
                statusLabel.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            if (statusLabel != null)
                statusLabel.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }


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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    String Tag = "GiftListView";

    private void getGiftDetails() {

        if (!KalendriaAppController.isNetworkConnected(getActivity())) {
            reloadListView();
            Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
            return;

        }
        showpDialog();

        int skip = pageIndex * limit;


        String userId = Constant.getUserId(getActivity());
        String userEmail = Constant.getEmail();

        // {"or":[{"email":"topqualityqa03@gmail.com"},{"from":17}]}

        String url = Constant.HOST;


        url += "api/v1/gift?limit=" + limit + "&populate=coupon&skip=" + skip
                + "&sort=createdAt+DESC&where={\"or\":[{\"email\":\"" + userEmail
                + "\"},{\"from\":" + userId + "}]}";


        System.out.println("MYODER-->" + url);
        JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(Tag, response.toString());
                hidepDialog();
                String userId2 = Constant.getUserId(getActivity());
                ArrayList temp = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject dictGift = response.getJSONObject(i);
                        JSONObject dictCoupon = dictGift.getJSONObject("coupon");
                        KAGift objGift = new KAGift();


                        objGift.receipnt_Name = SafeParser.getString(dictGift, "name", "");
                        objGift.code = SafeParser.getString(dictCoupon, "code");
                        objGift.email = SafeParser.getString(dictGift, "email");
                        objGift.type = SafeParser.getString(dictGift, "type");
                        objGift.fromId = SafeParser.getString(dictGift, "from");


                        if (objGift.fromId.equalsIgnoreCase(userId2)) {
                            objGift.ReceiverType = "Purchased";
                        } else {
                            objGift.ReceiverType = "Received";
                        }

                        objGift.address = SafeParser.getString(dictGift, "address");
                        objGift.phone = SafeParser.getString(dictGift, "phone");
                        objGift.message = SafeParser.getString(dictGift, "message");
                        objGift.remainAmt = SafeParser.getString(dictCoupon, "value");
                        objGift.amount = SafeParser.getString(dictCoupon, "amount");
                        objGift.discount_type = SafeParser.getString(dictCoupon, "discount_type");
                        String expiryAt = SafeParser.getString(dictCoupon, "expiryAt");
                        objGift.status = SafeParser.getString(dictGift, "status");


                        //expiryAt
                        try {

                            if (expiryAt.length() > 0) {
                                String[] arrExp = expiryAt.split(" ");
                                String expVal = arrExp[0];
                                objGift.expiryAt = expVal;

                            } else {
                                objGift.expiryAt = "";

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        temp.add(objGift);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                hidepDialog();

                arrGifts.addAll(temp);
                reloadListView();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Tag, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
                reloadListView();
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



        }



