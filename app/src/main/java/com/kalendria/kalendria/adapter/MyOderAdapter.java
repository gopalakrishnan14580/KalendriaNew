package com.kalendria.kalendria.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.FavorateModel;
import com.kalendria.kalendria.model.MyorderModel;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressLint("InflateParams")
public class MyOderAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private List<MyorderModel> cafeList;


	public MyOderAdapter(Context context, List<MyorderModel> cafeList) {
		this.context = context;
		this.cafeList = cafeList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	SimpleDateFormat formatter= null;
	SimpleDateFormat formatter2=null;

	public String getDate(String scheudleDate) {
		if (formatter == null)
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ssss");
		if (formatter2 == null)
			formatter2 = new SimpleDateFormat("dd MMMM");
		try {

			if (scheudleDate != null) {
				Date date = formatter.parse(scheudleDate);
				return formatter2.format(date);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}

		return "";
	}

	@Override
	public int getCount() {
		return cafeList.size();
	}

	@Override
	public Object getItem(int location) {
		return cafeList.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		Viewholder holder;
		if(convertView==null){
			holder=new Viewholder();
		
			convertView=inflater.inflate(R.layout.myorder_row, parent, false);
			holder.myOrder_VenueName = (TextView)convertView.findViewById(R.id.myorder_venu_name);
			holder.myOrder_ServiceName = (TextView)convertView.findViewById(R.id.myorder_service_name);
			holder.myOrder_Date = (TextView)convertView.findViewById(R.id.myorder_date);
			holder.myOrder_Time = (TextView)convertView.findViewById(R.id.myorder_time);
			holder.myOrder_Price_Point = (TextView)convertView.findViewById(R.id.myorder_price_or_points);
			holder.myOrder_Status = (TextView)convertView.findViewById(R.id.myorder_status);

			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}

			holder.myOrder_VenueName.setText(cafeList.get(position).getMorderVenuName());
			holder.myOrder_ServiceName.setText(cafeList.get(position).getMorderServiceName());
			holder.myOrder_Date.setText(getDate( cafeList.get(position).getMorderDate()));

		// Code modified by Magesh
		MyorderModel model = cafeList.get(position);
			holder.myOrder_Time.setText(cafeList.get(position).getMorderTime());
		if(!cafeList.get(position).getMorderPoints().equals("")){
			holder.myOrder_Price_Point.setText(cafeList.get(position).getMorderPoints());
		}
		if(!cafeList.get(position).getMorderPrice().equals("")){
			holder.myOrder_Price_Point.setText(cafeList.get(position).getMorderPrice());
		}

		holder.myOrder_Status.setText(model.getMorderStatus());
		//Log.d("My Order:","STAUTS: "+model.getMorderStatus());


		Drawable background = holder.myOrder_Status.getBackground();

		if(model.getMorderStatus().equalsIgnoreCase("Cancelled") || model.getMorderStatus().equalsIgnoreCase("No Show")){
			setBackgroundColor(background,"#F05050");
			//holder.myOrder_Status.setBackgroundColor(Color.parseColor("#F05050"));

		}
		else  if(model.getMorderStatus().equalsIgnoreCase("Confirmed")){
			//holder.myOrder_Status.setBackgroundColor(Color.parseColor("#7266BA"));
			setBackgroundColor(background,"#7266BA");
		}
		else
			setBackgroundColor(background,"#27C24C");
			//holder.myOrder_Status.setBackgroundColor(Color.parseColor("#27C24C"));

		String price = model.getMorderPrice();
		String dis = model.mOrderDiscount;
		String points = model.getMorderPoints();

		int priceAmt = 0;
		int dictAmt = 0;

		if(price != null){
			try {
				priceAmt = Integer.parseInt(price);
			}
			catch (Exception ex){}

		}

		if(dis != null){
			try {
				dictAmt = Integer.parseInt(dis);
			}
			catch (Exception ex){}

		}



		double calAmt1 = ((double)(priceAmt) / 100);
		Double calAmt = calAmt1 * (double)(dictAmt);

		int discountAmount = calAmt.intValue();
		int remainAmount = 0;


		remainAmount = priceAmt - discountAmount;
		String remainAmtString = remainAmount + " " + "AED";


		String pay_at = model.mOrderPayAt;

		holder.myOrder_Price_Point.setText("");
		if(pay_at.equalsIgnoreCase("points")){
			if(points != null){
				holder.myOrder_Price_Point.setText( points + " Points");
			}
		}else{
			holder.myOrder_Price_Point.setText( remainAmtString+"");

		}

//End

		//holder.myOrder_Status.setText(cafeList.get(position).getMorderStatus());

		return convertView;
	}

	private void setBackgroundColor(Drawable background, String color) {
		if (background instanceof ShapeDrawable) {
			((ShapeDrawable) background).getPaint().setColor(Color.parseColor(color));
		} else if (background instanceof GradientDrawable) {
			((GradientDrawable) background).setColor(Color.parseColor(color));
		} else if (background instanceof ColorDrawable) {
			((ColorDrawable) background).setColor(Color.parseColor(color));
		}

	}
	static class Viewholder{


		TextView myOrder_Date, myOrder_Time,
				myOrder_VenueName,
				myOrder_ServiceName,
				myOrder_Price_Point,
				myOrder_Status;
		
	}


}