package com.kalendria.kalendria.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kalendria.kalendria.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
	// Declare Variables
	Context context;
	ArrayList<String> imagesList;
	LayoutInflater inflater;
	public ViewPagerAdapter(Context context, ArrayList<String> flag) {
		this.context = context;
		this.imagesList = flag;
	}

	@Override
	public int getCount() {
		return imagesList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		// Declare Variables
		
		ImageView imgflag;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.viewpager_item, container,false);

		// Locate the ImageView in viewpager_item.xml
		imgflag = (ImageView) itemView.findViewById(R.id.flag);
		// Capture position and set to the ImageView
		//imgflag.setImageResource(flag[position]);
		if( imagesList.get(position) != null && !"".equals(imagesList.get(position)) ){
			try {
				Picasso.with(context)
						.load(imagesList.get(position))
						.error(R.drawable.bg)
						.into(imgflag);
			} catch (Exception e) {
				e.printStackTrace();
				setDefaultImage(imgflag);
			}
		}
		else setDefaultImage(imgflag);


		// Add viewpager_item.xml to ViewPager
		((ViewPager) container).addView(itemView);

		return itemView;
	}
	public void setDefaultImage(ImageView imageView)
	{
		imageView.setImageResource(R.drawable.bg);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((RelativeLayout) object);

	}
}
