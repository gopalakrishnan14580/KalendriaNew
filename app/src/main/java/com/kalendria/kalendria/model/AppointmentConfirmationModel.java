package com.kalendria.kalendria.model;

/**
 * Created by murugan on 01/03/16.
 */
public class AppointmentConfirmationModel {


    private String serviceName;
    private String staffName;
    private String startTimeEndTime;
    private String duritaion;
    private String servicePrice;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStartTimeEndTime() {
        return startTimeEndTime;
    }

    public void setStartTimeEndTime(String startTimeEndTime) {
        this.startTimeEndTime = startTimeEndTime;
    }

    public String getDuritaion() {
        return duritaion;
    }

    public void setDuritaion(String duritaion) {
        this.duritaion = duritaion;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }
}
