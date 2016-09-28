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
import com.kalendria.kalendria.model.ReviewPostModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by murugan on 08/03/16.
 */
public class PostReviewAdapter extends BaseAdapter {

    public List<ReviewPostModel> _data;
    private ArrayList<ReviewPostModel> arraylist;
    Context _c;
    ViewHolder v;


    public PostReviewAdapter(List<ReviewPostModel> selectUsers, Context context) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<ReviewPostModel>();
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

        } else {
            view = convertView;

        }

        v = new ViewHolder();

        v.title = (TextView) view.findViewById(R.id.company);


        final ReviewPostModel data = (ReviewPostModel) _data.get(i);
        v.title.setText(data.getServiceName());




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
            for (ReviewPostModel wp : arraylist) {
                if (wp.getServiceName().toLowerCase(Locale.getDefault()).contains(charText)) {
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
