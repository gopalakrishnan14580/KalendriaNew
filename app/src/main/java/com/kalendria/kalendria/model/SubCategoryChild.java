package com.kalendria.kalendria.model;

public class SubCategoryChild {
    private String subcategoryName;
    private String subcategoryId;
    private String parentId;
    private String parentName;
    public  String  sector;

    public SubCategoryChild() {
    }

    public String getParentName() {

        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }



    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }



    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }
}