package com.example.geotodo;

import java.util.UUID;

public class Place {

	private UUID mId;
	private String mTitle;
	private double mLongitude;
	private double mLatitude;

	public Place() {
		mId = UUID.randomUUID(); // Generate unique identifier
	}

	public UUID getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public double getLongitude() {
		return mLongitude;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

	@Override
	public String toString() {
		return mTitle;
	}

}
