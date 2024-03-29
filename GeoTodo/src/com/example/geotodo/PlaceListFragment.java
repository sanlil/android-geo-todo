package com.example.geotodo;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PlaceListFragment extends ListFragment {

	private static final String TAG = "PlaceListFragment";
	private ArrayList<Place> mPlaces;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mPlaces = PlaceStorage.get(getActivity()).getPlaces();
		PlaceAdapter adapter = new PlaceAdapter(mPlaces);
		setListAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		((PlaceAdapter) getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);

		ListView listView = (ListView) v.findViewById(android.R.id.list);
		registerForContextMenu(listView);

		return v;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Place place = ((PlaceAdapter) getListAdapter()).getItem(position);

		// Start TaskListActivity
		Log.d(TAG, place.getTitle() + "was clicked");
		Intent i = new Intent(getActivity(), TaskListActivity.class);
		i.putExtra(PlaceFragment.EXTRA_PLACE_ID, place.getId());
		startActivity(i);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.fragment_place_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_place:
			Place place = new Place();
			PlaceStorage.get(getActivity()).addPlace(place);
			Intent i = new Intent(getActivity(), PlaceActivity.class);
			i.putExtra(PlaceFragment.EXTRA_PLACE_ID, place.getId());
			startActivityForResult(i, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.place_list_item_context,
				menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		int position = info.position;
		PlaceAdapter adapter = (PlaceAdapter) getListAdapter();
		Place place = adapter.getItem(position);
		switch (item.getItemId()) {
		case R.id.menu_item_delete_place:
			// Delete item
			PlaceStorage.get(getActivity()).deletePlace(place);
			adapter.notifyDataSetChanged();
			return true;
		case R.id.menu_item_edit_place:
			// Go to edit page
			Intent i = new Intent(getActivity(), PlaceActivity.class);
			i.putExtra(PlaceFragment.EXTRA_PLACE_ID, place.getId());
			startActivityForResult(i, 0);
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private class PlaceAdapter extends ArrayAdapter<Place> {

		private int[] rowColors = new int[] { Color.parseColor("#eeeeee"),
				Color.parseColor("#d9e8ee") };

		public PlaceAdapter(ArrayList<Place> places) {
			super(getActivity(), 0, places);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Set layout and color on list items
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_place, null);
			}
			int colorPos = position % rowColors.length;
			convertView.setBackgroundColor(rowColors[colorPos]);

			// Set corresponding text on items
			Place place = getItem(position);
			TextView titleTextView = (TextView) convertView
					.findViewById(R.id.place_list_item_title);
			titleTextView.setText(place.getTitle());
			TextView longTextView = (TextView) convertView
					.findViewById(R.id.place_list_item_longitude);
			longTextView.setText(String.valueOf(place.getLongitude()));
			TextView latTextView = (TextView) convertView
					.findViewById(R.id.place_list_item_latitude);
			latTextView.setText(String.valueOf(place.getLatitude()));

			return convertView;
		}
	}

}
