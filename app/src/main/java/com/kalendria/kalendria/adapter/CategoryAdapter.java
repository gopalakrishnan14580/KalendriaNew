package com.kalendria.kalendria.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalendria.kalendria.model.Category;
import com.kalendria.kalendria.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by murugan on 02/03/16.
 */
public class CategoryAdapter extends BaseAdapter {

    public List<Category> _data;
    private ArrayList<Category> arraylist;
    Context _c;
    ViewHolder v;


    public CategoryAdapter(List<Category> selectUsers, Context context) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<Category>();
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
            view = li.inflate(R.layout.category_child, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.title = (TextView) view.findViewById(R.id.category_child_text);
        v.imageView = (ImageView) view.findViewById(R.id.category_child_images);

        final Category data = (Category) _data.get(i);
        v.title.setText(data.getCategoryName());

        if( data.getCategoryImages() != null && !"".equals(data.getCategoryImages()) ){
            //getName is valid
            try {
                Picasso.with(_c)
                        .load(data.getCategoryImages())
                        // .memoryPolicy(MemoryPolicy.NO_CACHE )
                        // .networkPolicy(NetworkPolicy.NO_CACHE)
                        //.resize(720, 350)
                        // .error(R.drawable.login)
                        .placeholder(R.drawable.logo_placeholder)
                        .noFade()
                         .fit()
                        .skipMemoryCache()
                        .into( v.imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


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
            for (Category wp : arraylist) {
                if (wp.getCategoryName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    static class ViewHolder {
        ImageView imageView;
        TextView title;

    }
}
