package com.kalendria.kalendria.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MyorderModel implements Serializable {

	private String morderVenuName;
	private String morderServiceName;
	private String morderTime;
	private String morderDate;
	private String morderStatus;
	private String morderPoints;
	private String morderPrice;

	public String mOrderDiscount;
	public String mOrderPayAt;
	public boolean isCurrent;

	public String getMorderVenuName() {
		return morderVenuName;
	}

	public void setMorderVenuName(String morderVenuName) {
		this.morderVenuName = morderVenuName;
	}

	public String getMorderPrice() {
		return morderPrice;
	}

	public void setMorderPrice(String morderPrice) {
		this.morderPrice = morderPrice;
	}

	public String getMorderPoints() {
		return morderPoints;
	}

	public void setMorderPoints(String morderPoints) {
		this.morderPoints = morderPoints;
	}

	public String getMorderDate() {
		return morderDate;
	}

	public void setMorderDate(String morderDate) {
		this.morderDate = morderDate;
	}

	public String getMorderTime() {
		return morderTime;
	}

	public void setMorderTime(String morderTime) {
		this.morderTime = morderTime;
	}

	public String getMorderServiceName() {
		return morderServiceName;
	}

	public void setMorderServiceName(String morderServiceName) {
		this.morderServiceName = morderServiceName;
	}

	public String mOrderId;
	public String mOrderBusinessId;
	public String getMorderId()
	{
		return mOrderId;
	}
	public void setMorderId(String morderId)
	{
		this.mOrderId=morderId;
	}
	public String getmOrderBusinessId()
	{
		return mOrderBusinessId;
	}
	public void setmOrderBusinessId(String mOrderBusinessId)
	{
		this.mOrderBusinessId=mOrderBusinessId;
	}
	public String getMorderStatus() {
		return morderStatus;
	}

	public void setMorderStatus(String morderStatus) {
		this.morderStatus = morderStatus;
	}
}