package com.kalendria.kalendria.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.kalendria.kalendria.adapter.GridDetailViewAdapter;
import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.singleton.AddToCardSingleTone;
import com.kalendria.kalendria.utility.KalendriaAppController;

import java.util.ArrayList;
import java.util.List;


public class GiftDetailViewFragment extends Fragment {

    View view;
    List<AddToCardVenueModel> addToCardSingletone;
    ListView listView;
    TextView statusLabel;
    ArrayList<Object> people = new ArrayList<>();
    GridDetailViewAdapter giftDetailAdapter;

    public GiftDetailViewFragment() {
        // Required empty public constructor
        addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();/*This line is used add to card venue name and servie list */

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.gift_detailview_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.listview);
        statusLabel =(TextView)view.findViewById(R.id.status_label);

        giftDetailAdapter = new GridDetailViewAdapter(getActivity(), KalendriaAppController.getInstance().selectedGiftVoucher);
        listView.setAdapter(giftDetailAdapter);

        Button backButton = (Button)view.findViewById(R.id.gift_back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
            }
        });


giftDetailAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();


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



}



