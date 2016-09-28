package com.kalendria.kalendria.singleton;

import android.content.Context;
import android.widget.Toast;

import com.kalendria.kalendria.model.AddToCardServiceModel;
import com.kalendria.kalendria.model.AddToCardVenueModel;
import com.kalendria.kalendria.model.DeshBoardViewPageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murugan on 07/03/16.
 */
public class AddToCardSingleTone {

    private static AddToCardSingleTone addToCardSingleTone;

    public List<AddToCardVenueModel> addToCardArrayList;

    private AddToCardSingleTone() {
        addToCardArrayList = new ArrayList<AddToCardVenueModel>();

    }
    public static AddToCardSingleTone getInstance() {
        if (addToCardSingleTone == null) {
            addToCardSingleTone = new AddToCardSingleTone();
        }

        return addToCardSingleTone;
    }
    public List<AddToCardVenueModel> getParamList() {
        return addToCardArrayList;
    }

    public void setParamList(List<AddToCardVenueModel> paramList) {
        this.addToCardArrayList = paramList;
    }

    public boolean addToCart(int positionBtn,ArrayList<DeshBoardViewPageModel> imagesList,Context context,boolean showToster){

       // addToCardSingletone = AddToCardSingleTone.getInstance().getParamList();
        boolean flag = false;
        boolean serviceDublicateID = false;
        int location = 0;
        boolean isExist=true;

        AddToCardVenueModel addToCardVenueModel = new AddToCardVenueModel();
        ArrayList<AddToCardServiceModel> service = new ArrayList<AddToCardServiceModel>();
        ArrayList<AddToCardServiceModel> service_checkDublicate = new ArrayList<AddToCardServiceModel>();

        for (int i = 0; i < addToCardArrayList.size(); i++) {

            if (imagesList.get(positionBtn).getVenueID().equals(addToCardArrayList.get(i).getVenueID())) {
                flag = true;
                location = i;
                service_checkDublicate = addToCardArrayList.get(location).getItems();
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

        if (!flag) {
            addToCardVenueModel.setVenueID(imagesList.get(positionBtn).getVenueID());
            addToCardVenueModel.setVenueName(imagesList.get(positionBtn).getVenueName());
            addToCardVenueModel.setVenuImage(imagesList.get(positionBtn).getVenuImage());
            addToCardVenueModel.setVenuImagethumb(imagesList.get(positionBtn).getVenuImageThamp());
            addToCardVenueModel.setCity(imagesList.get(positionBtn).getCity());
            addToCardVenueModel.setRegion(imagesList.get(positionBtn).getRegion());

            service.add(addToCardServiceModel);
            addToCardVenueModel.setItems(service);
            addToCardArrayList.add(addToCardVenueModel);
            if(showToster)
            Toast.makeText(context, "Service added successfully in cart!", Toast.LENGTH_SHORT).show();

            isExist=true;


        } else {

            addToCardVenueModel = addToCardArrayList.get(location);
            service = addToCardVenueModel.getItems();
            if (!serviceDublicateID) {
                service.add(addToCardServiceModel);
                addToCardVenueModel.setItems(service);
                addToCardArrayList.set(location, addToCardVenueModel);
            } else {
                if(showToster)
                Toast.makeText(context, "This service have been already added in the cart!", Toast.LENGTH_SHORT).show();
                                /*vasanth you add for popup remove*/
                isExist=false;

            }

        }
        return isExist;

    }

}
