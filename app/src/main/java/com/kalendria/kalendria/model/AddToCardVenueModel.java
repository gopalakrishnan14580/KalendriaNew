package com.kalendria.kalendria.model;

import com.google.android.gms.appindexing.Thing;

import java.util.ArrayList;

/**
 * Created by murugan on 01/03/16.
 */
public class AddToCardVenueModel {


    //venue details start

    private String venueName;
    private String venueID;
    private String venueOverallRatting;
    private String venuReviewCount;
    private String venuImage;

    //Purchase constants
    public  boolean isVoucher ;
    public  boolean isGift ;
    public  int totAmtPaid ;
    public  boolean isLoyalty ;
    public String userPhone;
    private String userSector;
    public KACoupen coupen;
    public KACoupen gift;
    public  int discountedNetPayable;
    public boolean isFromLoyality;
    private String how_often;
    private String is_iron;

    public String getVenuImagethumb() {
        return venuImagethumb;
    }

    public void setVenuImagethumb(String venuImagethumb) {
        this.venuImagethumb = venuImagethumb;
    }

    private String venuImagethumb;



    public AddToCardVenueModel() {
    }

    public void setUserSector(String userSector) {
        this.userSector=userSector;
    }
    public String getUserSector() {
        return userSector==null?"1":userSector;
    }
    /*below code for child items start */
    private ArrayList<AddToCardServiceModel> Items;

    public ArrayList<AddToCardServiceModel> getItems() {
        return Items;
    }

    public void setItems(ArrayList<AddToCardServiceModel> items) {
        Items = items;
    }
    /*below code for child items end */

    public String getVenuImage() {
        return venuImage;
    }

    public void setVenuImage(String venuImage) {
        this.venuImage = venuImage;
    }


    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueID() {
        return venueID;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }


    public String getIs_iron() {
        if(is_iron==null)return "false";
        return is_iron;
    }

    public void setHow_often(String how_often) {
        this.how_often = how_often;
    }

    public String getHow_often() {
        if(how_often==null)return "";
        return how_often;
    }

    public void setIs_iron(String is_iron) {
        this.is_iron = is_iron;
    }



    public String getVenueOverallRatting() {
        return venueOverallRatting;
    }

    public void setVenueOverallRatting(String venueOverallRatting) {
        this.venueOverallRatting = venueOverallRatting;
    }

    public String getVenuReviewCount() {
        return venuReviewCount;
    }

    public void setVenuReviewCount(String venuReviewCount) {
        this.venuReviewCount = venuReviewCount;
    }


    //cisty and region
    private String city;
    private String region;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


}
