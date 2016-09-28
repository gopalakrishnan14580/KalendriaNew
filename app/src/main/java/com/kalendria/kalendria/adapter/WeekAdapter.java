package com.kalendria.kalendria.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.KADate;
import com.kalendria.kalendria.model.VenueDay;
import com.kalendria.kalendria.utility.KalendriaAppController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by mansoor on 02/05/16.
 */
public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {
    Context ctx;
    ArrayList<KADate> dataValue;
    ArrayList<String> dataname;

    public int todayUniqueID = 0;
    public int selectedIndex =-1;
    public LinkedHashMap dictWorkingHours;
    OnItemClickListener mItemClickListener;

    public WeekAdapter(Context context, ArrayList<KADate> data, ArrayList<String> data1,int indexvalue1) {
        ctx = context;
        dataValue = data;
        dataname = data1;


        Calendar calender = Calendar.getInstance(Locale.US);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        int month = calender.get(Calendar.MONTH)+1;// becoz it start from 0
        int year = calender.get(Calendar.YEAR);

        todayUniqueID = year*10000+month*100+day;//month*1000000+day*10000+year;
        selectedIndex=todayUniqueID;
    }

    public void refreshDates(Context context, ArrayList<KADate> data, ArrayList<String> data1,int indexvalue1)
    {
        ctx = context;
        dataValue = data;
        dataname = data1;
        // indexvalue = indexvalue1;
        // selectedIndex=indexvalue1;
    }
    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.days_row, parent, false);

        return new WeekViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {

      /*  int index = days.indexOf(weekViews.get(0).getCurrentDay());
        System.out.println("Current position :  " + index);*/

        KADate dateObject = dataValue.get(position);
        holder.days.setText(""+dateObject.day);
        holder.daysname.setText(dataname.get(position));

        try {
            if(selectedIndex==dateObject.unique)
            {
                holder.imageView.setBackgroundResource(R.drawable.button_circle);
                holder.days.setTextColor(Color.WHITE);
            }
            else if(todayUniqueID==dateObject.unique && selectedIndex!=position)
            {
                holder.imageView.setBackgroundResource(R.drawable.button_circle_white);
                holder.days.setTextColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorSkyBlue));
            }
            else if(dateObject.unique<todayUniqueID)
            {
                holder.imageView.setBackgroundColor(Color.TRANSPARENT);
                holder.days.setTextColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorLightTextColor2));
            }
            else {
                holder.days.setTextColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorSkyBlue));
                holder.imageView.setBackgroundColor(Color.TRANSPARENT);
            }

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        try {
            if (dictWorkingHours != null && dictWorkingHours.size() > 0) {
                VenueDay dayObject = (VenueDay) dictWorkingHours.get(dateObject.dayLongName.toLowerCase());
                if(dayObject!=null)
                {
                    if(!dayObject.isOpen)
                        holder.days.setTextColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorLightTextColor2));


                }
            }

        }
        catch (Exception ex){ex.printStackTrace();}


        /*
        if(position == indexvalue){
            holder.days.setBackgroundResource(R.drawable.button_circle);
            holder.days.setTextColor(Color.WHITE);
        }else  if(selectedIndex==position && position != indexvalue) {
            holder.days.setBackgroundResource(R.drawable.button_circle);
            holder.days.setTextColor(Color.WHITE);
        }
        */
    }

    @Override
    public int getItemCount() {
        return dataValue.size();
    }

    public class WeekViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView days;
        TextView daysname;
        ImageView imageView;

        public WeekViewHolder(View itemView) {
            super(itemView);
            days = (TextView) itemView.findViewById(R.id.daysvalue);
            daysname = (TextView) itemView.findViewById(R.id.daysname);
            imageView = (ImageView) itemView.findViewById(R.id.bgImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
