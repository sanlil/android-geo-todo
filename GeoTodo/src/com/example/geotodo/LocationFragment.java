package com.example.geotodo;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

	private boolean isSameLocation(Place place) {
		boolean latIsSame = (place.getLatitude() == mLastLocation.getLatitude());
		boolean longIsSame = (place.getLongitude() == mLastLocation
				.getLongitude());
		Log.d(TAG, "is same location: " + (latIsSame && longIsSame));
		return (latIsSame && longIsSame);
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		Log.d(TAG, "lat: " + mLastLocation.getLatitude() + " long: "
				+ mLastLocation.getLongitude());

		// Check if any Place location is the same as the current GPS location
		Place currentPosition = null;
		if (mLastLocation != null) {
			for (Place place : PlaceList.get().getPlaces()) {
				Log.d(TAG, "place: " + place.getTitle());
				if (isSameLocation(place)) {
					currentPosition = place;
					// break;
				}
			}
		}

		// Show the TaskListFragment corresponding to current GPS location
		// or if there's no match, a default view
		Fragment fragmentToShow;
		if (currentPosition != null) {
			Log.d(TAG, "fragmentToShow: " + currentPosition.getTitle());
			fragmentToShow = TaskListFragment.newInstance(currentPosition
					.getId());
		} else {
			Log.d(TAG, "No fragment to show");
			fragmentToShow = new NoPlaceFragment();
		}
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
