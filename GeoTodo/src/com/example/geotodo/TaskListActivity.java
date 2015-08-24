package com.example.geotodo;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class TaskListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		// return new TaskListFragment();
		UUID placeId = (UUID) getIntent().getSerializableExtra(
				TaskListFragment.EXTRA_PLACE_ID);
		return TaskListFragment.newInstance(placeId);
	}

}
