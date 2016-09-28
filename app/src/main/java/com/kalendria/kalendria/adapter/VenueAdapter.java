package com.kalendria.kalendria.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.CheckOut;
import com.kalendria.kalendria.activity.KalendriaActivity;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.fragment.VenueFragment;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.ServiceModal;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mansoor on 11/03/16.
 */
public class VenueAdapter extends BaseAdapter {

    public List<ServiceModal> _data;
    private ArrayList<ServiceModal> arraylist;
    Context mContext;
    ViewHolder v;

    VenueFragment fragement;

    /*add to card start */
    private AlertDialog myDialog;
    private View alertView;
    TextView home_venu_name_txt_add,home_service_name_txt,home_service_price_txt,home_service_duration_txt,addtocart_txt;
    Button cross_image;
    Button home_service_book_btn;
    List<AddToCardVenueModel> addToCardSingletone;
    int positionBtn=0;
	/*add to card start end */

    public String sector;

    public VenueAdapter(List<ServiceModal> selectUsers, Context context,VenueFragment fragement) {
        _data = selectUsers;
        mContext = context;
        this.arraylist = new ArrayList<ServiceModal>();
        this.arraylist.addAll(_data);
        initAlertBox();
        this.fragement = fragement;

        System.out.println("i am from selected adapter page"+_data.size() );
        //Toast.makeText(_c, "hi", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.categoryslect_child, null);
            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.imageView = (ImageView) view.findViewById(R.id.imavenu);
        v.ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);

        v.venu_name_txt = (TextView) view.findViewById(R.id.venu_name_txt);
        v.service_title_txt = (TextView) view.findViewById(R.id.service_title_txt);
        v.venulocation_tet = (TextView) view.findViewById(R.id.venulocation_tet);
        v.normal_price_text = (TextView) view.findViewById(R.id.normal_price_text);
        v.discount_price_txt = (TextView) view.findViewById(R.id.discount_price_txt);


        final ServiceModal data = (ServiceModal) _data.get(i);
        v.venu_name_txt.setText(data.getBuisnessName());
        v.service_title_txt.setText(data.getServiceName());
        v.venulocation_tet.setText(data.getRegion()+", "+data.getCity());
        v.normal_price_text.setText(data.getPrice()+" "+"AED");
        v.normal_price_text.setPaintFlags(v.normal_price_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        v.discount_price_txt.setText(data.getDiscounted_price() + " " + "AED");

        //-- Coded by Magesh for Price and discount fix
        if(data.getDiscount()!=null && !data.getDiscount().equalsIgnoreCase("0"))
        {
            v.normal_price_text.setVisibility(View.VISIBLE);
        }
        else
            v.normal_price_text.setVisibility(View.INVISIBLE);

        if(data.getPrice()!=null && !data.getPrice().equalsIgnoreCase("0"))
        {
            v.discount_price_txt.setEnabled(true);
        }
        else
            v.discount_price_txt.setEnabled(false);
//end code ---



        v.discount_price_txt.setTag(i);
        try {
            System.out.println("getoverallRatting-->"+data.getOverallRating());

            if (data.getOverallRating()!="null") {

                float numstars = Float.parseFloat(data.getOverallRating());
                v.ratingbar.setRating(numstars);

                LayerDrawable stars = (LayerDrawable) v.ratingbar.getProgressDrawable();

                if(v.ratingbar.getRating() > 0)
                {
                    stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                    stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Boolean isImageFound = false;
        if( data.getImageUrl() != null && !"".equals(data.getImageUrl()) ){
            try {
                Picasso.with(mContext)
                        .load(data.getImageUrl())
                                // .memoryPolicy(MemoryPolicy.NO_CACHE )
                                // .networkPolicy(NetworkPolicy.NO_CACHE)
                                //.resize(720, 350)
                                // .error(R.drawable.login)
                        .placeholder(R.drawable.login_logo)
                        .noFade()
                                // .fit().centerCrop()
                        .into(v.imageView);
                isImageFound=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(isImageFound==false)
        {
            v.imageView.setImageResource(R.drawable.bg);
        }


        v.discount_price_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                positionBtn = (int) v.getTag();
                ServiceModal child = (ServiceModal) _data.get(positionBtn);
                if (child.getIntPrice() > 0) {
                    home_venu_name_txt_add.setText(_data.get(positionBtn).getBuisnessName());
                    home_service_name_txt.setText(_data.get(positionBtn).getServiceName());
                    home_service_price_txt.setText(_data.get(positionBtn).getDiscounted_price() + " " + "AED");
                    home_service_duration_txt.setText(_data.get(positionBtn).getDuration());
                    myDialog.show();
                }
            }
        });



        view.setTag(data);
        return view;
    }

    private void initAlertBox()
    {
        // alertdialog start
        /*set the tag for book now button */
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflateralert = LayoutInflater.from(mContext);
        alertView = inflateralert.inflate(R.layout.booknow, null);
        builder.setView(alertView);


        home_venu_name_txt_add = (TextView) alertView.findViewById(R.id.home_venu_name_txt_add);
        home_service_name_txt = (TextView) alertView.findViewById(R.id.home_service_name_txt);
        home_service_price_txt = (TextView) alertView.findViewById(R.id.home_service_price_txt);
        home_service_duration_txt = (TextView) alertView.findViewById(R.id.home_service_duration_txt);
        addtocart_txt = (TextView) alertView.findViewById(R.id.addtocart_txt);
        cross_image = (Button) alertView.findViewById(R.id.cross_image_addto_card);
        home_service_book_btn = (Button) alertView.findViewById(R.id.home_service_book_btn);

        builder.setCancelable(true);
        myDialog = builder.create();


        cross_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        home_service_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(_c, "Service added successfully in cart", Toast.LENGTH_SHORT).show();
                addtoCar(false);
                SharedPreferences sharedPref;
                sharedPref = mContext.getSharedPreferences(Constant.MyPREFERENCES, 0);
                Intent intent = new Intent(mContext, CheckOut.class);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("venueID", _data.get(positionBtn).getBuisnessId());
                editor.commit();
                mContext.startActivity(intent);

            }
        });addtocart_txt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

