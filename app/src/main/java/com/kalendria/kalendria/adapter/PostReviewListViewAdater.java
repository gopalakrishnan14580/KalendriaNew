package com.kalendria.kalendria.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.ReviewPostModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by murugan  on 1-03-2016.
 */
public class PostReviewListViewAdater extends BaseAdapter {

    public List<ReviewPostModel> _data;
    private ArrayList<ReviewPostModel> arraylist;
    Context _c;
    ViewHolder v;
    OnItemSelector mOnItemSelector;


    public PostReviewListViewAdater(List<ReviewPostModel> selectUsers, Context context,OnItemSelector mOnItemSelector) {
        _data = selectUsers;
        _c = context;

        this.arraylist = new ArrayList<ReviewPostModel>();
        this.arraylist.addAll(_data);
        this.mOnItemSelector = mOnItemSelector;
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
            view = li.inflate(R.layout.post_review_row_layout, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.title = (TextView) view.findViewById(R.id.listText);
        v.post_r = (LinearLayout) view.findViewById(R.id.post_r);
        v.post_review_array_retting_bar = (RatingBar) view.findViewById(R.id.post_review_array_retting_bar);


        final ReviewPostModel data = (ReviewPostModel) _data.get(i);
        v.title.setText(data.getServiceName());
        v.post_r.setTag(i);
        v.post_review_array_retting_bar.setTag(i);

        v.post_r.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(_c, "HI", Toast.LENGTH_SHORT).show();
                int pos = (int) v.getTag();

                mOnItemSelector.OnItemSelected(pos);
            }
        });

        v.post_review_array_retting_bar.setOnRatingBarChangeListener(onRatingChangedListener(v, i));

        view.setTag(data);
        return view;
    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

                //System.out.println("-->"+String.valueOf(v));
               /* Log.i("Adapter", "star: " + v);
                Log.i("Adapter", "star position: " + position);*/
                mOnItemSelector.OnItemSelected(position, String.valueOf(v));
            }
        };
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
        LinearLayout post_r;
        RatingBar post_review_array_retting_bar;

    }

    public interface OnItemSelector {

        void OnItemSelected(int postion, String imageName);

        void OnItemSelected(int postion);
    }
}
