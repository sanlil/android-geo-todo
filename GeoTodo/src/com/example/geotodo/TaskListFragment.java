package com.example.geotodo;

import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.TextView;

public class TaskListFragment extends ListFragment {

	private static final String TAG = "TaskListFragment";
	private ArrayList<Task> mTasks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mTasks = TaskStorage.get(getActivity()).getTasks();
		TaskAdapter adapter = new TaskAdapter(mTasks);
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
			TaskStorage.get(getActivity()).addTask(task);
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
			TaskStorage.get(getActivity()).deleteTask(task);
			adapter.notifyDataSetChanged();
			return true;
		case R.id.menu_item_edit_task:
			// Go to edit page
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

			Task task = getItem(position);

			TextView titleTextView = (TextView) convertView
					.findViewById(R.id.task_list_item_titleTextView);
			titleTextView.setText(task.getTitle());
			TextView dateTextView = (TextView) convertView
					.findViewById(R.id.task_list_item_dateTextView);
			dateTextView.setText(task.getDueDate().toString());
			CheckBox solvedCheckBox = (CheckBox) convertView
					.findViewById(R.id.task_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(task.isDone());

			return convertView;
		}
	}

}