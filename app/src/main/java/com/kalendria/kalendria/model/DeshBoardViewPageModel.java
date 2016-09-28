package com.kalendria.kalendria.model;

/**
 * Created by murugan on 01/03/16.
 */
public class DeshBoardViewPageModel {


    private  String serviceId;
    private  String serviceName;
    private  String servicePrice;
    private  String serviceDiscount;
    private  String serviceDuration;
    private  String servicePoints;
    private  String serviceImage;
    public   String sector;//added by magesh

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
    private String venuImageThamp;

    public String getVenuImageThamp() {
        return venuImageThamp;
    }

    public void setVenuImageThamp(String venuImageThamp) {
        this.venuImageThamp = venuImageThamp;
    }




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
}
