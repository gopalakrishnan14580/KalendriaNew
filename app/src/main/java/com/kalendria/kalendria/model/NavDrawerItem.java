package com.kalendria.kalendria.model;

public class NavDrawerItem {

    private String title;
    private String image;

    public NavDrawerItem() {}

    public NavDrawerItem(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}