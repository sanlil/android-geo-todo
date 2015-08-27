package com.example.geotodo;

import java.util.ArrayList;
import java.util.UUID;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class TaskFragment extends Fragment {
	private static final String TAG = "TaskFragment";
	public static final String EXTRA_TASK_ID = "sandrasplayground.summercourse.criminalintent.task_id";

	private Task mTask;
	private EditText mTitleField;
	private ImageView mSaveButton;

	public static TaskFragment newInstance(UUID taskId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TASK_ID, taskId);

		TaskFragment fragment = new TaskFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		UUID taskId = (UUID) getArguments().getSerializable(EXTRA_TASK_ID);
		ArrayList<Place> places = PlaceStorage.get(getActivity()).getPlaces();
		for (Place place : places) {
			TaskList tl = place.getTaskList();
			if (tl.getTask(taskId) != null) {
				mTask = tl.getTask(taskId);
			}
		}
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_task, parent, false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		mTitleField = (EditText) v.findViewById(R.id.task_title);
		mTitleField.setText(mTask.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				mTask.setTitle(c.toString());
			}

			public void beforeTextChanged(CharSequence c, int start, int count,
					int after) {
				// intentionally left blank
			}

			public void afterTextChanged(Editable c) {
				// intentionally left blank
			}
		});

		mSaveButton = (ImageView) v.findViewById(R.id.task_save_button);
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent i = new Intent(WinActivity.this, GameActivity.class);
				// startActivity(i);
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
			}
		});

		return v;
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause()");
		super.onPause();
		PlaceStorage.get(getActivity()).savePlaces();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
