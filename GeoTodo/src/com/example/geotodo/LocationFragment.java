package com.example.geotodo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LocationFragment extends Fragment {

	private Location mLastLocation;
	private TextView mLatitudeText;
	private TextView mLongitudeText;
	private LocationManager mLocationManager;

	private final LocationListener mLocationListener = new LocationListener() {

		@Override
		public void onLocationChanged(Location locFromGps) {
			// called when the listener is notified with a location update from
			// the GPS
			Log.d("log", "onLocationChanged()");
			double currentLat = locFromGps.getLatitude();
			double currentLong = locFromGps.getLongitude();
			Log.d("log", "latitude: " + currentLat + " longitude: "
					+ currentLong);
			TestFragment2 secondFragment = new TestFragment2();
			getActivity().getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, secondFragment).commit();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// called when the GPS provider is turned off (user turning off the
			// GPS on the phone)
			Log.d("log", "onProviderDisabled()");
		}

		@Override
		public void onProviderEnabled(String provider) {
			// called when the GPS provider is turned on (user turning on the
			// GPS on the phone)
			Log.d("log", "onProviderEnabled()");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// called when the status of the GPS provider changes
			Log.d("log", "onStatusChanged()");
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_location_container,
				container, false);

		mLatitudeText = (TextView) v.findViewById(R.id.latitudeTextView);
		mLongitudeText = (TextView) v.findViewById(R.id.longitudeTextView);

		mLocationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				35000, 10, this.mLocationListener);

		TestFragment1 firstFragment = new TestFragment1();
		getActivity().getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, firstFragment).commit();

		return v;
	}

}
