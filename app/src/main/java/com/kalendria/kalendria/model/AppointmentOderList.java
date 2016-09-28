package com.kalendria.kalendria.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AppointmentOderList implements Serializable {


	private String oderId;

	public String getStarTime() {
		return starTime;
	}

	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}

	private String starTime;
	private String serviceName;
	private String staffName;
	private String day;
	private String durition;

	private String orderStatus;

	private String paymentType;
	private String paymentStatus;
	private String orderTotal;
	private String payAtVenue;









	public String getOderId() {
		return oderId;
	}

	public void setOderId(String oderId) {
		this.oderId = oderId;
	}

	public String getPayAtVenue() {
		return payAtVenue;
	}

	public void setPayAtVenue(String payAtVenue) {
		this.payAtVenue = payAtVenue;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getDurition() {
		return durition;
	}

	public void setDurition(String durition) {
		this.durition = durition;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
}