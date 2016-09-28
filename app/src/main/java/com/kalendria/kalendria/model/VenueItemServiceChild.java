package com.kalendria.kalendria.model;

/**
 * Created by mansoor on 18/03/16.
 */
public class VenueItemServiceChild {


    private   String name;
    private   String id;
    private   String price;
    private   String duration;
    private   String discount,discription;
    public int discountAmount;
    public int remainAmount;

    //code modified by magesh
    public String serviceID;
    public String sector;
    public String getserviceID() {
        return serviceID;
    }
    public void  setServiceID(String serviceID) { this.serviceID=serviceID;}



    // Venue Details

    private   String venuename;
    private   String venueid;
    private   String venuecity;
    private   String veneregiion;
    private   String venueImage;

    public String getVenuename() {
        return venuename;
    }

    public void setVenuename(String venuename) {
        this.venuename = venuename;
    }

    public String getVenueid() {
        return venueid;
    }

    public void setVenueid(String venueid) {
        this.venueid = venueid;
    }

    public String getVenuecity() {
        return venuecity;
    }

    public void setVenuecity(String venuecity) {
        this.venuecity = venuecity;
    }

    public String getVeneregiion() {
        return veneregiion;
    }

    public void setVeneregiion(String veneregiion) {
        this.veneregiion = veneregiion;
    }

    public String getVenueImage() {
        return venueImage;
    }

    public void setVenueImage(String venueImage) {
        this.venueImage = venueImage;
    }

    public String getPrice() {
        return price;
    }
    public int getIntPrice()
    {
        try {


            return Integer.parseInt(price);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        if (duration!=null && !duration.equalsIgnoreCase("null")) {
            return duration;
        }

        return "";
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void calculateDiscountAmount()
    {

        String price = this.getPrice();
        String dis = this.getDiscount();

        int priceAmt = 0;
        int dictAmt = 0;

        if(price != null && !price.equalsIgnoreCase("0") &&  !price.equalsIgnoreCase("null")){
            try {
                priceAmt = Integer.parseInt(price);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if(dis != null && !dis.equalsIgnoreCase("null")  && !dis.equalsIgnoreCase("0")){
            try {
                dictAmt = Integer.parseInt(dis);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        Double  priceAmt2f =Double.parseDouble(priceAmt+"");
        Double pricef = (Double)100.0;

        Double calAmt1 = priceAmt2f/ pricef;
        Double calAmt = calAmt1 * dictAmt;

        this.discountAmount = calAmt.intValue();
        this.remainAmount = 0;

        this.remainAmount = priceAmt - discountAmount;

    }

    public String getStrikeOutAmount()
    {
        if(discountAmount != 0){
            return   this.getPrice() + " " + "AED";
        }

        return  "";
    }

    public String getOriginalPrices()
    {
        return  remainAmount + " " + "AED";
    }

}
