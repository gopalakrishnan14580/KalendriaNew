package com.kalendria.kalendria.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ReviewModel implements Serializable {

	private String reviewUserName;
	private String reviewUserTampImage_url;
	private String favorateImage_thumb;
	private String reviewCommants;

	public String getReviewRatting() {
		return reviewRatting;
	}

	public void setReviewRatting(String reviewRatting) {
		this.reviewRatting = reviewRatting;
	}

	private String reviewRatting;


	private String reviewResponceUserName;
	private String reviewResponceUserTampImage_url;
	private String reviewResponceUserCommands;

	public String getReviewUserName() {
		return reviewUserName;
	}

	public void setReviewUserName(String reviewUserName) {
		this.reviewUserName = reviewUserName;
	}

	public String getReviewUserTampImage_url() {
		return reviewUserTampImage_url;
	}

	public void setReviewUserTampImage_url(String reviewUserTampImage_url) {
		this.reviewUserTampImage_url = reviewUserTampImage_url;
	}

	public String getFavorateImage_thumb() {
		return favorateImage_thumb;
	}

	public void setFavorateImage_thumb(String favorateImage_thumb) {
		this.favorateImage_thumb = favorateImage_thumb;
	}

	public String getReviewCommants() {
		return reviewCommants;
	}

	public void setReviewCommants(String reviewCommants) {
		this.reviewCommants = reviewCommants;
	}

	public String getReviewResponceUserName() {
		return reviewResponceUserName;
	}

	public void setReviewResponceUserName(String reviewResponceUserName) {
		this.reviewResponceUserName = reviewResponceUserName;
	}

	public String getReviewResponceUserTampImage_url() {
		return reviewResponceUserTampImage_url;
	}

	public void setReviewResponceUserTampImage_url(String reviewResponceUserTampImage_url) {
		this.reviewResponceUserTampImage_url = reviewResponceUserTampImage_url;
	}

	public String getReviewResponceUserCommands() {
		return reviewResponceUserCommands;
	}

	public void setReviewResponceUserCommands(String reviewResponceUserCommands) {
		this.reviewResponceUserCommands = reviewResponceUserCommands;
	}
}