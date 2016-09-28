package com.kalendria.kalendria.model;

import java.util.ArrayList;

/**
 * Created by mansoor on 18/03/16.
 */
public class VeneuItemServiceHeader {



    private   String name;
    private   String business_name;
    private   String sub_category;


    /*below code for child items start */
    private ArrayList<VenueItemServiceChild> Items;

    public ArrayList<VenueItemServiceChild> getItems() {
        return Items;
    }

    public void setItems(ArrayList<VenueItemServiceChild> items) {
        Items = items;
    }
    /*below code for child items end */




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }


}
