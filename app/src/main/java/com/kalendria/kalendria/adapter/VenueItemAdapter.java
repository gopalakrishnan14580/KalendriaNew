package com.kalendria.kalendria.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kalendria.kalendria.model.VenueDay;
import com.kalendria.kalendria.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mansoor on 11/03/16.
 */
public class VenueItemAdapter extends BaseAdapter {

    public List<VenueDay> _data;
    private ArrayList<VenueDay> arraylist;
    Context _c;
    ViewHolder v;


    public VenueItemAdapter(List<VenueDay> selectUsers, Context context) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<VenueDay>();
        this.arraylist.addAll(_data);
        System.out.println("hi i am from opeining houres");
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.selectedvenuchild_day, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.day = (TextView) view.findViewById(R.id.day);
        v.starting_time = (TextView) view.findViewById(R.id.starting_time);
        v.end_time = (TextView) view.findViewById(R.id.end_time);


        final VenueDay data = (VenueDay) _data.get(i);
        v.day.setText(data.getDay());
        //-- Code Modifed by Magesh
        if(data.isOpen) {
            v.starting_time.setText(data.getStart_time() + "-" + data.getEnd_time());
            v.end_time.setText(data.getEnd_time());
            v.starting_time.setTextColor(Color.BLACK);
        }
        else {
            //#FF6667
            v.starting_time.setTextColor(Color.parseColor("#FF6667"));
            v.starting_time.setText("Closed");
            v.end_time.setText("");
        }
       // Toast.makeText(_c,data.getDay(),Toast.LENGTH_LONG).show();


        view.setTag(data);
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (VenueDay wp : arraylist) {
                if (wp.getDay().toLowerCase(Locale.getDefault()).contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {

        TextView day, starting_time, end_time;

    }
}

