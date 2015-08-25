package com.example.geotodo;

import java.util.ArrayList;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class TaskList {

	private ArrayList<Task> mTasks;

	public TaskList() {
		mTasks = new ArrayList<Task>();
	}

	public TaskList(JSONArray taskarray) {
		mTasks = new ArrayList<Task>();
		try {
			for (int i = 0; i < taskarray.length(); i++) {
				Task task = new Task(taskarray.getJSONObject(i));
				mTasks.add(task);
			}
		} catch (Exception e) {
			Log.e(DataJSONConverter.TAG, "Error when loading tasks: ", e);
		}
	}

	public void addTask(Task t) {
		mTasks.add(t);
	}

	public ArrayList<Task> getTasks() {
		return mTasks;
	}

	public Task getTask(UUID id) {
		for (Task t : mTasks) {
			if (t.getId().equals(id)) {
				return t;
			}
		}
		return null;
	}

	public void deleteTask(Task t) {
		mTasks.remove(t);
	}

	public JSONArray toJSON() throws JSONException {
		JSONArray json = new JSONArray();
		for (Task t : mTasks) {
			json.put(t.toJSON());
		}
		return json;
	}

}
