package com.example.geotodo;

import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TaskListActivity extends FragmentActivity {

	UUID placeId;

	protected Fragment createFragment() {
		// return new TaskListFragment();
		placeId = (UUID) getIntent().getSerializableExtra(
				TaskListFragment.EXTRA_PLACE_ID);
		return TaskListFragment.newInstance(placeId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);

		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment)
					.commit();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fragment_task_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_task:
			Task task = new Task();
			TaskList taskList = PlaceStorage.get(this).getPlace(placeId)
					.getTaskList();
			taskList.addTask(task);
			Intent i = new Intent(this, TaskActivity.class);
			i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
			startActivityForResult(i, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
