package com.kalendria.kalendria.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.FavorateModel;
import com.kalendria.kalendria.utility.CircularNetworkImageView;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

@SuppressLint("InflateParams")
public class FavorateAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private List<FavorateModel> cafeList;
	ImageLoader imageLoader = KalendriaAppController.getInstance().getImageLoader();
	Activity activity = (Activity) context;


	public FavorateAdapter(Context context, List<FavorateModel> cafeList) {
		this.context = context;
		this.cafeList = cafeList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		
			convertView=inflater.inflate(R.layout.favorite_row, parent, false);
			holder.faverote_venu_name_txt = (TextView) convertView.findViewById(R.id.faverote_venu_name_txt);
			holder.faverote_review_count_txt = (TextView) convertView.findViewById(R.id.faverote_review_count_txt);
			holder.rating = (RatingBar) convertView.findViewById(R.id.rating);
			holder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
			holder.faverote_venu_location_txt = (TextView) convertView.findViewById(R.id.faverote_venu_location_txt);

			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}

			holder.faverote_venu_name_txt.setText(cafeList.get(position).getFavorateVenuName());
		if(!cafeList.get(position).getFavorateReview().equalsIgnoreCase("0"))
		{
			holder.faverote_review_count_txt.setText(cafeList.get(position).getFavorateReview()+" Review");
		}
		else holder.faverote_review_count_txt.setText("");

		holder.faverote_venu_location_txt.setText(cafeList.get(position).getLocation());

		//holder.rating.setText(cafeList.get(position).getRating()+"/5");
			float numstars = Float.parseFloat(cafeList.get(position).getFavorateRatting());
			holder.rating.setRating(numstars);

			LayerDrawable stars = (LayerDrawable) holder.rating.getProgressDrawable();
			
			if(holder.rating.getRating() > 0)
			{
				stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
				stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
				stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

			}


	
		if (imageLoader == null)
			imageLoader = KalendriaAppController.getInstance().getImageLoader();

		final FavorateModel cafeitems = cafeList.get(position);


		if( cafeitems.getFavorateImage_thumb() != null && !"".equals(cafeitems.getFavorateImage_thumb()) ){

			Transformation transformation = new RoundedTransformationBuilder()
					.borderColor(Color.GRAY)
					.borderWidthDp(1)
					.cornerRadiusDp(30)
					.oval(false)
					.build();

			Picasso.with(context)
					.load(cafeitems.getFavorateImage_thumb())
					.fit()
					.error(R.drawable.bg)
					.transform(transformation)
					.into(holder.imageView);

		}
		else {
			setDefaultImage(holder.imageView);
		}

		return convertView;
	}
	public void setDefaultImage(ImageView imageView)
	{
		Transformation transformation = new RoundedTransformationBuilder()
				.borderColor(Color.GRAY)
				.borderWidthDp(1)
				.cornerRadiusDp(30)
				.oval(false)
				.build();

		Picasso.with(context)
				.load(R.drawable.bg)
				.fit()
				.error(R.drawable.bg)
				.transform(transformation)
				.into(imageView);

		imageView.setImageResource(R.drawable.bg);
	}


	static class Viewholder{
		TextView faverote_venu_name_txt ,faverote_review_count_txt;
		TextView faverote_venu_location_txt;
		RatingBar rating;
		ImageView imageView ;
		
	}


}