package com.example.geotodo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;
import android.util.Log;

public class DataJSONConverter {

	static final String TAG = "DataJSONConverter";

	private Context mContext;
	private String mFilename;

	public DataJSONConverter(Context c, String f) {
		mContext = c;
		mFilename = f;
	}

	public void savePlaces(ArrayList<Place> places) throws JSONException,
			IOException {
		JSONArray array = new JSONArray();
		for (Place p : places) {
			array.put(p.toJSON());
		}
		Log.d(TAG, array.toString());

		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename,
					Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public ArrayList<Place> loadPlaces() throws IOException, JSONException {
		ArrayList<Place> places = new ArrayList<Place>();
		BufferedReader reader = null;
		try {
			// Open and read the file into a StringBuilder
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			// Parse the JSON using JSONTokener
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
					.nextValue();
			// Build the array of crimes from JSONObjects
			for (int i = 0; i < array.length(); i++) {
				places.add(new Place(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {
			// Ignore this one; it happens when starting fresh
		} finally {
			if (reader != null)
				reader.close();
		}
		return places;
	}

}