               addtoCar(true);

        }
    });


    }

    public void addtoCar(boolean showToast)
    {
        //Toast.makeText(context, "card addted successfully ", Toast.LENGTH_SHORT).show();
        System.out.println("potionID card-->"+""+positionBtn);

        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();
        boolean flag=false;
        boolean serviceDublicateID=false;
        int location=0;

        AddToCardVenueModel addToCardVenueModel=new AddToCardVenueModel();
        ArrayList<AddToCardServiceModel> items=new ArrayList<AddToCardServiceModel>();
        ArrayList<AddToCardServiceModel> service_checkDublicate=new ArrayList<AddToCardServiceModel>();

        for(int i=0;i<addToCardSingletone.size();i++){

            AddToCardVenueModel model = addToCardSingletone.get(i);
            if(_data.get(positionBtn).getBuisnessId().equals(addToCardSingletone.get(i).getVenueID())){
                if(model.isFromLoyality)
                {
                    addToCardSingletone.remove(i);
                    break;

                }
                else
                {
                    flag=true;
                    location=i;
                }

                service_checkDublicate=addToCardSingletone.get(location).getItems();
                for(int j=0;j<service_checkDublicate.size();j++){
                    if(_data.get(positionBtn).getServiceId().equals(service_checkDublicate.get(j).getServiceId())){
                        serviceDublicateID=true;
                    }
                }
            }
        }

        AddToCardServiceModel addToCardServiceModel=new AddToCardServiceModel();
        addToCardServiceModel.setServiceId(_data.get(positionBtn).getServiceId());
        addToCardServiceModel.setServiceId2(_data.get(positionBtn).getServiceId());
        addToCardServiceModel.setServiceName(_data.get(positionBtn).getServiceName());
        addToCardServiceModel.setServicePrice(_data.get(positionBtn).getPrice());
        addToCardServiceModel.setServiceDiscount(_data.get(positionBtn).getDiscount());
        addToCardServiceModel.setServiceDuration(_data.get(positionBtn).getDuration());
        addToCardServiceModel.setSector(this.sector);
        addToCardServiceModel.calculateDiscountAmount();


        Boolean isAdded =true;
        if(!flag){
            addToCardVenueModel.setVenueID(_data.get(positionBtn).getBuisnessId());
            addToCardVenueModel.setVenueName(_data.get(positionBtn).getBuisnessName());
            addToCardVenueModel.setVenuImage(_data.get(positionBtn).getImageUrl());
            addToCardVenueModel.setVenuImagethumb(_data.get(positionBtn).getImageUrlThumb());
            addToCardVenueModel.setCity(_data.get(positionBtn).getCity());
            addToCardVenueModel.setUserSector(this.sector);
            addToCardVenueModel.setHow_often(fragement.getOften());
            addToCardVenueModel.setRegion(_data.get(positionBtn).getRegion());


            items.add(addToCardServiceModel);
            addToCardVenueModel.setItems(items);
            addToCardSingletone.add(addToCardVenueModel);

        }else{
            addToCardVenueModel=addToCardSingletone.get(location);
            items=addToCardVenueModel.getItems();
            if(!serviceDublicateID){
                items.add(addToCardServiceModel);
                addToCardVenueModel.setItems(items);
                addToCardSingletone.set(location,addToCardVenueModel);
                if(showToast)
                    Toast.makeText(mContext, "This service have been already added in the cart!", Toast.LENGTH_SHORT).show();
            }else{
                isAdded=false;
                if(showToast)
                Toast.makeText(mContext, "This service have been already added in the cart!", Toast.LENGTH_SHORT).show();
            }

        }

        if(isAdded && showToast)
            Toast.makeText(mContext, "Service added successfully in cart!", Toast.LENGTH_SHORT).show();


        String sizeofcard = " " + addToCardSingletone.size();
        ((KalendriaActivity) mContext).dispatchInformations(sizeofcard);
        myDialog.dismiss();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (ServiceModal wp : arraylist) {
                if (wp.getOverallRating().toLowerCase(Locale.getDefault()).contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    static class ViewHolder {

        RatingBar ratingbar;
        ImageView imageView;
        TextView venu_name_txt,service_title_txt,venulocation_tet,normal_price_text,discount_price_txt;

    }
}
