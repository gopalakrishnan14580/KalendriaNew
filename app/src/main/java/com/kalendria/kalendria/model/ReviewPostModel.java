package com.kalendria.kalendria.model;

/**
 * Created by mansoor on 18/03/16.
 */
public class ReviewPostModel {



    private   String businessID;

    private   String businessName;
    private   String serviceID;
    private   String serviceName;

    public String getRattingFromList() {
        return rattingFromList;
    }

    public void setRattingFromList(String rattingFromList) {
        this.rattingFromList = rattingFromList;
    }

    private   String rattingFromList;

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
