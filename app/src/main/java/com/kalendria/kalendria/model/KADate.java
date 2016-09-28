package com.kalendria.kalendria.model;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Superman on 5/23/16.
 */
public class KADate {

    public int day;
    public int month;
    public int year;
    public int unique;
    public  String dayLongName;

    public KADate(Calendar calender)
    {
        day = calender.get(Calendar.DAY_OF_MONTH);
        month = calender.get(Calendar.MONTH)+1;// because jan start from 0
        year = calender.get(Calendar.YEAR);

        unique = year*10000+month*100+day;

        dayLongName = calender.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);

    }
    public static String todayLongName()
    {
        Calendar calender = Calendar.getInstance();
        return calender.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);

    }
}
