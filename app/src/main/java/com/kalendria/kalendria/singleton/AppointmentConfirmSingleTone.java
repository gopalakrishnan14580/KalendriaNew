package com.kalendria.kalendria.singleton;

import com.kalendria.kalendria.model.AddToCardServiceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murugan on 07/03/16.
 */
public class AppointmentConfirmSingleTone {

    private static AppointmentConfirmSingleTone addToCardSingleTone;

    public List<AddToCardServiceModel> addToCardArrayList;

    private AppointmentConfirmSingleTone() {
        addToCardArrayList = new ArrayList<AddToCardServiceModel>();

    }
    public static AppointmentConfirmSingleTone getInstance() {
        if (addToCardSingleTone == null) {
            addToCardSingleTone = new AppointmentConfirmSingleTone();
        }

        return addToCardSingleTone;
    }
    public List<AddToCardServiceModel> getParamList() {
        return addToCardArrayList;
    }

    public void setParamList(List<AddToCardServiceModel> paramList) {
        this.addToCardArrayList = paramList;
    }

}
