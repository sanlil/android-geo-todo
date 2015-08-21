package com.example.geotodo;

import java.util.Date;
import java.util.UUID;

public class Task {

	private UUID mId;
	private String mTitle;
	private Date mDueDate;
	private boolean mIsDone;

	public Task() {
		mId = UUID.randomUUID(); // Generate unique identifier
		mDueDate = new Date();
	}

	public UUID getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public Date getDueDate() {
		return mDueDate;
	}

	public void setDueDate(Date dueDate) {
		mDueDate = dueDate;
	}

	public boolean isDone() {
		return mIsDone;
	}

	public void setDone(boolean isDone) {
		mIsDone = isDone;
	}

	@Override
	public String toString() {
		return mTitle;
	}

}
