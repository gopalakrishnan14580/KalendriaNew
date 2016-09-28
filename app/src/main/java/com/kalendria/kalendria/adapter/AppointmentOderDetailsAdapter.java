package com.kalendria.kalendria.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.AppointmentDetailModel;

import java.util.List;

@SuppressLint("InflateParams")
public class AppointmentOderDetailsAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private List<AppointmentDetailModel> appointmentConfirmationList;


	public AppointmentOderDetailsAdapter(Context context, List<AppointmentDetailModel> appointmentConfirmationList) {
		this.context = context;
		this.appointmentConfirmationList = appointmentConfirmationList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//Toast.makeText(context, "HI"+appointmentConfirmationList.size(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public int getCount() {
		return appointmentConfirmationList.size();
	}

	@Override
	public Object getItem(int location) {
		return appointmentConfirmationList.get(location);
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
		
			convertView=inflater.inflate(R.layout.appointment_details_row, parent, false);

			holder.service_name = (TextView)convertView.findViewById(R.id.service_name);
			holder.staff_name = (TextView)convertView.findViewById(R.id.staff_name);
			holder.day_date = (TextView)convertView.findViewById(R.id.day_date);
			holder.dourition_time = (TextView)convertView.findViewById(R.id.dourition_time);
			holder.confirm_oder= (TextView)convertView.findViewById(R.id.confirm_oder);

			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}

		holder.service_name.setText(appointmentConfirmationList.get(position).serviceName);
		holder.staff_name.setText("Staff: "+appointmentConfirmationList.get(position).empFirstName);
		holder.day_date.setText( appointmentConfirmationList.get(position).sheduleDay);
	//	String s = appointmentConfirmationList.get(position).getDurition().replace(".",":");
	//	System.out.println("totalTime"+s+"  "+appointmentConfirmationList.get(position).getDurition);
		holder.dourition_time.setText(appointmentConfirmationList.get(position).timing);
		String status =appointmentConfirmationList.get(position).status;
		holder.confirm_oder.setText( status);

		Drawable background = holder.confirm_oder.getBackground();
		if(status.equalsIgnoreCase("Cancelled") || status.equalsIgnoreCase("No Show")){
			setBackgroundColor(background,"#F05050");
			//holder.myOrder_Status.setBackgroundColor(Color.parseColor("#F05050"));

		}
		else  if(status.equalsIgnoreCase("Confirmed")){
			//holder.myOrder_Status.setBackgroundColor(Color.parseColor("#7266BA"));
			setBackgroundColor(background,"#7266BA");
		}
		else
			setBackgroundColor(background,"#27C24C");
			//holder.confirm_oder.setBackgroundColor(Color.parseColor("#3cbe51"));


		return convertView;
	}
	
	static class Viewholder{


		TextView service_name,staff_name,day_date,dourition_time,confirm_oder;
		
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

}