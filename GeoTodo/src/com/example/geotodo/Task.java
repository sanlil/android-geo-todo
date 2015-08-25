package com.example.geotodo;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {

	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_DONE = "isDone";

	private UUID mId;
	private String mTitle;
	private Date mDueDate;
	private boolean mIsDone;

	public Task() {
		mId = UUID.randomUUID(); // Generate unique identifier
		mDueDate = new Date();
	}

	public Task(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		mIsDone = json.getBoolean(JSON_DONE);
		mDueDate = new Date();
	}

	public UUID getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public Date getDueDate() {
		return mDueDate;
	}

	public boolean isDone() {
		return mIsDone;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public void setDueDate(Date dueDate) {
		mDueDate = dueDate;
	}

	public void setDone(boolean isDone) {
		mIsDone = isDone;
	}

	@Override
	public String toString() {
		return mTitle;
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle.toString());
		json.put(JSON_DONE, String.valueOf(mIsDone));
		return json;
	}

}
