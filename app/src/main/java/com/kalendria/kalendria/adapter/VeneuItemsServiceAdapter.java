package com.kalendria.kalendria.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.CheckOut;
import com.kalendria.kalendria.activity.KalendriaActivity;
import com.kalendria.kalendria.api.Constant;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.VeneuItemServiceHeader;
import com.kalendria.kalendria.model.VenueItemServiceChild;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;

import java.util.ArrayList;
import java.util.List;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;


public class VeneuItemsServiceAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<VeneuItemServiceHeader> groups;
    public String sector;
    /*add to card start */
    private AlertDialog myDialog;
    private View alertView;
    TextView home_venu_name_txt_add, home_service_name_txt, home_service_price_txt, home_service_duration_txt, addtocart_txt;
    Button cross_image;
    Button home_service_book_btn;
    List<AddToCardVenueModel> addToCardSingletone;
    int positionBtn = 0;
    /*add to card start end */


    public VeneuItemsServiceAdapter(Context context, ArrayList<VeneuItemServiceHeader> expListItems) {
        this.context = context;
        this.groups = expListItems;
        initGUI();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<VenueItemServiceChild> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @SuppressWarnings("static-access")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        final TextView service_child, service_duration, service_discount, service_price;
        final Button info_button;


        final VenueItemServiceChild child = (VenueItemServiceChild) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.venue_items_service_child, null);
        }

        service_child = (TextView) convertView.findViewById(R.id.service_child);
        service_duration = (TextView) convertView.findViewById(R.id.service_duration);
        service_discount = (TextView) convertView.findViewById(R.id.service_discount);
        service_price = (TextView) convertView.findViewById(R.id.service_price);

        info_button = (Button) convertView.findViewById(R.id.info_button);

        service_child.setText(child.getName());
        service_duration.setText(child.getDuration());


        service_discount.setText(child.getStrikeOutAmount());

        service_discount.setPaintFlags(service_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        service_price.setText(child.getOriginalPrices());

        /* code modified by magesh
        if (!child.getPrice().equalsIgnoreCase("null") && !child.getPrice().equalsIgnoreCase("0")) {
            service_discount.setText(child.getPrice() + "AED");
            service_discount.setPaintFlags(service_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (!child.getDiscount().equalsIgnoreCase("null") && !child.getDiscount().equalsIgnoreCase("0")) {

            service_price.setText(child.getDiscount() + "AED");
        }*/


        if (child.getDiscription() != null && child.getDiscription().length() > 0) {
            info_button.setVisibility(View.VISIBLE);
        } else
            info_button.setVisibility(View.INVISIBLE);

        RelativeLayout infoLayout = (RelativeLayout) convertView.findViewById(R.id.infoLayout);
        infoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopup(info_button, child);
            }
        });

        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopup(info_button, child);

            }
        });

        if (child.getDiscription() != null && child.getDiscription().length() > 0) {
            service_child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPopup(info_button, child);

                }
            });
        } else {
            service_child.setOnClickListener(null);
        }

        service_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (child.getIntPrice() > 0) {
                    home_venu_name_txt_add.setText(child.getVenuename());
                    home_service_name_txt.setText(child.getName());
                    home_service_price_txt.setText(child.getOriginalPrices());
                    home_service_duration_txt.setText(child.getDuration());
                    myDialog.show();


                    cross_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.cancel();
                        }
                    });

                    home_service_book_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addtoCar(true, child);
                            SharedPreferences sharedPref;
                            sharedPref = context.getSharedPreferences(Constant.MyPREFERENCES, 0);
                            Intent intent = new Intent(context, CheckOut.class);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("venueID", child.getVenueid());
                            editor.commit();
                            context.startActivity(intent);

                        }
                    });

                    addtocart_txt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addtoCar(true, child);
                        }
                    });
                }


            }
        });

        return convertView;

    }

    public void showPopup(Button info_button, VenueItemServiceChild child) {
        new SimpleTooltip.Builder(context)
                .anchorView(info_button)
                .text(Html.fromHtml(child.getDiscription()))
                .gravity(Gravity.TOP)
                .animated(false)
                .transparentOverlay(true)
                .build()
                .show();


/*
                Activity parentActivity = (Activity)context;
                View view = parentActivity.getLayoutInflater().inflate(R.layout.layout_tip_content_horizontal, null);
                TextView contentTextView =(TextView)view.findViewById(R.id.contentTextView);
                contentTextView.setText(Html.fromHtml(child.getDiscription()));
                new EasyDialog(context)
                        // .setLayoutResourceId(R.layout.layout_tip_content_horizontal)//layout resource id
                        .setLayout(view)
                        .setBackgroundColor(Color.WHITE)
                                // .setLocation(new location[])//point in screen
                        .setLocationByAttachedView(info_button)
                        .setGravity(EasyDialog.GRAVITY_TOP)
                        .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 1000, -600, 100, -50, 50, 0)
                        .setAnimationAlphaShow(1000, 0.3f, 1.0f)
                        .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 500, -50, 800)
                        .setAnimationAlphaDismiss(500, 1.0f, 0.0f)
                        .setTouchOutsideDismiss(true)
                        .setMatchParent(false)
                        .setMarginLeftAndRight(24, 24)
                        .setOutsideColor(parentActivity.getResources().getColor(R.color.outside_color_trans))
                        .show();

                /*
                String[] message = new String[1];
                ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, message);
                message[0]=child.getDiscription();
                new android.app.AlertDialog.Builder(context)
                        .setTitle("")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        }).create().show();

/*
                Tooltip.make(context,
                        new Tooltip.Builder(101)
                                .anchor(info_button, Tooltip.Gravity.BOTTOM)
                                .closePolicy(new Tooltip.ClosePolicy()
                                        .insidePolicy(true, false)
                                        .outsidePolicy(true, false), 4000)
                                .activateDelay(900)
                                .showDelay(400)
                                .text(child.getDiscription())
                                .maxWidth(600)
                                .withArrow(true)
                                .withOverlay(true).build()
                ).show();
                */
    }

    public void addtoCar(boolean showToast, VenueItemServiceChild child) {
        System.out.println("potionID card-->" + "" + positionBtn);

        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();
        boolean flag = false;
        boolean serviceDublicateID = false;
        int location = 0;


        AddToCardVenueModel addToCardVenueModel = new AddToCardVenueModel();
        ArrayList<AddToCardServiceModel> items = new ArrayList<AddToCardServiceModel>();
        ArrayList<AddToCardServiceModel> service_checkDublicate = new ArrayList<AddToCardServiceModel>();

        for (int i = 0; i < addToCardSingletone.size(); i++) {
            AddToCardVenueModel model = addToCardSingletone.get(i);
            if (child.getVenueid().equals(addToCardSingletone.get(i).getVenueID())) {
                if (model.isFromLoyality) {
                    addToCardSingletone.remove(i);
                    break;

                } else {
                    flag = true;
                    location = i;
                }

                service_checkDublicate = addToCardSingletone.get(location).getItems();
                for (int j = 0; j < service_checkDublicate.size(); j++) {
                    if (child.getserviceID().equals(service_checkDublicate.get(j).getServiceId())) {
                        serviceDublicateID = true;
                    }
                }
            }
        }

        AddToCardServiceModel addToCardServiceModel = new AddToCardServiceModel();
        addToCardServiceModel.setServiceId(child.getserviceID());
        addToCardServiceModel.setServiceName(child.getName());
        addToCardServiceModel.setServicePrice(child.getPrice());
        addToCardServiceModel.setServiceDiscount(child.getDiscount());
        addToCardServiceModel.setServiceDuration(child.getDuration());
        addToCardServiceModel.calculateDiscountAmount();
        addToCardServiceModel.setSector(child.sector);
        addToCardServiceModel.setServiceId2(child.getserviceID());

        if (!flag) {
            addToCardVenueModel.setVenueID(child.getVenueid());
            addToCardVenueModel.setVenueName(child.getVenuename());
            addToCardVenueModel.setVenuImage(child.getVenueImage());
            addToCardVenueModel.setVenuImagethumb(child.getVenueImage());
            addToCardVenueModel.setCity(child.getVenuecity());
            addToCardVenueModel.setRegion(child.getVeneregiion());

            if (showToast)
                Toast.makeText(context, "Service successfully added in the cart!", Toast.LENGTH_SHORT).show();
            items.add(addToCardServiceModel);
            addToCardVenueModel.setItems(items);
            addToCardSingletone.add(addToCardVenueModel);
        } else {
            addToCardVenueModel = addToCardSingletone.get(location);
            items = addToCardVenueModel.getItems();
            if (!serviceDublicateID) {
                items.add(addToCardServiceModel);
                addToCardVenueModel.setItems(items);
                addToCardSingletone.set(location, addToCardVenueModel);
                if (showToast)
                    Toast.makeText(context, "Service successfully added in the cart!", Toast.LENGTH_SHORT).show();

            } else {
                if (showToast)
                    Toast.makeText(context, "This service have been already added in the cart!", Toast.LENGTH_SHORT).show();
                // myDialog.dismiss();
            }

        }

        myDialog.dismiss();
        String sizeofcard = " " + addToCardSingletone.size();
        ((KalendriaActivity) context).dispatchInformations(sizeofcard);

    }

    public void initGUI() {
        // alertdialog start
        /*set the tag for book now button */
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

        builder.setCancelable(true);
        myDialog = builder.create();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<VenueItemServiceChild> chList = groups.get(groupPosition).getItems();

        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }


    @SuppressWarnings("static-access")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        VeneuItemServiceHeader group = (VeneuItemServiceHeader) getGroup(groupPosition);


        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.venue_items_service_header, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.venu_service_header_txt);
        textView.setText(group.getName());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

