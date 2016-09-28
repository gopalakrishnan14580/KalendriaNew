package com.kalendria.kalendria.fragment;

/**
 * Created by murugan on 2/05/2016
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.toolbox.RequestFuture;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.AppointmentConfirmPageActivity;
import com.kalendria.kalendria.activity.Category;
import com.kalendria.kalendria.activity.CheckOut;
import com.kalendria.kalendria.activity.DashBoard;
import com.kalendria.kalendria.activity.GiftVoucherActivity;
import com.kalendria.kalendria.activity.MyOrderActivity;
import com.kalendria.kalendria.activity.SubCategory;
import com.kalendria.kalendria.activity.Venue;
import com.kalendria.kalendria.adapter.CheckOutAdapter;
import com.kalendria.kalendria.adapter.CustomAdapter;
import com.kalendria.kalendria.adapter.StaffListAdapter;
import com.kalendria.kalendria.adapter.WeekAdapter;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.KADate;
import com.kalendria.kalendria.model.KAStaff;
import com.kalendria.kalendria.model.TimeBean;
import com.kalendria.kalendria.model.VenueDay;
import com.kalendria.kalendria.model.WeekView;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.KAJsonArrayRequest;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.kalendria.kalendria.utility.SafeParser;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class CheckoutFragment extends Fragment implements CheckOutAdapter.CheckOutAdapterDelegate {
    TextView week;
    LinearLayout per, next;
    RecyclerView daysList;
    View view;
    ArrayList<WeekView> weekViews;
    Context ctx;
    int count = 0;
    final ArrayList<KADate> days = new ArrayList<>();
    final ArrayList<String> days_name = new ArrayList<>();

    AddToCardVenueModel selectedAddToCardVenueModel;

    ArrayList<AddToCardServiceModel> service_dboup = new ArrayList<AddToCardServiceModel>();
    CheckOutAdapter checkOutAdapter;
    CustomAdapter customAdapter; //added by Magesh
    ListView list, listView;

    ArrayList<Double> date_add;
    ArrayList<TimeBean> myList = new ArrayList<TimeBean>();
    String[] arrTime;
    ArrayList<String>oftenArray;
    List<String> time_value;
    WeekAdapter weekAdapter;
    boolean checkCurrentDay;
    KADate selectedDay;
    String selectedTime;
    private ProgressDialog pDialog;
    private ProgressDialog pDialog1;
    private LinkedHashMap dictBusiness;
    private LinkedHashMap dictWorkingHours;
    StaffListAdapter staffListAdapter = null;
    TextView statusLabel;
    TextView often_textfield_act,often_close;
    Button checkOutButton;
    TextView txtTotalPrice;

    private AlertDialog staffDialog;
    private View alertView;
    private ListView staffListView;
    LinkedHashMap<String, ArrayList> hashEmployes;
    LinearLayout often_dropDownImage, often_LinearLayout;
    int selectedItemIndex;
    boolean isInitalload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        pDialog1 = new ProgressDialog(getActivity());
        pDialog1.setMessage("Please wait...");
        pDialog1.setCancelable(false);

        view = inflater.inflate(R.layout.week_days, container, false);
        daysList = (RecyclerView) view.findViewById(R.id.days_list);
        week = (TextView) view.findViewById(R.id.week);
        per = (LinearLayout) view.findViewById(R.id.pre);
        next = (LinearLayout) view.findViewById(R.id.next);
        list = (ListView) view.findViewById(R.id.list);
        listView = (ListView) view.findViewById(R.id.add_time_list);
        checkOutButton = (Button) view.findViewById(R.id.checkout_btn);
        txtTotalPrice = (TextView) view.findViewById(R.id.price);
        statusLabel = (TextView) view.findViewById(R.id.status_label);


        often_LinearLayout = (LinearLayout) view.findViewById(R.id.often_LinearLayout);
        often_dropDownImage = (LinearLayout) view.findViewById(R.id.often_dropDownImage);
        often_textfield_act = (TextView) view.findViewById(R.id.often_textfield_act);
        often_close = (TextView) view.findViewById(R.id.often_close);


        //statusLabel.setVisibility(View.GONE);
        // we are hard coding current day as selected day
        selectedDay = new KADate(Calendar.getInstance());

        hashEmployes = new LinkedHashMap<>();
        per.setVisibility(View.INVISIBLE);//No prev as of now
        dictBusiness = new LinkedHashMap();
        dictWorkingHours = new LinkedHashMap();

        getWorkingHours();
        loadTimes();
        cartList();
        initStafPicker();
        setCustomWeekCalender();
        onClickMethod();

        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        isInitalload = true;

        Button button = (Button) view.findViewById(R.id.checkout_back_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < service_dboup.size(); i++) {
                    AddToCardServiceModel addToCardServiceModel = service_dboup.get(i);
                    addToCardServiceModel.selectedDate = null;
                    addToCardServiceModel.selectedTime = null;
                    addToCardServiceModel.staffname = null;
                    addToCardServiceModel.staffID = null;
                    addToCardServiceModel.staffthumbImage = null;
                }
                CheckoutFragment.this.getActivity().finish();
            }
        });


        return view;
    }


    public void showTimes(boolean showTime) {
        if (showTime) {
            listView.setVisibility(View.VISIBLE);
            statusLabel.setVisibility(View.INVISIBLE);
        } else {
            listView.setVisibility(View.INVISIBLE);
            statusLabel.setVisibility(View.VISIBLE);
        }
    }

    public void loadTimes() {
        arrTime = new String[]{"07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45",
                "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45",
                "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45",
                "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45",
                "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45",
                "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45",
                "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45",
                "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45",
                "23:00", "23:15", "23:30", "23:45"};

        oftenArray = new ArrayList<>();
        oftenArray.add("Just Once");
        oftenArray.add("Weekly");
        oftenArray.add("Every 2 Weeks");
        oftenArray.add("2 times/week");
        oftenArray.add("3 times/week");
    }

    private String addMinute1(int hr, int min, int toadd) {
        Calendar calander = Calendar.getInstance();
        calander.set(Calendar.HOUR_OF_DAY, hr);
        calander.set(Calendar.MINUTE, min);
        calander.add(Calendar.MINUTE, toadd);
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
        return df3.format(calander.getTime());

    }

    public void resetAppointmentDate() {

        try {


            Boolean isTimeAvailable = true;

            Calendar calander = Calendar.getInstance();
            if (selectedTime == null || selectedTime.length()==0) {
                int nowHour = calander.get(Calendar.HOUR_OF_DAY);
                int nowMin = calander.get(Calendar.MINUTE);
                int rem = nowMin % 15;
                nowMin += (15 - rem);
                isTimeAvailable = false;
                selectedTime = String.format("%02d", nowHour) + ":" + String.format("%02d", nowMin);
                isInitalload=true;
            }


            String dateStr = selectedDay.year + "-" + selectedDay.month + "-" + selectedDay.day + " ";
            if (selectedTime != null && selectedTime.length() > 2 && selectedDay != null) {

                dateStr += selectedTime == null ? "9:00" : selectedTime;

            }
            if (isInitalload == false) {
                checkAvailability(dateStr);

            }

            int index = ArrayUtils.indexOf(arrTime, selectedTime);
            String startTime = selectedTime;
            Boolean isShopOpen = true;
            Boolean isLoyality = false;

            SimpleDateFormat dayformat = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");

            int totalAmount = 0;
            for (int i = 0; i < service_dboup.size(); i++) {

                Boolean isValid = false;

                try {
                    AddToCardServiceModel addToCardServiceModel = service_dboup.get(i);
                    addToCardServiceModel.selectedDate = String.format("%02d", selectedDay.month) + "-" + String.format("%02d", selectedDay.day) + "-" + selectedDay.year;
                    addToCardServiceModel.staffname="";
                    addToCardServiceModel.staffID="";
                    addToCardServiceModel.staffthumbImage="";

                    isLoyality = addToCardServiceModel.isFromLoyality;
                    if (isLoyality)
                        totalAmount += addToCardServiceModel.getIntServicePoints();
                    else
                        totalAmount += addToCardServiceModel.remainAmount;

                    if (i == 0) {
                        String aString = startTime;
                        String first = aString.substring(0, aString.indexOf(":"));
                        String second = aString.substring(aString.indexOf(":") + 1, aString.length());
                        int hour = Integer.parseInt(first);
                        int min = Integer.parseInt(second);
                        calander.set(Calendar.HOUR_OF_DAY, hour);
                        calander.set(Calendar.MINUTE, min);
                        calander.set(Calendar.MONTH, selectedDay.month - 1);
                        calander.set(Calendar.DAY_OF_MONTH, selectedDay.day);
                        calander.set(Calendar.YEAR, selectedDay.year);
                        calander.set(Calendar.SECOND, 0);
                        calander.set(Calendar.MILLISECOND, 0);


                        //calander.add(Calendar.MINUTE, toadd);
                    }
                    addToCardServiceModel.selectedDate = dayformat.format(calander.getTime());
                    String strStartTime = timeformat.format(calander.getTime());
                    long intStartTime = calander.getTimeInMillis();
                    String duration = addToCardServiceModel.getServiceDuration();
                    if (duration != null && duration.indexOf(":") > 0) {
                        String first = duration.substring(0, duration.indexOf(":"));
                        String second = duration.substring(duration.indexOf(":") + 1, duration.length());
                        int dhour = Integer.parseInt(first);
                        int dmin = Integer.parseInt(second);
                        //addToCardServiceModel.isValid = true;
                        //endTime = addMinute1(hour, min, (dhour * 60) + dmin);
                        calander.add(Calendar.MINUTE, (dhour * 60) + dmin);

                    }
                    long intEndTime = calander.getTimeInMillis();
                    String strEndTime = timeformat.format(calander.getTime());


                    long intCloseTime = 0;
                    VenueDay dayObject = null;
                    if (dictWorkingHours != null) {
                        dayObject = (VenueDay) dictWorkingHours.get(selectedDay.dayLongName.toLowerCase());

                        if (dayObject != null) {
                            isShopOpen = dayObject.isOpen();
                            addToCardServiceModel.isOpen = dayObject.isOpen();

                            String strCloseDay = addToCardServiceModel.selectedDate;
                            strCloseDay += " " + dayObject.getEnd_time();

                            SimpleDateFormat closeFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                            Date endDate = closeFormat.parse(strCloseDay);
                            intCloseTime = endDate.getTime();
                        }

                    }
                    addToCardServiceModel.selectedTime = strStartTime + " - " + strEndTime;

                    if (intCloseTime == 0 || intCloseTime < intEndTime) {
                        addToCardServiceModel.isValid = false;
                    } else
                        addToCardServiceModel.isValid = true;

                    if (dayObject != null) {
                        if (dayObject.isOpen == false)
                            addToCardServiceModel.selectedTime = dayObject.getStart_time() + " - " + dayObject.getEnd_time();
                    } else if (!isValid) {
                        addToCardServiceModel.isValid = false;

                    }

                    if(myList.size()==0)
             {
                        isShopOpen=false;
                        addToCardServiceModel.isValid = false;
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }



            }



            statusLabel.setText("Sorry, this time is not available for booking");
            showTimes(isShopOpen);

            if (customAdapter != null) {
                customAdapter.totalAmount = totalAmount;
                customAdapter.isLoyality = selectedAddToCardVenueModel.isFromLoyality;
                customAdapter.notifyDataSetChanged();
            }

            if (isLoyality)
                txtTotalPrice.setText("" + totalAmount + " Points");
            else
                txtTotalPrice.setText("" + totalAmount + " AED");
            checkOutAdapter.notifyDataSetChanged();
            validateStaff();//check and enable check button

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            isInitalload=false;
        }
    }

    public void cartList() {
        try {
            List<AddToCardVenueModel> addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();/*This line is used add to card venue name and servie list */

            for (int i = 0; i < addToCardSingletone.size(); i++) {
                if (Constant.getVenueId(getActivity()).equalsIgnoreCase(addToCardSingletone.get(i).getVenueID())) {

                    selectedAddToCardVenueModel = addToCardSingletone.get(i);
                    service_dboup = addToCardSingletone.get(i).getItems();
                    break;


                }
            }

            if(selectedAddToCardVenueModel.getUserSector().equalsIgnoreCase("2"))
            {
                often_LinearLayout.setVisibility(View.VISIBLE);
                String oftenText=   selectedAddToCardVenueModel.getHow_often() ;
                if(oftenText!=null && oftenText.length()>0)
                often_textfield_act.setText(oftenText);
                else {
                    often_textfield_act.setText(oftenArray.get(0));
                    selectedAddToCardVenueModel.setHow_often(oftenArray.get(0));
                }
            }
            else
            {
                often_LinearLayout.setVisibility(View.GONE);
            }

            often_textfield_act.setText(selectedAddToCardVenueModel.getHow_often());
            checkOutAdapter = new CheckOutAdapter(service_dboup, getActivity(), this);
            list.setAdapter(checkOutAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setCustomWeekCalender() {
        /*set the custom calender view start */
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance(Locale.US);

        int nowHour = c.get(Calendar.HOUR_OF_DAY);
        int nowMin = c.get(Calendar.MINUTE);
        int rem = nowMin % 15;
        nowMin += (15 - rem);


        try {
            weekViews = new ArrayList<WeekView>();
            c.add(Calendar.DATE, 0);
            for (int i = 0; i < 7; i++) {
                String currentDate = (String) (formattedDate.format(c.getTime()));
                WeekView weekView = new WeekView();
                weekView.setCurrentDate(currentDate);
                weekView.setCurrentWeek(c.get(Calendar.WEEK_OF_MONTH));
                weekView.setCurrentDay(Integer.toString(c.get(Calendar.DAY_OF_MONTH)));
                weekView.setCurrentMonth(Integer.toString(c.get(Calendar.MONTH) + 1));
                weekView.setCurrentYear(Integer.toString(c.get(Calendar.YEAR)));
                weekView.setCurrentWeekDays(getDaysOfWeek(c.getTime(), c.getFirstDayOfWeek()));
                weekViews.add(weekView);
                c.add(Calendar.DATE, 7);
            }

            daysList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            daysList.setLayoutManager(llm);



          /*get the days of week start */
            /*
            DateFormatSymbols symbols = new DateFormatSymbols();
            String[] dayNames = symbols.getShortWeekdays();

            for (String s : dayNames) {
                if (!s.equals("")) {
                    days_name.add(s.toUpperCase());
                }
            }*/

            days_name.add("SUN");
            days_name.add("MON");
            days_name.add("TUE");
            days_name.add("WED");
            days_name.add("THU");
            days_name.add("FRI");
            days_name.add("SAT");


            weekAdapter = new WeekAdapter(getActivity(), days, days_name, 0); /*days and days_name for display and index for round current date */
            daysList.setAdapter(weekAdapter);

            ArrayList<KADate> daysT = weekViews.get(0).getCurrentWeekDays();
            days.addAll(daysT);

            int idx = 0;
            for (KADate date : daysT) {
                // Get Today KADate Object
                if (date.unique == weekAdapter.todayUniqueID) {
                    selectedDay = date;
                    weekAdapter.selectedIndex = date.unique;
                    break;
                }
                idx++;
            }

            // View created, so show current date time
            if (selectedDay != null && selectedDay.dayLongName != null)
                loadAvailableTimes(true, selectedDay.dayLongName);
            else
                loadAvailableTimes(checkCurrentDay, KADate.todayLongName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setCustomAdapterListner() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                customAdapter.selectedIndex = position;
                customAdapter.notifyDataSetChanged();
                TimeBean currentListData = myList.get(position);
                selectedTime = currentListData.getCal_time();
                resetAppointmentDate();

            }
        });
        customAdapter.SetOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TimeBean currentListData, int position) {


            }
        });

    }

    /**
     * discussion, reloading Month Horiztal listview
     *
     * @param index, week index
     */
    public void reloadDaysWheel(int index) {
        int monthName = Integer.parseInt(weekViews.get(count).getCurrentMonth());
        week.setText(getMonth(monthName) + " " + weekViews.get(count).getCurrentYear());

        days.clear();
        ArrayList<KADate> daysT = weekViews.get(count).getCurrentWeekDays();
        days.addAll(daysT);

        if(selectedTime==null  || selectedTime.length()==0)
        selectedTime = arrTime[0];

        weekAdapter.refreshDates(getActivity(), days, days_name, 0);
        weekAdapter.notifyDataSetChanged();

        resetAppointmentDate();


    }

    /**
     * Load bottom time listview based on selected date
     */
    public void loadAvailableTimes(boolean isToday, String dayLongname) {

        int intStartIndex = 0;
        int intTodayIndex = 0;
        int endIndex = 0;
        if (isToday) { // if Today then we have to show time from now to 22:55
            Calendar c = Calendar.getInstance(Locale.US);

            int nowHour = c.get(Calendar.HOUR_OF_DAY);
            int nowMin = c.get(Calendar.MINUTE);
            int rem = nowMin % 15;
            nowMin += (15 - rem);
            if (nowMin == 60) {
                nowHour++;
                nowMin = 0;

            }
            String strTime = String.format("%02d", nowHour) + ":" + String.format("%02d", nowMin);
            intStartIndex = ArrayUtils.indexOf(arrTime, strTime);

        }
        try {


            if (dictWorkingHours != null && dictWorkingHours.size() > 0) {

                VenueDay dayObject = (VenueDay) dictWorkingHours.get(dayLongname.toLowerCase());
                intTodayIndex = ArrayUtils.indexOf(arrTime, dayObject.getStart_time());
                endIndex = ArrayUtils.indexOf(arrTime, dayObject.getEnd_time());
                weekAdapter.dictWorkingHours=dictWorkingHours;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (intTodayIndex > intStartIndex)
            intStartIndex = intTodayIndex;

        if (intStartIndex < 0) intStartIndex = 0;
        if (endIndex < 0) endIndex = 0;
        if (endIndex > arrTime.length) endIndex = arrTime.length;


        try {

            if (myList == null) myList = new ArrayList<>();
            else myList.clear();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Load all time value into myList array
        for (int i = intStartIndex; i < endIndex; i++) {
            try {


                TimeBean ld = new TimeBean();
                ld.setCal_time(arrTime[i]);
                myList.add(ld);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (myList.size() > 0) {
            TimeBean tb = myList.get(0);
            selectedTime = tb.getCal_time();
        } else {
            selectedTime = null;
            showTimes(false);
        }

        try {
            if (customAdapter == null) {
                customAdapter = new CustomAdapter(getActivity(), myList);

                listView.setAdapter(customAdapter);
                setCustomAdapterListner();
            }
            customAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            int monthName = Integer.parseInt(weekViews.get(count).getCurrentMonth());
            week.setText(getMonth(monthName) + " " + weekViews.get(count).getCurrentYear());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        resetAppointmentDate();

    }

    public void showOftenAlert() {

        final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, oftenArray);
        new android.app.AlertDialog.Builder(getActivity())
                .setTitle("Choose")
                .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        often_textfield_act.setText(oftenArray.get(which).toString());
                        //sendOften= how_often_txt.getText().toString();
                        selectedAddToCardVenueModel.setHow_often(oftenArray.get(which).toString());
                        often_close.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                }).create().show();

        KalendriaAppController.hideSoftKeyboard(getActivity());
    }

    public void onClickMethod() {


        often_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                often_close.setVisibility(View.INVISIBLE);
                often_textfield_act.setText("");
                selectedAddToCardVenueModel.setHow_often("");

            }
        });
        often_textfield_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showOftenAlert();
            }
        });
        often_dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showOftenAlert();
            }
        });
        // Method for Appointment
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                KalendriaAppController.getInstance().selectedAddToCardVenueModel = selectedAddToCardVenueModel;
                Intent intent = new Intent(getActivity(), AppointmentConfirmPageActivity.class);
                startActivity(intent);
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

        list.setOnTouchListener(new ListView.OnTouchListener() {
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


        weekAdapter.SetOnItemClickListener(new WeekAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                // count=position;
                ArrayList<KADate> daysT = weekViews.get(count).getCurrentWeekDays();

                // code modified by Magesh
                KADate selectedDay1 = daysT.get(position);

                if (selectedDay1.unique < weekAdapter.todayUniqueID)
                    return;//old days we dont have any click event
                if (selectedDay1.unique == weekAdapter.todayUniqueID) {
                    checkCurrentDay = true;
                } else
                    checkCurrentDay = false;

                selectedDay = selectedDay1;
                weekAdapter.selectedIndex = selectedDay.unique;
                customAdapter.selectedIndex = 0;
                if (selectedDay != null && selectedDay.dayLongName != null)
                    loadAvailableTimes(checkCurrentDay, selectedDay.dayLongName);
                else
                    loadAvailableTimes(checkCurrentDay, KADate.todayLongName());
                weekAdapter.notifyDataSetChanged();
            }

        });
