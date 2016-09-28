package com.kalendria.kalendria.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.CheckOut;
import com.kalendria.kalendria.activity.KalendriaActivity;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.DeshBoardViewPageModel;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.KalendriaAppController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DeshBoardViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    ArrayList<DeshBoardViewPageModel> imagesList;
    LayoutInflater inflater;



    /*add to card start */
    private AlertDialog myDialog;
    private View alertView;
    TextView home_venu_name_txt_add, home_service_name_txt, home_service_price_txt, home_service_duration_txt, addtocart_txt;
    Button cross_image;
    Button home_service_book_btn;
    List<AddToCardVenueModel> addToCardSingletone;
    int positionBtn = 0;

    LinearLayout book;
    /*add to card start end */


    public DeshBoardViewPagerAdapter(Context context, ArrayList<DeshBoardViewPageModel> flag) {
        this.context = context;
        this.imagesList = flag;

        //coded by Magesh
        initDialog();
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
    public Object instantiateItem(ViewGroup container, final int position) {

        // Declare Variables

       // System.out.println("potionIDTotal-->" + "" + position);

        ImageView imgflag;
        final TextView service_name_txt, home_venu_name_txt, home_address, home_discount_price_txt, home__price_txt, home_durition;
        Button book_now_btn;
        RatingBar home_ratingbar;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.home_viewpager, container, false);


        // Locate the ImageView in viewpager_item.xml
        imgflag = (ImageView) itemView.findViewById(R.id.flag);
        service_name_txt = (TextView) itemView.findViewById(R.id.service_name_txt);
        home_venu_name_txt = (TextView) itemView.findViewById(R.id.home_venu_name_txt);
        home_address = (TextView) itemView.findViewById(R.id.home_address);
        home_discount_price_txt = (TextView) itemView.findViewById(R.id.home_discount_price_txt);
        home__price_txt = (TextView) itemView.findViewById(R.id.home__price_txt);
        home_durition = (TextView) itemView.findViewById(R.id.home_durition);
        book_now_btn = (Button) itemView.findViewById(R.id.book_now_btn);
        home_ratingbar = (RatingBar) itemView.findViewById(R.id.home_ratingbar);

        // Capture position and set to the ImageView
        //imgflag.setImageResource(flag[position]);

        if (imagesList.get(position).getVenuImage() != null) {
            Picasso.with(context).load(imagesList.get(position).getVenuImage()).into(imgflag);
        }


       //service_name_txt.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        service_name_txt.setText(imagesList.get(position).getServiceName());
        home_venu_name_txt.setText(imagesList.get(position).getVenueName());


        if (imagesList.get(position).getCity() != null) {
            home_address.setText(imagesList.get(position).getRegion() + ", " + imagesList.get(position).getCity());
        } else {
            home_address.setText(imagesList.get(position).getRegion());
        }

        String doscount = imagesList.get(position).getServiceDiscount();
        if (!doscount.equalsIgnoreCase("0")) {
            home_discount_price_txt.setText("Save " + imagesList.get(position).getServiceDiscount() + "%");
        }

        int priceAmt = 0;
        int dictAmt = 0;

        try {
            priceAmt=Integer.parseInt(imagesList.get(position).getServicePrice());
        }
       catch (Exception ex){ex.printStackTrace();}

        try {
            dictAmt=Integer.parseInt(imagesList.get(position).getServiceDiscount());
        }
        catch (Exception ex){ex.printStackTrace();}




        double calAmt1 = ((double)(priceAmt) / 100);
        Double calAmt = (calAmt1 * (double)dictAmt);

        int discountAmount = calAmt.intValue();
        int remainAmount = 0;

        remainAmount = priceAmt - discountAmount;

        String remainAmtString = remainAmount + " " + "AED";

        home__price_txt.setText(remainAmtString);
        String dur =imagesList.get(position).getServiceDuration();
        try {


            String[] durations = dur.split(":");
            if (durations.length > 1) {

                int hour = 0, min = 0;

                try {
                    hour = Integer.parseInt(durations[0]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    min = Integer.parseInt(durations[1]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                String strDur = "";
                if (hour > 0) {
                    if (hour > 1)
                        strDur += durations[0] + " hrs";
                    else
                        strDur += durations[0] + " hr";
                }
                if (min > 0) {
                    if(hour>0)
                    strDur += " " + durations[1] + " min";
                    else
                        strDur +=  durations[1] + " min";
                }

                home_durition.setText(strDur);
            } else
                home_durition.setText(dur);
        }catch (Exception ex){
            home_durition.setText(dur);
            ex.printStackTrace();
        }

        float numstars = Float.parseFloat(imagesList.get(position).getVenueOverallRatting());
        home_ratingbar.setRating(numstars);

        LayerDrawable stars = (LayerDrawable) home_ratingbar.getProgressDrawable();

        if (home_ratingbar.getRating() > 0) {
            //stars.getDrawable(4).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            //stars.getDrawable(3).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        }

        book_now_btn.setTag(position); // set the position id to button to get the orginal id from todal id

/*
Code block has moved to seperate Methode because you are re-allocating myDialog many times,
which is totally wrong
 */

        book_now_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                positionBtn = (int) v.getTag();

                home_venu_name_txt_add.setText(imagesList.get(positionBtn).getVenueName());
                home_service_name_txt.setText(imagesList.get(positionBtn).getServiceName());
                home_service_price_txt.setText(imagesList.get(positionBtn).getServicePrice() + " " + "AED");
                home_service_duration_txt.setText(imagesList.get(positionBtn).getServiceDuration());
                myDialog.show();


            }
        });


        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    ////coded by Magesh
    public void initDialog() {

	/*set the tag for book now button */
        if(context==null)
            context= KalendriaAppController.getInstance().getApplicationContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflateralert = LayoutInflater.from(context);
        alertView = inflateralert.inflate(R.layout.booknow, null);
        builder.setView(alertView);

        home_venu_name_txt_add = (TextView) alertView.findViewById(R.id.home_venu_name_txt_add);
        home_service_name_txt = (TextView) alertView.findViewById(R.id.home_service_name_txt);
        home_service_price_txt = (TextView) alertView.findViewById(R.id.home_service_price_txt);
        home_service_duration_txt = (TextView) alertView.findViewById(R.id.home_service_duration_txt);
        addtocart_txt = (TextView) alertView.findViewById(R.id.addtocart_txt);
        cross_image = (Button) alertView.findViewById(R.id.cross_image_addto_card);
        home_service_book_btn = (Button) alertView.findViewById(R.id.home_service_book_btn);
        book = (LinearLayout) alertView.findViewById(R.id.book);

        myDialog = builder.create();
        builder.setCancelable(true);


        cross_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

            }
        });

        home_venu_name_txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();

            }
        });

        home_service_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "Working is going on  ", Toast.LENGTH_SHORT).show();
                addtoCar(false);
                SharedPreferences sharedPref;
                sharedPref = context.getSharedPreferences(Constant.MyPREFERENCES, 0);
                Intent intent = new Intent(context, CheckOut.class);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("venueID", imagesList.get(positionBtn).getVenueID());
                editor.commit();
                context.startActivity(intent);
                //Toast.makeText(context, "Service added successfully in cart", Toast.LENGTH_SHORT).show();

            }
        });

        addtocart_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "card addted successfully ", Toast.LENGTH_SHORT).show();
                addtoCar(true);

            }
        });

    }

    public void addtoCar(boolean showToast)
    {
        System.out.println("potionID card-->" + "" + positionBtn);
        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();
        boolean flag = false;
        boolean serviceDublicateID = false;
        int location = 0;

        AddToCardVenueModel addToCardVenueModel = new AddToCardVenueModel();
        ArrayList<AddToCardServiceModel> service = new ArrayList<AddToCardServiceModel>();
        ArrayList<AddToCardServiceModel> service_checkDublicate = new ArrayList<AddToCardServiceModel>();

        for (int i = 0; i < addToCardSingletone.size(); i++) {

            AddToCardVenueModel model = addToCardSingletone.get(i);
            if (imagesList.get(positionBtn).getVenueID().equals(addToCardSingletone.get(i).getVenueID())) {
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

                service_checkDublicate = addToCardSingletone.get(location).getItems();
                for (int j = 0; j < service_checkDublicate.size(); j++) {
                    if (imagesList.get(positionBtn).getServiceId().equals(service_checkDublicate.get(j).getServiceId())) {
                        serviceDublicateID = true;
                    }
                }

            }
        }

        AddToCardServiceModel addToCardServiceModel = new AddToCardServiceModel();
        addToCardServiceModel.setServiceId(imagesList.get(positionBtn).getServiceId());
        addToCardServiceModel.setServiceName(imagesList.get(positionBtn).getServiceName());
        addToCardServiceModel.setServicePrice(imagesList.get(positionBtn).getServicePrice());
        addToCardServiceModel.setServiceDiscount(imagesList.get(positionBtn).getServiceDiscount());
        addToCardServiceModel.setServiceDuration(imagesList.get(positionBtn).getServiceDuration());
        addToCardServiceModel.setServiceId2(imagesList.get(positionBtn).getServiceId());
        addToCardServiceModel.setSector(imagesList.get(positionBtn).sector);
        addToCardServiceModel.calculateDiscountAmount();

        if (!flag) {
            addToCardVenueModel.setVenueID(imagesList.get(positionBtn).getVenueID());
            addToCardVenueModel.setVenueName(imagesList.get(positionBtn).getVenueName());
            addToCardVenueModel.setVenuImage(imagesList.get(positionBtn).getVenuImage());
            addToCardVenueModel.setVenuImagethumb(imagesList.get(positionBtn).getVenuImageThamp());
            addToCardVenueModel.setCity(imagesList.get(positionBtn).getCity());
            addToCardVenueModel.setRegion(imagesList.get(positionBtn).getRegion());

            service.add(addToCardServiceModel);
            addToCardVenueModel.setItems(service);
            addToCardSingletone.add(addToCardVenueModel);
            if(showToast)
            Toast.makeText(context, "Service added successfully in cart!", Toast.LENGTH_SHORT).show();


            myDialog.dismiss();

        } else {

            addToCardVenueModel = addToCardSingletone.get(location);
            service = addToCardVenueModel.getItems();
            if (!serviceDublicateID) {
                service.add(addToCardServiceModel);
                addToCardVenueModel.setItems(service);
                addToCardSingletone.set(location, addToCardVenueModel);
                if(showToast)
                    Toast.makeText(context, "Service added successfully in cart!", Toast.LENGTH_SHORT).show();
            } else {
                if(showToast)
                Toast.makeText(context, "This service have been already added in the cart!", Toast.LENGTH_SHORT).show();
                                /*vasanth you add for popup remove*/

                myDialog.dismiss();
            }

        }
        System.out.println("v--->" + "" + addToCardSingletone.size());

        try {

            String sizeofcard = " " + addToCardSingletone.size();
            ((KalendriaActivity) context).dispatchInformations(sizeofcard);

        }catch (Exception ex){ex.printStackTrace();}


        myDialog.dismiss();
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }


}
