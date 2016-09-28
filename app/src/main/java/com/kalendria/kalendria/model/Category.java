package com.kalendria.kalendria.model;

/**
 * Created by mansoor on 02/03/16.
 */
public class Category {
    private String categoryName;
    private String categoryId;
    private String categoryImages;
    public  String sector;

    public Category(String category_name, String category_images, String category_id) {
        this.categoryName = category_name;
        this.categoryImages = category_images;
        this.categoryId = category_id;
    }

    public Category() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryImages() {
        return categoryImages;
    }

    public void setCategoryImages(String categoryImages) {
        this.categoryImages = categoryImages;
    }


}
