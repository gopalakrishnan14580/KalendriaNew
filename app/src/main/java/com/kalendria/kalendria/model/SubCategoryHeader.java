package com.kalendria.kalendria.model;

/**
 * Created by mansoor on 11/03/16.
 */
public class SubCategoryHeader {
    private String subcategoryHeaderName;
    private String subcategoryHeaderId;
    public String sector;

    public String getSubcategoryHeaderParent() {
        return subcategoryHeaderParent;
    }

    public void setSubcategoryHeaderParent(String subcategoryHeaderParent) {
        this.subcategoryHeaderParent = subcategoryHeaderParent;
    }

    private String subcategoryHeaderParent;

    public String getSubcategoryHeaderId() {
        return subcategoryHeaderId;
    }

    public void setSubcategoryHeaderId(String subcategoryHeaderId) {
        this.subcategoryHeaderId = subcategoryHeaderId;
    }

    public String getSubcategoryHeaderName() {
        return subcategoryHeaderName;
    }

    public void setSubcategoryHeaderName(String subcategoryHeaderName) {
        this.subcategoryHeaderName = subcategoryHeaderName;
    }
}
