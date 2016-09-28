package com.kalendria.kalendria.model;

/**
 * Created by murugan on 01/03/16.
 */
public class AddToCardServiceModel {


    private  String serviceId;
    private  String serviceName;
    private  String servicePrice;
    private  String serviceDiscount;
    private  String serviceDuration;
    private  String servicePoints;
    private  String serviceImage;

    public  boolean isOpen;
    public  boolean isFromLoyality;
    public int discountAmount;
    public int remainAmount;
    public boolean isValid;
    private  String serviceId2;

    public   String selectedDate;
    public  String selectedTime;

    public  String staffname;
    public  String staffID;
    public  String staffthumbImage;

    private   String sector;

    public String getstaffname() {

        if(staffname==null) return "";
        return staffname;
    }


    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId2() {
        return serviceId2;
    }

    public void setServiceId2(String serviceId2) {
        this.serviceId2 = serviceId2;
    }


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getServiceDiscount() {
        return serviceDiscount;
    }
    public int getIntServiceDiscount() {
        try {


            return Integer.parseInt(serviceDiscount);
        }
        catch (Exception ex){return 0;}
    }

    public void setServiceDiscount(String serviceDiscount) {
        this.serviceDiscount = serviceDiscount;
    }

    public String getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(String serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public String getServicePoints() {
        return servicePoints;
    }
    public int getIntServicePoints() {
        try {


            return Integer.parseInt(servicePoints);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

    public void setServicePoints(String servicePoints) {
        this.servicePoints = servicePoints;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }


    //venue details start


    private String venueName;
    private String venueID;
    private String venueOverallRatting;
    private String venuReviewCount;
    private String venuImage;

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

    public void calculateDiscountAmount()
    {

        String price = this.getServicePrice();
        String dis = this.getServiceDiscount();

        int priceAmt = 0;
        int dictAmt = 0;

        if(price != null && !price.equalsIgnoreCase("0")){
            try {
                priceAmt = Integer.parseInt(price);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if(dis != null && !dis.equalsIgnoreCase("0")){
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
            if(isFromLoyality)
            return   "";
            else
                return   this.getServicePrice() + " " + "AED";
        }

        return  "";
    }

    public int getOriginalPrices()
    {
        if(isFromLoyality) return getIntServicePoints();
        return  remainAmount ;
    }
    public String getOriginalDisplayPrices()
    {
        if(isFromLoyality) return getServicePoints() +" Points";
        return  remainAmount + " " + "AED";
    }

}
