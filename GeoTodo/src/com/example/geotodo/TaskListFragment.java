package com.example.geotodo;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TaskListFragment extends ListFragment {
	public static final String EXTRA_PLACE_ID = "sandrasplayground.summercourse.criminalintent.place_id";

	private static final String TAG = "TaskListFragment";
	private TaskList mTaskList;

	public static TaskListFragment newInstance(UUID placeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_PLACE_ID, placeId);

		TaskListFragment fragment = new TaskListFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID placeId = (UUID) getArguments().getSerializable(EXTRA_PLACE_ID);
		Place place = PlaceStorage.get(getActivity()).getPlace(placeId);
		mTaskList = place.getTaskList();
		TaskAdapter adapter = new TaskAdapter(mTaskList.getTasks());
		setListAdapter(adapter);
		Log.d(TAG, "onCreate()");
	}

	@Override
	public void onResume() {
		super.onResume();
		((TaskAdapter) getListAdapter()).notifyDataSetChanged();
		Log.d(TAG, "onResume()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		View v = super.onCreateView(inflater, parent, savedInstanceState);

		ListView listView = (ListView) v.findViewById(android.R.id.list);
		registerForContextMenu(listView);

		return v;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.task_list_item_context,
				menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		int position = info.position;
		TaskAdapter adapter = (TaskAdapter) getListAdapter();
		Task task = adapter.getItem(position);
		switch (item.getItemId()) {
		case R.id.menu_item_delete_task:
			// Delete item
			mTaskList.deleteTask(task);
			adapter.notifyDataSetChanged();
			return true;
		case R.id.menu_item_edit_task:
			// Go to edit page
			Intent i = new Intent(getActivity(), TaskActivity.class);
			i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
			startActivityForResult(i, 0);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private class TaskAdapter extends ArrayAdapter<Task> {

		public TaskAdapter(ArrayList<Task> tasks) {
			super(getActivity(), 0, tasks);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_task, null);
			}

			final Task task = getItem(position);

			TextView titleView = (TextView) convertView
					.findViewById(R.id.task_list_item_title);
			titleView.setText(task.getTitle());

			CheckBox solvedCheckBox = (CheckBox) convertView
					.findViewById(R.id.task_list_item_solved);
			solvedCheckBox.setChecked(task.isDone());
			solvedCheckBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked != task.isDone()) {
								task.setDone(isChecked);
								Log.d(TAG, "Checked is changed");
								Log.d(TAG, task.getTitle() + " isDone: "
										+ isChecked);
								PlaceStorage.get(getActivity()).savePlaces();
							}
						}
					});

			ImageView deleteButton = (ImageView) convertView
					.findViewById(R.id.task_delete_button);
			deleteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d(TAG, "remove task " + task.getTitle());
					mTaskList.deleteTask(task);
					notifyDataSetChanged();
				}
			});

			return convertView;
		}

	}

}
