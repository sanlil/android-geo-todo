package com.example.geotodo;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class TaskStorage {

	private ArrayList<Task> mTasks;
	private static TaskStorage mTaskStorage;
	private Context mAppContext;

	private TaskStorage(Context appContext) {
		mAppContext = appContext;
		mTasks = new ArrayList<Task>();
	}

	public static TaskStorage get(Context c) {
		if (mTaskStorage == null) {
			mTaskStorage = new TaskStorage(c.getApplicationContext());
		}
		return mTaskStorage;
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

}
