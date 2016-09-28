package com.kalendria.kalendria.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kalendria.kalendria.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by murugan on 08/03/16.
 */
public class RegisterSpinner extends BaseAdapter {

    public List<com.kalendria.kalendria.model.RegisterSpinner> _data;
    private ArrayList<com.kalendria.kalendria.model.RegisterSpinner> arraylist;
    Context _c;
    ViewHolder v;


    public RegisterSpinner(List<com.kalendria.kalendria.model.RegisterSpinner> selectUsers, Context context) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<com.kalendria.kalendria.model.RegisterSpinner>();
        this.arraylist.addAll(_data);
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
            view = li.inflate(R.layout.register_spinner, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.title = (TextView) view.findViewById(R.id.company);


        final com.kalendria.kalendria.model.RegisterSpinner data = (com.kalendria.kalendria.model.RegisterSpinner) _data.get(i);
        v.title.setText(data.getName());




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
            for (com.kalendria.kalendria.model.RegisterSpinner wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    static class ViewHolder {
        TextView title;

    }
}
