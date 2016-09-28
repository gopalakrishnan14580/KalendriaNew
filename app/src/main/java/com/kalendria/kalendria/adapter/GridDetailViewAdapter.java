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
public class GridDetailViewAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	KAGift objGift;
	private String[] cafeList={"Receipent Name","Code","Email","Type","Received/Purchased","Address","Phone","Message","Remaining Amount","Amount","Discount Type","Expiry","Status"};

	ImageLoader imageLoader = KalendriaAppController.getInstance().getImageLoader();
	Activity activity = (Activity) context;


	public GridDetailViewAdapter(Context context, KAGift objGift) {
		this.context = context;
		this.objGift = objGift;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return cafeList.length;
	}

	@Override
	public Object getItem(int location) {
		return cafeList[location];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public String getValue(int position)
	{
		switch (position) {

			case 0:
				return objGift.receipnt_Name;
			case 1:
				return objGift.code;

			case 2:
				return objGift.email;

			case 3:
				return objGift.type;

			case 4:
				return objGift.ReceiverType;

			case 5:
				return objGift.address;

			case 6:
				return objGift.phone;

			case 7:
				return objGift.message;

			case 8:
				return objGift.remainAmt;
			//arrGiftValue.addObject(dictGift.valueForKey("value")as !String)
			case 9:
				return objGift.amount;
			//arrGiftValue.addObject(dictGift.valueForKey("amount")as !String)
			case 10:
				return objGift.discount_type;

			case 11:
				return objGift.expiryAt;

			case 12:
				return objGift.status;
			default:return "";

		}

	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		Viewholder holder;
		if(convertView==null){
			holder=new Viewholder();
		
			convertView=inflater.inflate(R.layout.giftdetail_row, parent, false);
			holder.textLabel = (TextView) convertView.findViewById(R.id.gift_label);
			holder.textValue = (TextView) convertView.findViewById(R.id.gift_type);

			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}

		holder.textLabel.setText(cafeList[position]);
		holder.textValue.setText(getValue(position));

		if(position==1) {
			holder.textValue.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					TextView ed = (TextView) v;
					ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
					cm.setText(ed.getText());
					Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

				}
			});
		}
		else
		holder.textValue.setOnClickListener(null);

		return convertView;
	}



	static class Viewholder{
		TextView textLabel ,textValue;

	}


}