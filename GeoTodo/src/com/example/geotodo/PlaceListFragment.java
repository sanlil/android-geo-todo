package com.example.geotodo;

import java.util.ArrayList;

import android.content.Intent;
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
		mPlaces = PlaceList.get().getPlaces();
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
		Log.d(TAG, place.getTitle() + "was clicked");
		// Intent i = new Intent(getActivity(), PlaceActivity.class);
		// i.putExtra(PlaceFragment.EXTRA_PLACE_ID, place.getId());
		Intent i = new Intent(getActivity(), TaskListActivity.class);
		i.putExtra(PlaceFragment.EXTRA_PLACE_ID, place.getId());
		startActivity(i);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		// TODO: make specified menu for place list
		inflater.inflate(R.menu.fragment_task_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_task:
			Place place = new Place();
			PlaceList.get().addPlace(place);
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
		getActivity().getMenuInflater().inflate(R.menu.task_list_item_context,
				menu); // TODO: Change to place specific context menu
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		int position = info.position;
		PlaceAdapter adapter = (PlaceAdapter) getListAdapter();
		Place place = adapter.getItem(position);
		switch (item.getItemId()) {
		case R.id.menu_item_delete_task:
			PlaceList.get().deletePlace(place);
			adapter.notifyDataSetChanged();
			return true;
		case R.id.menu_item_edit_task:
			// Go to edit page
		}
		return super.onContextItemSelected(item);
	}

	private class PlaceAdapter extends ArrayAdapter<Place> {

		public PlaceAdapter(ArrayList<Place> places) {
			super(getActivity(), 0, places);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.list_item_place, null);
			}

			Place place = getItem(position);

			TextView titleTextView = (TextView) convertView
					.findViewById(R.id.place_list_item_titleTextView);
			titleTextView.setText(place.getTitle());
			// TextView longTextView = (TextView) convertView
			// .findViewById(R.id.place_list_item_longitudeTextView);
			// longTextView.setText(String.valueOf(place.getLongitude()));
			// TextView latTextView = (TextView) convertView
			// .findViewById(R.id.place_list_item_latitudeTextView);
			// latTextView.setText(String.valueOf(place.getLatitude()));

			return convertView;
		}
	}

}
