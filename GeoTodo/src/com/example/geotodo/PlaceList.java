package com.example.geotodo;

import java.util.ArrayList;
import java.util.UUID;

public class PlaceList {

	private ArrayList<Place> mPlaces;
	private static PlaceList mPlaceStorage;

	private PlaceList() {
		mPlaces = new ArrayList<Place>();
	}

	public static PlaceList get() {
		if (mPlaceStorage == null) {
			mPlaceStorage = new PlaceList();
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
