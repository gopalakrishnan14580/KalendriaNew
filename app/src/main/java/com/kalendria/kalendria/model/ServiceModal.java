package com.kalendria.kalendria.model;

/**
 * Created by Superman on 6/4/16.
 */
public class ServiceModal extends Venue {


    //it come from json arrray
    private String serviceName;
    private String service_id;
    private String price;
    private String discount;
    private String discounted_price;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    private String duration;



    public String getServiceId() {
        return service_id;
    }

    public void setServiceId(String service_id) {
        this.service_id = service_id;
    }






    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String name_service) {
        this.serviceName = name_service;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(String discounted_price) {
        this.discounted_price = discounted_price;
    }

}
