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
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.ReviewModel;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.squareup.picasso.Picasso;

import java.util.List;

@SuppressLint("InflateParams")
public class ReviewAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private List<ReviewModel> cafeList;
	ImageLoader imageLoader = KalendriaAppController.getInstance().getImageLoader();
	Activity activity = (Activity) context;


	public ReviewAdapter(Context context, List<ReviewModel> cafeList) {
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

			convertView=inflater.inflate(R.layout.review_row, parent, false);
			holder.review_username = (TextView) convertView.findViewById(R.id.review_username);
			holder.review_commants = (TextView) convertView.findViewById(R.id.review_commants);
			holder.review_responce_command = (TextView) convertView.findViewById(R.id.review_responce_command);
			holder.review_responce_username = (TextView) convertView.findViewById(R.id.review_responce_username);
			holder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
			holder.review_responce_image = (ImageView) convertView.findViewById(R.id.review_responce_image);
			holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rating);

			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}

		holder.review_username.setText(cafeList.get(position).getReviewUserName());
		holder.review_commants.setText(cafeList.get(position).getReviewCommants());


		//holder.rating.setText(cafeList.get(position).getRating()+"/5");
		float numstars = Float.parseFloat(cafeList.get(position).getReviewRatting());
		holder.ratingBar.setRating(numstars);

		LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();

		if(holder.ratingBar.getRating() > 0)
		{
			stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(1).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
			stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

		}

		if (imageLoader == null)
			imageLoader = KalendriaAppController.getInstance().getImageLoader();

		final ReviewModel cafeitems = cafeList.get(position);

		if( cafeitems.getReviewUserTampImage_url() != null && !"".equals(cafeitems.getReviewUserTampImage_url()) ){
			try {
				Picasso.with(context)
						.load(cafeitems.getReviewUserTampImage_url())
						// .memoryPolicy(MemoryPolicy.NO_CACHE )
						// .networkPolicy(NetworkPolicy.NO_CACHE)
						//.resize(720, 350)
						// .error(R.drawable.login)
						.placeholder(R.drawable.faverote_icon)
						.noFade()
						// .fit().centerCrop()
						.into(holder.imageView );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}



		if("".equals(cafeList.get(position).getReviewResponceUserCommands()) || "null".equals(cafeList.get(position).getReviewResponceUserCommands())){

			holder.review_responce_command.setVisibility(View.GONE);
			holder.review_responce_username.setVisibility(View.GONE);
			holder.review_responce_image.setVisibility(View.GONE);
		}else{
			holder.review_responce_command.setVisibility(View.VISIBLE);
			holder.review_responce_username.setVisibility(View.VISIBLE);
			holder.review_responce_image.setVisibility(View.VISIBLE);
			holder.review_responce_command.setText(cafeList.get(position).getReviewResponceUserName());
			holder.review_responce_username.setText(cafeList.get(position).getReviewResponceUserCommands());
			if(Constant.getVenuSelecedImageUrl(context) != null && !"".equals(Constant.getVenuSelecedImageUrl(context)) ){
				try {
					Picasso.with(context)
							.load(Constant.getVenuSelecedImageUrl(context))
							// .memoryPolicy(MemoryPolicy.NO_CACHE )
							// .networkPolicy(NetworkPolicy.NO_CACHE)
							//.resize(720, 350)
							// .error(R.drawable.login)
							.placeholder(R.drawable.faverote_icon)
							.noFade()
							// .fit().centerCrop()
							.into(holder.review_responce_image );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return convertView;
	}

	static class Viewholder{
		TextView review_username ,review_commants,review_responce_command,review_responce_username;
		ImageView imageView,review_responce_image;
		RatingBar ratingBar;


	}


}