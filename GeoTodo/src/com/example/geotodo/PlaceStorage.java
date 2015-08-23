package com.example.geotodo;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class PlaceStorage {

	private ArrayList<Place> mPlaces;
	private static PlaceStorage mPlaceStorage;
	private Context mAppContext;

	private PlaceStorage(Context appContext) {
		mAppContext = appContext;
		mPlaces = new ArrayList<Place>();
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

}
