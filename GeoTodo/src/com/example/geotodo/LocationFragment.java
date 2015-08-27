package com.example.geotodo;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class LocationFragment extends Fragment implements ConnectionCallbacks,
		OnConnectionFailedListener {

	static final String TAG = "LastKnownLocation";

	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	// private Place currentPlace;
	private TextView mPlaceTitleView;
	private TextView mLatitudeView;
	private TextView mLongitudeView;
	private LinearLayout mHeaderLayout;
	private LinearLayout mNewTaskLayout;
	private Button mNewTaskButton;

	// private ImageView mSaveButton;
	// private EditText mNewTaskText;

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView()");
		View v = inflater.inflate(R.layout.fragment_location_container,
				container, false);
		mPlaceTitleView = (TextView) v.findViewById(R.id.place_list_item_title);
		mLatitudeView = (TextView) v
				.findViewById(R.id.place_list_item_latitude);
		mLongitudeView = (TextView) v
				.findViewById(R.id.place_list_item_longitude);
		mHeaderLayout = (LinearLayout) v
				.findViewById(R.id.current_place_header);
		mNewTaskLayout = (LinearLayout) v
				.findViewById(R.id.new_task_button_layout);
		mNewTaskButton = (Button) v.findViewById(R.id.new_task_button);
		// mNewTaskText = (EditText) v.findViewById(R.id.new_task_title);
		// mSaveButton = (ImageView) v.findViewById(R.id.task_save_button);
		showTaskListFragment(new NoPlaceFragment());
		buildGoogleApiClient();

		return v;
	}

	@Override
	public void onStart() {
		Log.d(TAG, "onStart()");
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop()");
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume()");
		super.onResume();
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.d(TAG, "onConnected()");
		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		Log.d(TAG, "lat: " + mLastLocation.getLatitude() + " long: "
				+ mLastLocation.getLongitude());

		final Place currentPlace = getClosestPlace();
		Fragment fragmentToShow = getTaskListFragment(currentPlace);
		showTaskListFragment(fragmentToShow);

		mNewTaskButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Task task = new Task();
				currentPlace.getTaskList().addTask(task);
				Intent i = new Intent(getActivity(), TaskActivity.class);
				i.putExtra(TaskFragment.EXTRA_TASK_ID, task.getId());
				startActivityForResult(i, 0);
			}
		});

		// mSaveButton.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Log.d(TAG, "CLICK SAVE NEW TASK");
		// }
		// });
		// mNewTaskText.setOnKeyListener(new OnKeyListener() {
		//
		// @Override
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// if (keyCode == KeyEvent.KEYCODE_ENTER
		// && event.getAction() == KeyEvent.ACTION_DOWN) {
		// Log.d(TAG, "ENTER CLICK");
		// Task t = new Task();
		// t.setTitle(mNewTaskText.getText().toString());
		// currentPlace.getTaskList().addTask(t);
		// PlaceStorage.get(getActivity()).savePlaces();
		// return false;
		// }
		// return false;
		// }
		// });
	}

	/*
	 * Check if any Place location is the same as the current GPS location and
	 * return it, otherwise return null
	 */
	private Place getClosestPlace() {
		if (mLastLocation != null) {
			double minVal = Double.MAX_VALUE;
			Place closestPlace = null;
			for (Place place : PlaceStorage.get(getActivity()).getPlaces()) {
				Log.d(TAG, "place: " + place.getTitle());
				if (minVal > getDistance(place)) {
					closestPlace = place;
					minVal = getDistance(place);
				}
			}
			if (minVal < 0.015) {
				return closestPlace;
			}
		}
		Log.d(TAG, "getClosestPlace() return null");
		return null;
	}

	/*
	 * Returns the Euclidean distance between the current position and a Place
	 */
	private double getDistance(Place place) {
		double diffLat = place.getLatitude() - mLastLocation.getLatitude();
		double diffLong = place.getLongitude() - mLastLocation.getLongitude();
		double distance = Math.sqrt(Math.pow(diffLat, 2)
				+ Math.pow(diffLong, 2));
		Log.d(TAG, "distance to " + place.getTitle() + ": " + distance);
		return distance;
	}

	/*
	 * Return the TaskListFragment corresponding to current GPS location or if
	 * there's no match, a default view
	 */
	private Fragment getTaskListFragment(Place place) {
		if (place != null) {
			Log.d(TAG, "fragmentToShow: " + place.getTitle());
			showPlaceHeader(place);
			return TaskListFragment.newInstance(place.getId());
		} else {
			Log.d(TAG, "No place found for this location");
			hidePlaceHeader();
			return new NoPlaceFragment();
		}
	}

	private void showPlaceHeader(Place place) {
		mPlaceTitleView.setText(place.getTitle());
		mLatitudeView.setText(String.valueOf(place.getLatitude()));
		mLongitudeView.setText(String.valueOf(place.getLongitude()));
		mHeaderLayout.setVisibility(LinearLayout.VISIBLE);
		mNewTaskLayout.setVisibility(LinearLayout.VISIBLE);
	}

	private void hidePlaceHeader() {
		mHeaderLayout.setVisibility(LinearLayout.GONE);
		mNewTaskLayout.setVisibility(LinearLayout.GONE);
	}

	private void showTaskListFragment(Fragment fragmentToShow) {
		Log.d(TAG, "showTaskListFragment(), " + fragmentToShow.toString());
		Fragment oldFragment = getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);
		if (oldFragment != null) {
			getActivity().getSupportFragmentManager().beginTransaction()
					.remove(oldFragment).commit();
		}
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragmentToShow).commit();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.d(TAG, "onConnectionFailed()");
		Toast.makeText(getActivity(),
				"Connection failed. Your location is unknown",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Log.d(TAG, "onConnectionSuspended()");
	}

}
