package com.kalendria.kalendria.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mansoor on 11/03/16.
 */
public class CheckOutAdapter extends BaseAdapter {

    public List<AddToCardServiceModel> _data;
    private ArrayList<AddToCardServiceModel> arraylist;
    CheckOutAdapterDelegate delegate;
    Context _c;
    ViewHolder v;
    public interface CheckOutAdapterDelegate
    {
        public void onShowStaffPicker(int position,AddToCardServiceModel data);
        public void onDeleteService(int position,AddToCardServiceModel data);
    }

    public CheckOutAdapter(List<AddToCardServiceModel> selectUsers, Context context,CheckOutAdapterDelegate delegate) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<AddToCardServiceModel>();
        this.arraylist.addAll(_data);
        this.delegate=delegate;
        System.out.println("i am from selected adapter page" + _data.size());
        //Toast.makeText(_c, "hi", Toast.LENGTH_LONG).show();
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
            view = li.inflate(R.layout.checkoutinclude, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.imageView = (ImageView) view.findViewById(R.id.imavenu);
        v.service_nameLable = (TextView) view.findViewById(R.id.service_nameLable);
        v.service_duration = (TextView) view.findViewById(R.id.service_duration);
        v.service_price = (TextView) view.findViewById(R.id.service_price);
        v.service_deiscount = (TextView) view.findViewById(R.id.service_deiscount);
        v.service_date = (TextView) view.findViewById(R.id.service_date);
        v.service_time = (TextView) view.findViewById(R.id.service_time);
        v.staffName = (TextView) view.findViewById(R.id.staffName);
        v.staffRow = (TableRow)view.findViewById(R.id.staffrow);
        v.staffImageView = (ImageView) view.findViewById(R.id.thumbnail);
        v.swipeLayout = (SwipeLayout)view.findViewById(R.id.swipe_layout);
        v.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        v.select_staff_ll=(LinearLayout)view.findViewById(R.id.select_staff_ll);
        v.category_autocompleted_btn = (ImageButton)view.findViewById(R.id.category_autocompleted_btn);

        v.deleteImage = (ImageView) view.findViewById(R.id.delete);
        v.deleteImage.setOnClickListener(onDeleteServiceListener(i, v));

        final AddToCardServiceModel data = (AddToCardServiceModel) _data.get(i);
        v.service_nameLable.setText(data.getServiceName());
        v.service_duration.setText(data.getServiceDuration());

        v.service_date.setText(data.selectedDate);
        v.service_time.setText(data.selectedTime);
        if(data.selectedTime!=null)
            Log.d("selectedTime",data.selectedTime);

        if(data.remainAmount==0)
            data.calculateDiscountAmount();//for additional calculation

        if(data.isFromLoyality)
        {

            v.service_price.setText("");
            v.service_deiscount.setText(data.getServicePoints()+" Points");
        }
        else {
            v.service_price.setPaintFlags(v.service_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            v.service_price.setText(data.getStrikeOutAmount());

            v.service_deiscount.setText(data.getOriginalDisplayPrices());
        }

        if(data.getstaffname().length()==0) {
            v.staffName.setText("Select staff");
            v.staffName.setTextColor(Color.RED);
        }
        else {

            v.staffName.setText(data.getstaffname());
            v.staffName.setTextColor(Color.BLACK);
        }

        final int pos = i;
        v.staffName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delegate.onShowStaffPicker(pos,data);
            }
        });
         v.category_autocompleted_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 delegate.onShowStaffPicker(pos,data);
             }
         });

        if(data.getstaffname().length()>0) {
            if (data.staffthumbImage != null && data.staffthumbImage.length()>4) {
                try {
             Transformation transformation = new RoundedTransformationBuilder()
                                .borderColor(Color.GRAY)
                                .borderWidthDp(1)
                                .cornerRadiusDp(30)
                                .oval(false)
                                .build();
                        Picasso.with(_c)
                                .load(data.staffthumbImage)
                                //.fit()
                                .transform(transformation)
                                .resize(40, 40)
                                        // .error(R.drawable.login)
                                .placeholder(R.drawable.avatar)
                                .noFade()
                                        // .fit().centerCrop()
                                .into(v.staffImageView );

                } catch (Exception e) {
                    v.staffImageView.setImageResource(R.drawable.avatar);
                    e.printStackTrace();
                }

            } else
                v.staffImageView.setImageResource(R.drawable.avatar);

            v.staffImageView.setVisibility(View.VISIBLE);
        }
        else
            v.staffImageView.setVisibility(View.INVISIBLE);

        if(!data.isValid || !data.isOpen)
            v.service_time.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorRoseRed));
        else
            v.service_time.setBackgroundColor(KalendriaAppController.getInstance().getResources().getColor(R.color.colorLightGreen));





        view.setTag(data);
        return view;
    }


    private View.OnClickListener onDeleteServiceListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AddToCardServiceModel data = (AddToCardServiceModel) _data.get(position);
                holder.swipeLayout.close();
                delegate.onDeleteService(position, data);
                //items.remove(position);

               // activity.updateMember(member);
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
            for (AddToCardServiceModel wp : arraylist) {
                if (wp.getServiceId().toLowerCase(Locale.getDefault()).contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {

        TextView service_nameLable, service_duration, service_price, service_deiscount, service_date, service_time, staffName;
        ImageView imageView;
        TableRow staffRow;
        ImageView staffImageView;
        SwipeLayout swipeLayout;
        View deleteImage;
        LinearLayout select_staff_ll;
        ImageButton category_autocompleted_btn;


    }
}
