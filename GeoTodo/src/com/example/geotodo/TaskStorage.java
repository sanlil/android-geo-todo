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

	public void addTask(Task c) {
		mTasks.add(c);
	}

	public ArrayList<Task> getTasks() {
		return mTasks;
	}

	public Task getTask(UUID id) {
		for (Task c : mTasks) {
			if (c.getId().equals(id)) {
				return c;
			}
		}
		return null;
	}

	public void deleteTask(Task c) {
		mTasks.remove(c);
	}

}
