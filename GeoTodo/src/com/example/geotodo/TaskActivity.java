package com.example.geotodo;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class TaskActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		UUID taskId = (UUID) getIntent().getSerializableExtra(
				TaskFragment.EXTRA_TASK_ID);
		return TaskFragment.newInstance(taskId);
	}

}
