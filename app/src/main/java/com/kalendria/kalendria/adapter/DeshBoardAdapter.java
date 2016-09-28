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

import com.kalendria.kalendria.model.DeshBoard;
import com.kalendria.kalendria.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by murugan  on 1-03-2016.
 */
public class DeshBoardAdapter extends BaseAdapter {

    public List<DeshBoard> _data;
    private ArrayList<DeshBoard> arraylist;
    Context _c;
    ViewHolder v;


    public DeshBoardAdapter(List<DeshBoard> selectUsers, Context context) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<DeshBoard>();
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
            view = li.inflate(R.layout.homepage_child, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.title = (TextView) view.findViewById(R.id.type_name_txt);
        v.imageView = (ImageView) view.findViewById(R.id.beauty_image_view);

        final DeshBoard data = (DeshBoard) _data.get(i);
        v.title.setText(data.getTypeName());

        if( data.getType_image() != null && !"".equals(data.getType_image()) ){
            //getName is valid
            try {
                Picasso.with(_c)
                        .load(data.getType_image())
                        // .memoryPolicy(MemoryPolicy.NO_CACHE )
                        // .networkPolicy(NetworkPolicy.NO_CACHE)
                        //.resize(720, 350)
                        // .error(R.drawable.login)
                        .noFade()
                        // .fit().centerCrop()
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
            for (DeshBoard wp : arraylist) {
                if (wp.getTypeName().toLowerCase(Locale.getDefault()).contains(charText)) {
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
