package com.kalendria.kalendria.model;

/**
 * Created by mansoor on 11/03/16.
 */
public class Venue {

    private String buisnessName;
    private String buisnessId;
    private String address;
    private String city;
    private String region;
    private String lat;
    private String longId;
    private String description;
    private String overallRating;
    private String reviewCount;
    private String sector;
    //it come from json object
    private String imageUrl;
    private String imageUrlThumb;
    public double distance;
    public String getImageUrlThumb() {
        return imageUrlThumb;
    }

    public void setImageUrlThumb(String imageUrlThumb) {
        this.imageUrlThumb = imageUrlThumb;
    }

    public String getBuisnessName() {
        return buisnessName;
    }

    public void setBuisnessName(String name) {
        this.buisnessName = name;
    }

    public String getBuisnessId() {
        return buisnessId;
    }

    public void setBuisnessId(String id) {
        this.buisnessId = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongId() {
        return longId;
    }

    public void setLongId(String longId) {
        this.longId = longId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }


    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }


}
