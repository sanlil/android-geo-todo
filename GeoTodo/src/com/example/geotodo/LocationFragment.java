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

	private static final String TAG = "LastKnownLocation";

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
		mGoogleApiClient.connect();

		return v;
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		Log.d(TAG, "onConnected(), latitude: " + mLastLocation.getLatitude()
				+ " longitude: " + mLastLocation.getLongitude());
		if (mLastLocation != null) {
			TestFragment2 firstFragment = new TestFragment2();
			getActivity().getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		}
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
