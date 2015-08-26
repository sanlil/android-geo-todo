package com.example.geotodo;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	private TextView mPlaceTitleView;
	private TextView mLatitudeView;
	private TextView mLongitudeView;
	private LinearLayout mHeaderLayout;

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
		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		Log.d(TAG, "lat: " + mLastLocation.getLatitude() + " long: "
				+ mLastLocation.getLongitude());

		Place currentPlace = getClosestPlace();
		Fragment fragmentToShow = getTaskListFragment(currentPlace);
		showTaskListFragment(fragmentToShow);

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
	}

	private void hidePlaceHeader() {
		mHeaderLayout.setVisibility(LinearLayout.GONE);
	}

	private void showTaskListFragment(Fragment fragmentToShow) {
		getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragmentToShow).commit();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.d(TAG, "onConnectionFailed()");
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Log.d(TAG, "onConnectionSuspended()");
	}

}
