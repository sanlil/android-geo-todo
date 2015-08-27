package com.example.geotodo;

import java.util.UUID;

import android.annotation.TargetApi;
import android.location.Location;
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
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class PlaceFragment extends Fragment implements ConnectionCallbacks,
		OnConnectionFailedListener {
	static final String EXTRA_PLACE_ID = "sandrasplayground.summercourse.criminalintent.place_id";
	private static final String TAG = "PlaceFragment";

	private Place mPlace;
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	private EditText mTitleField;
	private EditText mLatitudeField;
	private EditText mLongitudeField;
	private Button mSaveButton;
	private Button mCancelButton;

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
		mPlace = PlaceStorage.get(getActivity()).getPlace(placeId);
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_place, parent, false);
		buildGoogleApiClient();
		mGoogleApiClient.connect();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		mLatitudeField = (EditText) v.findViewById(R.id.latitude_textfield);
		mLatitudeField.setText(String.valueOf(mPlace.getLatitude()));

		mLongitudeField = (EditText) v.findViewById(R.id.longitude_textfield);
		mLongitudeField.setText(String.valueOf(mPlace.getLongitude()));

		mTitleField = (EditText) v.findViewById(R.id.place_title_textfield);
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

		mSaveButton = (Button) v.findViewById(R.id.save_place_button);
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "save place");
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
			}
		});

		mCancelButton = (Button) v.findViewById(R.id.cancel_save_place_button);
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "cancel saving place");
				PlaceStorage.get(getActivity()).deletePlace(mPlace);
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

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		String latitudeStr = String.valueOf(mLastLocation.getLatitude());
		String longitudeStr = String.valueOf(mLastLocation.getLongitude());
		Log.d(TAG, "onConnected()");
		Log.d(TAG, "lat: " + latitudeStr + " long: " + longitudeStr);
		if (mLastLocation != null) {
			if (mPlace.getLatitude() == 0) {
				mLatitudeField.setText(latitudeStr);
				mPlace.setLatitude(mLastLocation.getLatitude());
			}
			if (mPlace.getLongitude() == 0) {
				mLongitudeField.setText(longitudeStr);
				mPlace.setLongitude(mLastLocation.getLongitude());
			}
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
