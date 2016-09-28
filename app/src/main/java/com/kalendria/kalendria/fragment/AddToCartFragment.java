package com.kalendria.kalendria.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.kalendria.kalendria.R;
import com.kalendria.kalendria.activity.Category;
import com.kalendria.kalendria.activity.DashBoard;
import com.kalendria.kalendria.activity.GiftVoucherActivity;
import com.kalendria.kalendria.activity.MyOrderActivity;
import com.kalendria.kalendria.activity.SubCategory;
import com.kalendria.kalendria.activity.Venue;
import com.kalendria.kalendria.adapter.AddToCardAdapter;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;

import java.util.ArrayList;
import java.util.List;


public class AddToCartFragment extends Fragment {

    View view;
    List<AddToCardVenueModel> addToCardSingletone;
    ListView listView;
    TextView statusLabel;
    ArrayList<Object> people = new ArrayList<>();
    AddToCardAdapter addToCardAdapter;

    public AddToCartFragment() {
        // Required empty public constructor
        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();/*This line is used add to card venue name and servie list */

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);
        listView = (ListView) view.findViewById(R.id.subcatagory_list);
        statusLabel =(TextView)view.findViewById(R.id.status_label);

        addToCardAdapter = new AddToCardAdapter(getActivity(), people);
        listView.setAdapter(addToCardAdapter);



        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Jsonparsing();

        try {


            String sizeofcard = " " + addToCardSingletone.size();
            Context context = getActivity();
            if (context instanceof DashBoard) {
                ((DashBoard) context).dispatchInformations(sizeofcard);
            } else if (context instanceof Venue) {
                ((Venue) context).dispatchInformations(sizeofcard);
            }
            else if (context instanceof MyOrderActivity) {
                ((MyOrderActivity) context).dispatchInformations(sizeofcard);
            }
            else if (context instanceof Category) {
                ((Category) context).dispatchInformations(sizeofcard);
            }
            else if (context instanceof SubCategory) {
                ((SubCategory) context).dispatchInformations(sizeofcard);
            }
            else if (context instanceof GiftVoucherActivity) {
                ((GiftVoucherActivity) context).dispatchInformations(sizeofcard);
            }
        }catch (Exception ex){ex.printStackTrace();}
    }

    private void Jsonparsing() {


        people.clear();

        ArrayList<AddToCardServiceModel> items;
        if (addToCardSingletone.size() > 0) {

            for (int i = 0; i < addToCardSingletone.size(); i++) {

                AddToCardVenueModel addToCardVenueModel = new AddToCardVenueModel();
                addToCardVenueModel.setVenueID(addToCardSingletone.get(i).getVenueID());
                addToCardVenueModel.setVenueName(addToCardSingletone.get(i).getVenueName());
                addToCardVenueModel.setVenuImage(addToCardSingletone.get(i).getVenuImage());
                addToCardVenueModel.setCity(addToCardSingletone.get(i).getCity());
                addToCardVenueModel.setRegion(addToCardSingletone.get(i).getRegion());

                people.add(addToCardVenueModel);

                items = (addToCardSingletone.get(i).getItems());

                for (int j = 0; j < items.size(); j++) {
                            /*get the child details */
                    AddToCardServiceModel addToCardServiceModel = new AddToCardServiceModel();
                    addToCardServiceModel.setServiceId(items.get(j).getServiceId());
                    addToCardServiceModel.setServiceName(items.get(j).getServiceName());
                    addToCardServiceModel.setServicePrice(items.get(j).getServicePrice());
                    addToCardServiceModel.setServiceDiscount(items.get(j).getServiceDiscount());
                    addToCardServiceModel.setServiceDuration(items.get(j).getServiceDuration());
                    // get the parant detials
                    addToCardServiceModel.setVenueID(addToCardSingletone.get(i).getVenueID());
                    addToCardServiceModel.setVenueName(addToCardSingletone.get(i).getVenueName());
                    addToCardServiceModel.setVenuImage(addToCardSingletone.get(i).getVenuImage());
                    addToCardServiceModel.setCity(addToCardSingletone.get(i).getCity());
                    addToCardServiceModel.setRegion(items.get(j).getServiceDuration());

                    people.add(addToCardServiceModel);


                }
            }

        }

        addToCardAdapter.notifyDataSetChanged();
        if(people.size()>0) {

            if(statusLabel!=null)
                statusLabel.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        else
        {
            if(statusLabel!=null)
                statusLabel.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }


    }


}



