package com.example.geotodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NoPlaceFragment extends Fragment {

	Button addPlaceButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_no_place, container, false);
		addPlaceButton = (Button) v.findViewById(R.id.no_place_add_button);
		addPlaceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Place place = new Place();
				PlaceList.get().addPlace(place);
				Intent i = new Intent(getActivity(), PlaceActivity.class);
				i.putExtra(PlaceFragment.EXTRA_PLACE_ID, place.getId());
				startActivityForResult(i, 0);
			}
		});

		return v;
	}
}
