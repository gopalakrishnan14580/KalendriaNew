package com.kalendria.kalendria.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.model.AppointmentDetailModel;

import java.util.List;

@SuppressLint("InflateParams")
public class AppointmentCancelServiceListAdapter extends BaseAdapter {

    public interface AppointmentCancelServiceListDelegate {
        void appointmentRadioButtonTapped(int position);
    }

    Context context;
    LayoutInflater inflater;
    private List<AppointmentDetailModel> appointmentConfirmationList;
    public AppointmentCancelServiceListDelegate delegate;


    public AppointmentCancelServiceListAdapter(Context context, List<AppointmentDetailModel> appointmentConfirmationList) {
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


        final Viewholder holder;
        if (convertView == null) {
            holder = new Viewholder();

            convertView = inflater.inflate(R.layout.appointment_details_cancel_row, parent, false);

            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);
            holder.frontButton = (Button) convertView.findViewById(R.id.frontButton);
            holder.cancelLayout = (LinearLayout) convertView.findViewById(R.id.cancel_layout);

            convertView.setTag(holder);
        } else {
            holder = (Viewholder) convertView.getTag();
        }

        holder.radioButton.setTag(position + "");

        holder.radioButton.setText(appointmentConfirmationList.get(position).serviceName);


        holder.frontButton.setTag(position + "");

       holder.frontButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(holder.radioButton.isEnabled()) {
                   String post2 = (String) v.getTag();
                   int post = Integer.parseInt(post2);
                   delegate.appointmentRadioButtonTapped(post);
               }

           }
       });

        String status =appointmentConfirmationList.get(position).status;

        if (status.equalsIgnoreCase("Cancelled")) {
            holder.cancelLayout.setBackgroundColor(Color.parseColor("#F05050"));
            holder.cancelLayout.setVisibility(View.VISIBLE);
            holder.radioButton.setEnabled(false);
            holder.radioButton.setChecked(true);
        } else {
            holder.cancelLayout.setVisibility(View.GONE);
            holder.radioButton.setEnabled(true);
            holder.radioButton.setChecked(appointmentConfirmationList.get(position).isSelected);
        }



        return convertView;
    }

    static class Viewholder {

        RadioButton radioButton;
        Button frontButton;
        LinearLayout cancelLayout;


    }


}