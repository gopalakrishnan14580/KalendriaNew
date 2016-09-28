package com.kalendria.kalendria.model;

import com.kalendria.kalendria.activity.VenueItem;

import java.util.Comparator;

/**
 * Created by mansoor on 11/03/16.
 */
public class BookWithLoyalityModel extends ServiceModal {


    private String points;
    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }


    public static Comparator<BookWithLoyalityModel> sortPoints = new Comparator<BookWithLoyalityModel>() {

        public int compare(BookWithLoyalityModel s1, BookWithLoyalityModel s2) {

            int points1 = Integer.parseInt(s1.getPoints());
            int points2 = Integer.parseInt(s2.getPoints());

    /*For ascending order*/
            return points1 - points2;

    /*For descending order*/
            //points2-points1;
           // return points2 - points1;


        }};

}
