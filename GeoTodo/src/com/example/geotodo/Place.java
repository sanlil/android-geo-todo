package com.example.geotodo;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Place {

	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_LONGITUDE = "longitude";
	private static final String JSON_LATITUDE = "latitude";
	private static final String JSON_TASKS = "tasks";

	private UUID mId;
	private String mTitle;
	private double mLongitude;
	private double mLatitude;
	private TaskList mTaskList;

	public Place() {
		mId = UUID.randomUUID(); // Generate unique identifier
		mTaskList = new TaskList();
	}

	public Place(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		mLongitude = json.getDouble(JSON_LONGITUDE);
		mLatitude = json.getDouble(JSON_LATITUDE);
		JSONArray taskarray = json.getJSONArray(JSON_TASKS);
		mTaskList = new TaskList(taskarray);
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
		return mTaskList;
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

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle.toString());
		json.put(JSON_LONGITUDE, String.valueOf(mLongitude));
		json.put(JSON_LATITUDE, String.valueOf(mLatitude));
		json.put(JSON_TASKS, mTaskList.toJSON());
		return json;
	}
}
