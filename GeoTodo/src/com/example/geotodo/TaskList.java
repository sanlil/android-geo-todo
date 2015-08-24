package com.example.geotodo;

import java.util.ArrayList;
import java.util.UUID;

public class TaskList {

	private ArrayList<Task> mTasks;

	public TaskList() {
		mTasks = new ArrayList<Task>();
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
