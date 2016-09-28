package com.kalendria.kalendria.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FavorateModel implements Serializable {

	private String favorateVenuName;
	private String favorateImage_url;
	private String favorateImage_thumb;
	private String favorateRatting;
	private String favorateReview;
	private String favorateVenue_ID;
	private String favorateVenue_lat;
	private String favorateVenue_long;
	public String favorateVenue_city;
	public String favorateVenue_region;

	public String getFavorateVenue_lat() {
		return favorateVenue_lat;
	}

	public void setFavorateVenue_lat(String favorateVenue_lat) {
		this.favorateVenue_lat = favorateVenue_lat;
	}

	public String getFavorateVenue_long() {
		return favorateVenue_long;
	}

	public void setFavorateVenue_long(String favorateVenue_long) {
		this.favorateVenue_long = favorateVenue_long;
	}

	public String getFavorateVenue_ID() {
		return favorateVenue_ID;
	}

	public void setFavorateVenue_ID(String favorateVenue_ID) {
		this.favorateVenue_ID = favorateVenue_ID;
	}

	public String getFavorateImage_thumb() {
		return favorateImage_thumb;
	}

	public void setFavorateImage_thumb(String favorateImage_thumb) {
		this.favorateImage_thumb = favorateImage_thumb;
	}

	public String getFavorateImage_url() {
		return favorateImage_url;
	}

	public void setFavorateImage_url(String favorateImage_url) {
		this.favorateImage_url = favorateImage_url;
	}


	public String getFavorateVenuName() {
		return favorateVenuName;
	}

	public void setFavorateVenuName(String favorateVenuName) {
		this.favorateVenuName = favorateVenuName;
	}


	public String getLocation()
	{
	String locationStr="";
		if(favorateVenue_region !=null)
			locationStr +=favorateVenue_region;
		if(favorateVenue_city!=null && favorateVenue_city.length()>0)
		{
			if(locationStr.length()>0) locationStr+= ", "+favorateVenue_city;
			else
				return favorateVenue_city;
		}

		return locationStr;
	}



	public String getFavorateRatting() {
		return favorateRatting;
	}

	public void setFavorateRatting(String favorateRatting) {
		this.favorateRatting = favorateRatting;
	}

	public String getFavorateReview() {
		return favorateReview;
	}

	public void setFavorateReview(String favorateReview) {
		this.favorateReview = favorateReview;
	}
}