package com.kalendria.kalendria.adapter;

import android.content.Context;
import android.graphics.Color;
import android.security.KeyChainAliasCallback;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.auth.TokenData;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.fragment.CheckoutFragment;
import com.kalendria.kalendria.model.TimeBean;
import com.kalendria.kalendria.utility.KalendriaAppController;

import java.util.ArrayList;

/**
 * Created by chandirabalan on 5/11/2016.
 */
public class CustomAdapter extends BaseAdapter  {

    Context context;
    ArrayList<TimeBean> mylist ;
    LayoutInflater inflater;
    OnItemClickListener mItemClickListener;

    public int totalAmount;
    public int selectedIndex;
    public boolean isLoyality;
    public CustomAdapter(Context context, ArrayList<TimeBean> mylist) {
        this.context = context;
        this.mylist = mylist;
        inflater = (LayoutInflater.from(context));
        selectedIndex=0;

    }
    public void changeDatasource( ArrayList<TimeBean> mylist)
    {
        this.mylist=mylist;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public TimeBean getItem(int i) {
        return mylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        MyViewHolder mViewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.mytextview, viewGroup, false);
            mViewHolder = new MyViewHolder(view);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) view.getTag();
        }

        final TimeBean currentListData = getItem(i);

        mViewHolder.tvTitle.setText(currentListData.getCal_time());
        if(isLoyality)
        mViewHolder.tvAmount.setText(""+totalAmount+" Points");
        else
            mViewHolder.tvAmount.setText(""+totalAmount+" AED");
        if(selectedIndex==i)
        {
            view.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorSkyBlue));
            mViewHolder.tvTitle.setTextColor(Color.WHITE);
            mViewHolder.tvAmount.setTextColor(Color.WHITE);
        }
        else
        {
            view.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorWhite));
            mViewHolder.tvTitle.setTextColor(Color.BLACK);
            mViewHolder.tvAmount.setTextColor(Color.BLACK);
        }

        final int position = i;

        //mViewHolder.tvTitle.setOnClickListener(this);
        mViewHolder.tvTitle.setTag(""+i);

        mViewHolder.tvTitle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return view;

    }


    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(TimeBean currentListData, int position);
    }

    private class MyViewHolder {
        TextView tvTitle;
        TextView tvAmount;

        public MyViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.tv);
            tvAmount  = (TextView) item.findViewById(R.id.tvamount);
        }
    }
}
