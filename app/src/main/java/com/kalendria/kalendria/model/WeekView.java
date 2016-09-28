package com.kalendria.kalendria.model;

import java.util.ArrayList;

/**
 * Created by mansoor on 02/05/16.
 */
public class WeekView {
    public int currentWeek;
    public ArrayList<KADate> currentWeekDays;
    public String currentMonth;
    public String currentYear;
    public String currentDay;
    public String currentDate;

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
    }

    public ArrayList<KADate> getCurrentWeekDays() {
        return currentWeekDays;
    }

    public void setCurrentWeekDays(ArrayList<KADate> currentWeekDays) {
        this.currentWeekDays = currentWeekDays;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public WeekView(){

    }
}
