package com.example.geotodo;

import java.util.UUID;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class PlaceFragment extends Fragment {
	public static final String EXTRA_PLACE_ID = "sandrasplayground.summercourse.criminalintent.place_id";

	private Place mPlace;
	private EditText mTitleField;

	public static PlaceFragment newInstance(UUID placeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_PLACE_ID, placeId);

		PlaceFragment fragment = new PlaceFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		UUID placeId = (UUID) getArguments().getSerializable(EXTRA_PLACE_ID);
		mPlace = PlaceList.get().getPlace(placeId);
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_place, parent, false);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		mTitleField = (EditText) v.findViewById(R.id.place_title);
		mTitleField.setText(mPlace.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				mPlace.setTitle(c.toString());
			}

			public void beforeTextChanged(CharSequence c, int start, int count,
					int after) {
				// intentionally left blank
			}

			public void afterTextChanged(Editable c) {
				// intentionally left blank
			}

		});

		return v;
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
