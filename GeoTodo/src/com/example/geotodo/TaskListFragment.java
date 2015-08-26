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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
		setHasOptionsMenu(true);
		UUID placeId = (UUID) getArguments().getSerializable(EXTRA_PLACE_ID);
		Place place = PlaceStorage.get(getActivity()).getPlace(placeId);
		mTaskList = place.getTaskList();
		TaskAdapter adapter = new TaskAdapter(mTaskList.getTasks());
		setListAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		((TaskAdapter) getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);

		ListView listView = (ListView) v.findViewById(android.R.id.list);
		registerForContextMenu(listView);

		// if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
		// // Use floating context menus on Froyo and Gingerbread
		// registerForContextMenu(listView);
		// } else {
		// // Use contextual action bar on Honeycomb and higher
		// listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		// }

		return v;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Task task = ((TaskAdapter) getListAdapter()).getItem(position);
		Log.d(TAG, task.getTitle() + "was clicked");

		// Start TaskActivity
		Intent i = new Intent(getActivity(), TaskActivity.class);
		// Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
		startActivity(i);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_task_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_task:
			Task task = new Task();
			mTaskList.addTask(task);
			Intent i = new Intent(getActivity(), TaskActivity.class);
			// Intent i = new Intent(getActivity(), CrimePagerActivity.class);
			i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
			startActivityForResult(i, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							Log.d(TAG, "Checked is changed");
							Log.d(TAG,
									task.getTitle() + " isDone: "
											+ !task.isDone());
							task.setDone(!task.isDone());
							PlaceStorage.get(getActivity()).savePlaces();
						}
					});

			return convertView;
		}

	}

}
