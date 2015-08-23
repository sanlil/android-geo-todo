package com.example.geotodo;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class PlaceActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		UUID placeId = (UUID) getIntent().getSerializableExtra(
				PlaceFragment.EXTRA_PLACE_ID);
		return PlaceFragment.newInstance(placeId);
	}

}