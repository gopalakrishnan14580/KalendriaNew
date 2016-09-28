package com.kalendria.kalendria.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.AppointmentDetailsPageActivity;
import com.kalendria.kalendria.adapter.MyOderAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.MyorderModel;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**

 */
public class MyOrdersFragment extends Fragment {

    private ProgressDialog pDialog;
    ListView list;
    private List<MyorderModel> cafeList;
    public static String Tag = MyOrdersFragment.class.getSimpleName();
    MyOderAdapter adapter1;
    TextView current_txt,previous_txt,status_label;
    private SharedPreferences sharedPref;
    boolean isFirstTime=false;
    boolean isCurrent = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invites, container, false);
        isFirstTime=true;
        sharedPref = getActivity().getSharedPreferences(Constant.MyPREFERENCES, 0);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        list=(ListView)rootView.findViewById(R.id.myorder);
        current_txt=(TextView)rootView.findViewById(R.id.current_txt);
        previous_txt=(TextView)rootView.findViewById(R.id.previous_txt);
        status_label=(TextView)rootView.findViewById(R.id.status_label);

        onclickButton();

        cafeList=new ArrayList<>();
        adapter1 = new MyOderAdapter(getActivity(),cafeList);
        list.setAdapter(adapter1);

        if(KalendriaAppController.isNetworkConnected(getActivity())){

            String url= Constant.HOST;

            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date SelDate = new Date();
            String selectDateString = formatter2.format(SelDate);

            url += "api/v1/booking?limit="+limit
                    +"&populate=business&skip="+skip
                    +"&where={%22customer%22:"+Constant.getUserId()
                    +",%22scheduledAt%22:{%22%3E%22:%22"+selectDateString+"%22}}";
            isCurrent=true;
            MakeJsonArrayReq(url,true);

        }else{
            Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
        }

        Button backButton = (Button)rootView.findViewById(R.id.register_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getActivity().finish();
            }
        });
        backButton.setVisibility(View.INVISIBLE);
        initPaging();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String position1 = String.valueOf(position);
                System.out.println("position id==>" + position1);

                Intent intent = new Intent(getActivity(), AppointmentDetailsPageActivity.class);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("myOderBuisinesId", cafeList.get(position).getmOrderBusinessId());
                editor.putString("oderId", cafeList.get(position).getMorderId());
                editor.putBoolean("isCurrent", cafeList.get(position).isCurrent);
                editor.commit();
                startActivity(intent);

            }
        });
        KalendriaAppController.hideSoftKeyboard(getActivity());
        return rootView;
    }

    public  int limit=30;
    public  int skip=0;
    public int pageIndex=0;
    public int direction =1;//1=current,2=previous
    public void initPaging()
    {
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // Do nothing
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                Log.d("FIRST TIME:","first item :"+firstVisibleItem+" Total :"+totalItemCount);
                // threshold being indicator if bottom of list is hit
                int threshold = (pageIndex+1) *limit;
                if ((firstVisibleItem >= threshold-8) ) {
                    pageIndex++;
                    String url= getURL(direction);
                    if(direction==1) {
                        isCurrent=true;
                        MakeJsonArrayReq(url, true);
                    }
                    else {
                        isCurrent=true;
                        MakeJsonArrayReq(url, false);
                    }
                }
            }
        });
    }
    public String getURL(int direction)
    {
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date SelDate = new Date();
        String selectDateString = formatter2.format(SelDate);

        skip = pageIndex *limit;
        if(direction==2)//current
        {
            return  Constant.HOST + "api/v1/booking?limit="+limit
                    +"&populate=business&skip="+skip
                    +"&where={%22customer%22:"+Constant.getUserId()
                    +",%22scheduledAt%22:{%22<=%22:%22"+selectDateString+"%22}}";
        }
        else
        {

            return  Constant.HOST + "api/v1/booking?limit="+limit
                    +"&populate=business&skip="+skip
                    +"&where={%22customer%22:"+Constant.getUserId()
                    +",%22scheduledAt%22:{%22%3E%22:%22"+selectDateString+"%22}}";
        }
    }
    public void onclickButton(){
        current_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cafeList.clear();
                adapter1.notifyDataSetChanged();
                direction = 1;
                pageIndex = 0;
                current_txt.setBackgroundColor(Color.parseColor("#FFFFFF"));
                current_txt.setTextColor(Color.parseColor("#000000"));

                previous_txt.setBackgroundColor(Color.parseColor("#000000"));
                previous_txt.setTextColor(Color.parseColor("#FFFFFF"));

                if (KalendriaAppController.isNetworkConnected(getActivity())) {


                    String url = getURL(direction);
                    isCurrent=true;
                    MakeJsonArrayReq(url, true);
                } else {
                    Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        previous_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cafeList.clear();
                adapter1.notifyDataSetChanged();
                direction = 2;
                pageIndex = 0;
                previous_txt.setBackgroundColor(Color.parseColor("#FFFFFF"));
                previous_txt.setTextColor(Color.parseColor("#000000"));

                current_txt.setBackgroundColor(Color.parseColor("#000000"));
                current_txt.setTextColor(Color.parseColor("#FFFFFF"));
                if (KalendriaAppController.isNetworkConnected(getActivity())) {

                    String url = getURL(direction);
                    isCurrent =false;
                    MakeJsonArrayReq(url, false);

                } else {
                    Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void MakeJsonArrayReq(String url,final boolean isCurrent) {
        showpDialog();


        System.out.println("MYODER-->"+url);
        JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(Tag, response.toString());
                hidepDialog();

                ArrayList temp=new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        MyorderModel myorderModel=new MyorderModel();
                        JSONObject jo=object.getJSONObject("business");
                        myorderModel.setMorderVenuName(jo.getString("name"));
                        myorderModel.setmOrderBusinessId(jo.getString("id"));
                        myorderModel.setMorderServiceName(object.getString("service_name"));
                        myorderModel.setMorderPrice(object.getString("price"));
                        myorderModel.setMorderPoints(object.getString("points"));
                        myorderModel.setMorderTime(object.getString("time"));
                        myorderModel.setMorderDate(object.getString("scheduledAt"));
                        myorderModel.setMorderId(object.getString("order"));
                        myorderModel.isCurrent=isCurrent;


                        myorderModel.mOrderPayAt= SafeParser.getString(object,"pay_at","");
                        myorderModel.setMorderStatus(SafeParser.getString(object,"status",""));
                        myorderModel.mOrderDiscount= SafeParser.getString(object,"discount","0");

                        temp.add(myorderModel);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                hidepDialog();

                cafeList.addAll(temp);
                if(cafeList.size()==0)
                {
                    status_label.setVisibility(View.VISIBLE);
                    list.setVisibility(View.INVISIBLE);
                }
                else
                {
                    status_label.setVisibility(View.INVISIBLE);
                    list.setVisibility(View.VISIBLE);
                }
                adapter1.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        super.onResume();

        if(isFirstTime){ isFirstTime=false; return;}

        if(KalendriaAppController.isNetworkConnected(getActivity())){

            pageIndex=0;
            cafeList.clear();
            adapter1.notifyDataSetChanged();
            String url= getURL(direction);
            if(direction==1) {
                isCurrent=true;
                MakeJsonArrayReq(url, true);
            }
            else {
                isCurrent=false;
                MakeJsonArrayReq(url, false);
            }
        }else{
            Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
        }


    }
}
