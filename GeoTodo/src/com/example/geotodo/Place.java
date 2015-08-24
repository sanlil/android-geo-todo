package com.example.geotodo;

import java.util.UUID;

public class Place {

	private UUID mId;
	private String mTitle;
	private double mLongitude;
	private double mLatitude;
	private TaskList taskList;

	public Place() {
		mId = UUID.randomUUID(); // Generate unique identifier
		taskList = new TaskList();
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

	public TaskList getTaskList() {
		return taskList;
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
