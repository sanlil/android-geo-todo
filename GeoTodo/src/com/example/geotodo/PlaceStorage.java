package com.example.geotodo;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class PlaceStorage {

	private static final String TAG = "PlaceStorage";
	private static final String FILENAME = "places.json";

	private ArrayList<Place> mPlaces;
	private Context mAppContext;
	private DataJSONConverter mJSONConverter;
	private static PlaceStorage mPlaceStorage;

	private PlaceStorage(Context appContext) {
		mAppContext = appContext;
		mJSONConverter = new DataJSONConverter(mAppContext, FILENAME);

		try {
			mPlaces = mJSONConverter.loadPlaces();
		} catch (Exception e) {
			mPlaces = new ArrayList<Place>();
			Log.e(TAG, "Error loading crimes: ", e);
		}
	}

	public static PlaceStorage get(Context c) {
		if (mPlaceStorage == null) {
			mPlaceStorage = new PlaceStorage(c.getApplicationContext());
		}
		return mPlaceStorage;
	}

	public void addPlace(Place p) {
		mPlaces.add(p);
	}

	public ArrayList<Place> getPlaces() {
		return mPlaces;
	}

	public Place getPlace(UUID id) {
		for (Place p : mPlaces) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	public void deletePlace(Place p) {
		mPlaces.remove(p);
	}

	public boolean savePlaces() {
		Log.d(TAG, "savePlaces()");
		try {
			mJSONConverter.savePlaces(mPlaces);
			Log.d(TAG, "places saved to file");
			return true;
		} catch (Exception e) {
			Log.e(TAG, "Error when saving data: ", e);
			return false;
		}
	}

}
