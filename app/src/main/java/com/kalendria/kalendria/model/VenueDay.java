package com.kalendria.kalendria.model;

/**
 * Created by mansoor on 11/03/16.
 */
public class VenueDay {

    private String day;
    private String start_time;
    private String end_time;

    //--- Coded by Magesh
    public int order;
    public boolean isOpen;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
// End

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