/*set the onclick listner for recycle view end  */


        per.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                next.setVisibility(View.VISIBLE);
                try {

                    if (count > 0 && count <= 6) {
                        count--;
                        reloadDaysWheel(count);

                    }
                    if (count == 0)
                        per.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                per.setVisibility(View.VISIBLE);
                if (count < 6) {
                    count++;
                    reloadDaysWheel(count);

                }

                if(count>=6)
                    next.setVisibility(View.INVISIBLE);

            }
        });

    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    private static ArrayList<KADate> getDaysOfWeek(Date refDate, int firstDayOfWeek) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(refDate);
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        ArrayList<KADate> daysOfWeek = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            KADate date = new KADate(calendar);
            daysOfWeek.add(date);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysOfWeek;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        try {


            if (pDialog.isShowing())
                pDialog.dismiss();
        }
        catch (Exception ex){ex.printStackTrace();}
    }

    private void showpDialog1() {
        if (!pDialog1.isShowing())
            pDialog1.show();
    }

    private void hidepDialog1() {
        if (pDialog1.isShowing())
            pDialog1.dismiss();
    }

private void setOrderCount()
{
    try {

 Activity context = getActivity();

        if (context instanceof DashBoard) {
            ((DashBoard) context).dispatchInformations("");
        } else if (context instanceof Venue) {
            ((Venue) context).dispatchInformations("");
        }
        else if (context instanceof MyOrderActivity) {
            ((MyOrderActivity) context).dispatchInformations("");
        }
        else if (context instanceof Category) {
            ((Category) context).dispatchInformations("");
        }
        else if (context instanceof SubCategory) {
            ((SubCategory) context).dispatchInformations("");
        }
        else if (context instanceof GiftVoucherActivity) {
            ((GiftVoucherActivity) context).dispatchInformations("");
        }
        else if (context instanceof CheckOut) {
            ((CheckOut) context).dispatchInformations("");
        }

    }catch (Exception ex){ex.printStackTrace();}
}
    // Adapter interface methods
    public void onDeleteService(int position, AddToCardServiceModel data) {

        ArrayList<AddToCardServiceModel> items = selectedAddToCardVenueModel.getItems();
        items.remove(data);
        service_dboup.remove(data);
        checkOutAdapter.notifyDataSetChanged();
        setOrderCount();
        if (items.size() == 0) {
            AddToCardSingleTone.getInstance().addToCardArrayList.remove(selectedAddToCardVenueModel);
            getActivity().finish();
        }

        resetAppointmentDate();
    }

    public void onShowStaffPicker(int position, AddToCardServiceModel data) {
        tempEmpolyees.clear();
        selectedItemIndex = position;
        showStaffPicker();

    }

    public void showStaffPicker() {

        staffDialog.show();
        tempEmpolyees.clear();
        if (hashEmployes != null) {
            AddToCardServiceModel addToCardServiceModel = service_dboup.get(selectedItemIndex);
            ArrayList list = hashEmployes.get(addToCardServiceModel.getServiceId2());
            if (list != null)
                tempEmpolyees.addAll(list);
        }

        staffListAdapter.notifyDataSetChanged();
    }

    ArrayList tempEmpolyees;

    public void initStafPicker() {

        /*set the tag for book now button */
        Context context = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflateralert = LayoutInflater.from(context);
        alertView = inflateralert.inflate(R.layout.staffpicker, null);
        builder.setView(alertView);

        staffListView = (ListView) alertView.findViewById(R.id.staffList);
        Button closeButton = (Button) alertView.findViewById(R.id.cross_image_addto_card);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffDialog.dismiss();
            }
        });

        builder.setCancelable(true);
        staffDialog = builder.create();

        if (tempEmpolyees == null)
            tempEmpolyees = new ArrayList();


        staffListAdapter = new StaffListAdapter(getActivity(), tempEmpolyees);
        staffListView.setAdapter(staffListAdapter);

        staffListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                staffDialog.dismiss();
                try {


                    if (tempEmpolyees.size() > position) {
                        KAStaff objStaff = (KAStaff) tempEmpolyees.get(position);
                        if (objStaff.isAvailable) {
                            AddToCardServiceModel addToCardServiceModel = service_dboup.get(selectedItemIndex);
                            addToCardServiceModel.staffname = objStaff.firstName;
                            addToCardServiceModel.staffID = objStaff.empid;
                            addToCardServiceModel.staffthumbImage = objStaff.imgUrlThumb;
                            checkOutAdapter.notifyDataSetChanged();
                            validateStaff();
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    boolean isCorrectTime = true;

    private void validateCheckout() {

        if (isCorrectTime == true) {
            //btnCheckout.backgroundColor =  UIColor(hexString: "#0097DB")
            //btnCheckout.enabled = true

        } else {
            //btnCheckout.backgroundColor = UIColor.lightGrayColor()
            //btnCheckout.enabled = false
        }
        validateStaff();

    }

    private void validateStaff() {

        boolean isValid = false;
        for (int i = 0; i < service_dboup.size(); i++) {
            AddToCardServiceModel addToCardServiceModel = service_dboup.get(i);
            if (addToCardServiceModel.isOpen && addToCardServiceModel.isValid) {
                if (addToCardServiceModel.staffname == null || addToCardServiceModel.staffname.length() == 0) {

                    //Toast message
                    isValid = false;

                    break;
                }
                if (i == service_dboup.size() - 1)
                    isValid = true;
            } else {
                isValid = false;
                break;
            }

        }


        if (isValid) {
            checkOutButton.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorCheckout));
            checkOutButton.setEnabled(true);

        } else {
            //0097DB
            checkOutButton.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorGrey));
            checkOutButton.setEnabled(false);
        }


    }


    private void getStaffDetails() {


        for (int i = 0; i < service_dboup.size(); i++) {
            ArrayList arrEmployes = new ArrayList();

            AddToCardServiceModel addToCardServiceModel = service_dboup.get(i);
            addToCardServiceModel.staffname="";
            addToCardServiceModel.staffthumbImage="";
            addToCardServiceModel.staffID="";
            String empID = addToCardServiceModel.getServiceId2();
            String url = Constant.HOST + "api/v1/service/" + empID + "/employees";

            RequestFuture<JSONArray> future = RequestFuture.newFuture();
            KAJsonArrayRequest jsonObjReq = new KAJsonArrayRequest(Request.Method.GET, url, null, future, future) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");

                    return params;
                }
            };
            ;
            try {
                KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);

                JSONArray arrResponse = future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);


                Log.d("GET STAFF", arrResponse.toString());
                for (int em = 0; em < arrResponse.length(); em++) {
                    try {


                        JSONObject dictStaf = arrResponse.getJSONObject(em);
                        KAStaff objStaff = new KAStaff();
                        objStaff.empid = dictStaf.getString("id");
                        objStaff.firstName = SafeParser.getString(dictStaf, "first_name", "");
                        objStaff.lastName = SafeParser.getString(dictStaf, "last_name", "");
                        objStaff.gender = SafeParser.getString(dictStaf, "gender", "");
                        objStaff.email = SafeParser.getString(dictStaf, "email", "");
                        objStaff.businesName = SafeParser.getString(dictStaf, "business_name", "");
                        objStaff.business = SafeParser.getString(dictStaf, "business", "");

                        if (arrMissEmpIds.indexOf(objStaff.empid) > -1)
                            objStaff.isAvailable = true;
                        else {
                            if (service_dboup.size() == 1)
                                objStaff.isAvailable = true;
                            else
                                objStaff.isAvailable = false;
                        }


                        try {
                            if (dictStaf.has("profile_image") && dictStaf.get("profile_image") instanceof JSONObject && dictStaf.getJSONObject("profile_image") != null) {

                                JSONObject dictProfileImg = dictStaf.getJSONObject("profile_image");
                                if (dictProfileImg != null) {
                                    objStaff.imgUrlThumb = SafeParser.getString(dictProfileImg, "thumb", "");
                                    objStaff.imgUrlMedium = SafeParser.getString(dictProfileImg, "medium", "");
                                    objStaff.imgUrlNormal = SafeParser.getString(dictProfileImg, "url", "");
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        arrEmployes.add(objStaff);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                hashEmployes.put(empID, arrEmployes);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    boolean isAvailabiltyRunning=false;
    private void checkAvailability(final String datestr) {

        if(isAvailabiltyRunning==true) return;
        new AsyncTask<Void, Void, String>() {

            @Override
            protected void onPreExecute() {
                isAvailabiltyRunning=true;
                showpDialog();
            }

            @Override
            protected String doInBackground(Void... params) {

                getAvailableEmployIds(datestr);

                if (hashEmployes == null)
                    hashEmployes = new LinkedHashMap<String, ArrayList>();
                else
                    hashEmployes.clear();
                showDialogOnMainThread();
                getStaffDetails();

                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                isAvailabiltyRunning=false;
                hideDialogOnMainThread();


            }
        }.execute(null, null, null);
    }

    private void hideDialogOnMainThread() {
        Handler mainHandler = new Handler(KalendriaAppController.getInstance().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                hidepDialog();
            } // This is your code
        };
        mainHandler.post(myRunnable);
    }
    private void hideDialogOnMainThread2() {
        Handler mainHandler = new Handler(KalendriaAppController.getInstance().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                hidepDialog1();
            } // This is your code
        };
        mainHandler.post(myRunnable);
    }
    private void showDialogOnMainThread() {
        Handler mainHandler = new Handler(KalendriaAppController.getInstance().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                showpDialog();
            } // This is your code
        };
        mainHandler.post(myRunnable);
    }

    public int REQUEST_TIMEOUT = 30;
    ArrayList arrEmpIds = new ArrayList();
    ArrayList arrMissEmpIds = new ArrayList();

    private void getAvailableEmployIds(String datestr) {
        // showpDialog();
        String url = Constant.HOST + "api/v1/external/available";

        JSONObject parameter = new JSONObject();
        JSONArray arrSevices = new JSONArray();
        for (int i = 0; i < service_dboup.size(); i++) {
            try {


                AddToCardServiceModel addToCardServiceModel = service_dboup.get(i);
                String sid = addToCardServiceModel.getServiceId2();

                arrSevices.put(sid);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        try {

            parameter.put("services", arrSevices);
            parameter.put("date", datestr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Log.d("CheckOut", "External :" + parameter.toString());
        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        KAJsonArrayRequest jsonObjReq = new KAJsonArrayRequest(Request.Method.POST, url, parameter, future, future) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };


        try {

            KalendriaAppController.getInstance().addToRequestQueue(jsonObjReq);
            JSONArray arrResponse = future.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
            if (arrResponse != null) {

                arrMissEmpIds.clear();

                for (int i = 0; i < arrResponse.length(); i++) {

                    JSONObject dictEmp = arrResponse.getJSONObject(i);
                    String empID = SafeParser.getString(dictEmp, "employee", "");
                    if (empID != null && empID.length() > 0) {
                        int index = arrEmpIds.indexOf(empID);
                        if (arrMissEmpIds.indexOf(empID) == -1)
                            arrMissEmpIds.add(empID);
                    }
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    String TAG = "CheckOutFragment";

    private void getWorkingHours() {

        showpDialog();
        String venueID = Constant.getVenueId(getActivity());
        String url = Constant.HOST + "api/v1/business/" + venueID + "?populate=medias,working_hours,region,city";
        //System.out.println("VeneItemFragement-->"+url);
        Log.d(TAG, url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response response will be a json object
                    if (response != null) {

                        if (dictBusiness == null)
                            dictBusiness = new LinkedHashMap();

                        dictBusiness.put(response.getString("name"), "name");// get the Venue Name
                        dictBusiness.put(response.getString("overall_rating"), "overall_rating");


                        /* set ratting particular venue page end  */
                        if (response.has("city") && response.getJSONObject("city") != null) {
                            JSONObject cityObject = response.getJSONObject("city");
                            String city = SafeParser.getString(cityObject, "name", "");
                            dictBusiness.put(city, "city");
                        }

                        if (response.has("region") && response.getJSONObject("region") != null) {
                            JSONObject cityObject = response.getJSONObject("region");
                            String city = SafeParser.getString(cityObject, "name", "");
                            dictBusiness.put(city, "city");
                        }
                        dictBusiness.put(SafeParser.getString(response, "phone", ""), "phone");
                        dictBusiness.put(SafeParser.getString(response, "email", ""), "email");
                        dictBusiness.put(SafeParser.getString(response, "website", ""), "website");


                        Date dNow = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat("EEEE");
                        String todayDay = ft.format(dNow);
                        boolean isTodayleave = false;
                        JSONArray jsonArray = response.getJSONArray("working_hours");
                        if (dictWorkingHours == null)
                            dictBusiness = new LinkedHashMap();
                        else
                            dictBusiness.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                VenueDay selectedVenuDay = new VenueDay();

                                String dayStr = jsonObject.getString("day");

                                selectedVenuDay.setDay(dayStr);

                                selectedVenuDay.setOpen(SafeParser.getBoolen(jsonObject, "isOpen", false));

                                selectedVenuDay.setStart_time(jsonObject.getString("start_time"));
                                selectedVenuDay.setEnd_time(jsonObject.getString("end_time"));
                                String orderStr = jsonObject.getString("order");
                                if (orderStr != null)
                                    selectedVenuDay.order = Integer.parseInt(orderStr);
                                else
                                    selectedVenuDay.order = 0;

                                if (dayStr.equalsIgnoreCase(todayDay))
                                    isTodayleave = true;

                                dictWorkingHours.put(dayStr, selectedVenuDay);


                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }

                        weekAdapter.dictWorkingHours=dictWorkingHours;
                        if (selectedDay != null && selectedDay.dayLongName != null)
                            loadAvailableTimes(true, selectedDay.dayLongName);
                        else
                            loadAvailableTimes(checkCurrentDay, KADate.todayLongName());


                    } else {
                        // if responce is null write your commants here
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    // hidepDialog();
                    isInitalload = false;
                    hidepDialog();;
                    resetAppointmentDate();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                //  hidepDialog();
                hidepDialog();;
                isInitalload = false;
                resetAppointmentDate();
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
