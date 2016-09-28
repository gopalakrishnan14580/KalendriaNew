package com.kalendria.kalendria.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.AddToCardServiceModel;

import java.util.List;

@SuppressLint("InflateParams")
public class AppointmentConfirmationAdapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	private List<AddToCardServiceModel> appointmentConfirmationList;


	public AppointmentConfirmationAdapter(Context context, List<AddToCardServiceModel> appointmentConfirmationList) {
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
		
			convertView=inflater.inflate(R.layout.appointment_confirmation_row, parent, false);

			holder.appointment_service_name_tv = (TextView)convertView.findViewById(R.id.appointment_service_name_tv);
			holder.appointment_service_time_tv = (TextView)convertView.findViewById(R.id.appointment_service_time_tv);
			holder.appointment_duration_tv = (TextView)convertView.findViewById(R.id.appointment_duration_tv);
			holder.appointment_service_staff_name_tv = (TextView)convertView.findViewById(R.id.appointment_service_staff_name_tv);
			holder.appointment_service_price_tv= (TextView)convertView.findViewById(R.id.appointment_service_price_tv);

			convertView.setTag(holder);
		}else {
			holder=(Viewholder)convertView.getTag();
		}

		holder.appointment_service_name_tv.setText(appointmentConfirmationList.get(position).getServiceName());
		holder.appointment_service_time_tv.setText(appointmentConfirmationList.get(position).selectedTime);
		holder.appointment_duration_tv.setText("Time: " + appointmentConfirmationList.get(position).getServiceDuration());
		holder.appointment_service_staff_name_tv.setText("Staff: " + appointmentConfirmationList.get(position).getstaffname());
		holder.appointment_service_price_tv.setText(appointmentConfirmationList.get(position).getOriginalDisplayPrices());



		return convertView;
	}
	
	static class Viewholder{


		TextView appointment_service_name_tv, appointment_service_staff_name_tv, appointment_service_time_tv, appointment_duration_tv, appointment_service_price_tv;
		
	}


}