package com.kalendria.kalendria.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.FavorateModel;
import com.kalendria.kalendria.model.KAGift;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

@SuppressLint("InflateParams")
public class GridListViewAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private List<KAGift> cafeList;

	Activity activity = (Activity) context;


	public GridListViewAdapter(Context context, List<KAGift> cafeList) {
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
		
			convertView=inflater.inflate(R.layout.giftlist_row, parent, false);
			holder.textCode = (TextView) convertView.findViewById(R.id.gift_code);
			holder.textRemaining = (TextView) convertView.findViewById(R.id.gift_remaining );
			holder.textType = (TextView) convertView.findViewById(R.id.gift_type);



			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}
		KAGift objGift = cafeList.get(position);

		holder.textCode.setText(objGift.code);
		holder.textType.setText(objGift.type);
		holder.textRemaining.setText(objGift.remainAmt);

		holder.textCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				TextView ed = (TextView)v;
				ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(ed.getText());
				Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

			}
		});

		return convertView;
	}


	static class Viewholder{
		TextView textCode, textType,textRemaining;

	}


}