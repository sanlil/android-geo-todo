package com.example.geotodo;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

public class LocationActivity extends FragmentActivity {

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
			mLatitudeText.setText(String.valueOf(currentLat));
			mLongitudeText.setText(String.valueOf(currentLong));
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		// setUpMapIfNeeded();

		mLatitudeText = (TextView) findViewById(R.id.latitudeTextView);
		mLongitudeText = (TextView) findViewById(R.id.longitudeTextView);

		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
		// 35000, 10, mLocationListener);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				35000, 10, this.mLocationListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// setUpMapIfNeeded();
	}

}
