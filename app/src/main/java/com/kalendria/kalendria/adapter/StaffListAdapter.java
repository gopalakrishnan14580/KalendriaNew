package com.kalendria.kalendria.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.KAStaff;
import com.kalendria.kalendria.model.ReviewModel;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class StaffListAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private ArrayList staffList;
	ImageLoader imageLoader = KalendriaAppController.getInstance().getImageLoader();
	Activity activity = (Activity) context;

	public StaffListAdapter(Context context, ArrayList staffList) {
		this.context = context;
		this.staffList = staffList;
		//this.delegate=delegate;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return staffList.size();
	}

	@Override
	public Object getItem(int location) {
		return staffList.get(location);
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
		
			convertView=inflater.inflate(R.layout.stafflistrow, parent, false);
			holder.staffName = (TextView) convertView.findViewById(R.id.staffname);
			holder.staffImage = (ImageView) convertView.findViewById(R.id.staffimage);

			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}

		final KAStaff objStaff = (KAStaff)staffList.get(position);

		if(objStaff.isAvailable)
			holder.staffName.setText(objStaff.firstName);
		else
			holder.staffName.setText(objStaff.firstName+" (Unavailable)");
	
		if (imageLoader == null)
			imageLoader = KalendriaAppController.getInstance().getImageLoader();


		if( objStaff.imgUrlThumb != null && !"".equals(objStaff.imgUrlThumb) ){
			try {
				Transformation transformation = new RoundedTransformationBuilder()
						.borderColor(Color.GRAY)
						.borderWidthDp(1)
						.cornerRadiusDp(30)
						.oval(false)
						.build();
				Picasso.with(context)
                        .load(objStaff.imgUrlThumb)
						//.fit()
						.transform(transformation)
                        .resize(40, 40)
                        // .error(R.drawable.login)
                        .placeholder(R.drawable.avatar)
                        .noFade()
                        // .fit().centerCrop()
                        .into(holder.staffImage );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 else
			holder.staffImage.setImageResource(R.drawable.avatar);


		return convertView;
	}
	
	static class Viewholder{
		TextView staffName;
		ImageView staffImage;

		
	}


}