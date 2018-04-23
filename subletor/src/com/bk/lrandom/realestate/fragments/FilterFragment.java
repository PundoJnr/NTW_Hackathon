package com.bk.lrandom.realestate.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bk.lrandom.realestate.PropertiesActivity;
import com.bk.lrandom.realestate.R;
import com.bk.lrandom.realestate.business.JSONFetchTask;
import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.models.County;
import com.bk.lrandom.realestate.models.Types;

@SuppressLint("NewApi")
public class FilterFragment extends Fragment {
	Spinner typeSpinner, countySpinner, citiesSpinner, purposeSpinner;
	EditText title, minPrice, maxPrice, area, bedroom;
	static final String KEY_type_RESPONSE = "KEY_type_RESPONSE";
	static final String KEY_COUNTY_RESPONSE = "KEY_COUNTY_RESPONSE";
	static final String KEY_CITIES_RESPONSE = "KEY_CITIES_RESPONSE";
	String[] type_name, purpose_name, city_name, county_name;
	int[] type_id, county_id, city_id;
	String[] purpose_id;
	String TAG = "FilterFragment";
	int type_selected = 0;
	int county_selected = 0;
	int aim_selected = 0;
	int city_selected = 0;
	int purpose_selected = 0;
	Handler handler_type = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			Log.i(TAG, "Handler");
			if (data.containsKey(KEY_type_RESPONSE)) {
				Log.i(TAG, "Handler");
				String jsonString = data.getString(KEY_type_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					type_id = new int[jsonArray.length() + 1];
					type_name = new String[jsonArray.length() + 1];
					type_id[0] = 0;
					type_name[0] = getActivity().getResources().getString(
							R.string.all_label);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject typeJSON = jsonArray.getJSONObject(i);
						int id = typeJSON.getInt(Types.TAG_ID);
						String name = typeJSON.getString(Types.TAG_NAME);
						type_id[i + 1] = id;
						type_name[i + 1] = name;
					}

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getActivity(), android.R.layout.simple_list_item_1,
							type_name);
					typeSpinner.setAdapter(adapter);
					typeSpinner
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									type_selected = type_id[arg2];
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}

							});

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						new JSONFetchTask(getResources().getString(
								R.string.county_json_url)
								+ "county", handler_county, KEY_COUNTY_RESPONSE)
								.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					} else {
						new JSONFetchTask(getResources().getString(
								R.string.county_json_url)
								+ "county", handler_county, KEY_COUNTY_RESPONSE)
								.execute();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
	};

	Handler handler_county = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			Log.i(TAG, "Handler");
			if (data.containsKey(KEY_COUNTY_RESPONSE)) {
				Log.i(TAG, "Handler");
				String jsonString = data.getString(KEY_COUNTY_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					county_id = new int[jsonArray.length()];
					county_name = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject countyJSON = jsonArray.getJSONObject(i);
						int id = countyJSON.getInt(County.TAG_ID);
						String name = countyJSON.getString(County.TAG_NAME);
						county_id[i] = id;
						county_name[i] = name;
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getActivity(), android.R.layout.simple_list_item_1,
							county_name);
					countySpinner.setAdapter(adapter);
					countySpinner
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									county_selected = county_id[arg2];
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
										new JSONFetchTask(
												getResources()
														.getString(
																R.string.cities_json_url)
														+ "cities_by_county_id/id/"
														+ county_selected,
												handler_cities,
												KEY_CITIES_RESPONSE)
												.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
									} else {
										new JSONFetchTask(
												getResources()
														.getString(
																R.string.cities_json_url)
														+ "cities_by_county_id/id/"
														+ county_selected,
												handler_cities,
												KEY_CITIES_RESPONSE).execute();
									}
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}

							});

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
	};

	Handler handler_cities = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			Log.i(TAG, "Handler");
			if (data.containsKey(KEY_CITIES_RESPONSE)) {
				Log.i(TAG, "Handler");
				String jsonString = data.getString(KEY_CITIES_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					city_id = new int[jsonArray.length()];
					city_name = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject countyJSON = jsonArray.getJSONObject(i);
						int id = countyJSON.getInt(County.TAG_ID);
						String name = countyJSON.getString(County.TAG_NAME);
						city_id[i] = id;
						city_name[i] = name;
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getActivity(), android.R.layout.simple_list_item_1,
							city_name);
					citiesSpinner.setAdapter(adapter);
					citiesSpinner
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									city_selected = city_id[arg2];
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}

							});

				} catch (Exception e) {
					// TODO: handle exception
					city_name = new String[0];
					city_selected = 0;
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getActivity(), android.R.layout.simple_list_item_1,
							city_name);
					citiesSpinner.setAdapter(adapter);
					e.printStackTrace();
					e.printStackTrace();
				}
			}
		};
	};

	public static final FilterFragment newInstance() {
		// TODO Auto-generated constructor stub
		FilterFragment fragment = new FilterFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.filter_layout, null);
		typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
		countySpinner = (Spinner) view.findViewById(R.id.county_spinner);
		citiesSpinner = (Spinner) view.findViewById(R.id.cities_spinner);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new JSONFetchTask(getResources().getString(R.string.type_json_url)
					+ "type", handler_type, KEY_type_RESPONSE)
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			new JSONFetchTask(getResources().getString(R.string.type_json_url)
					+ "type", handler_type, KEY_type_RESPONSE).execute();
		}

		minPrice = (EditText) view.findViewById(R.id.minPrice);
		maxPrice = (EditText) view.findViewById(R.id.maxPrice);
		area = (EditText) view.findViewById(R.id.area);
		bedroom = (EditText) view.findViewById(R.id.bedroom);
		purposeSpinner = (Spinner) view.findViewById(R.id.purpose_spinner);

		purpose_id = getResources().getStringArray(R.array.purpose_id);
		purpose_name = getResources().getStringArray(R.array.purpose_name);
		ArrayAdapter<String> purposeAdapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_list_item_1,
				purpose_name);
		purposeSpinner.setAdapter(purposeAdapter);
		purposeSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						purpose_selected = Integer.valueOf(purpose_id[arg2]);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});

		Button btnFilter = (Button) view.findViewById(R.id.btn_filter);
		btnFilter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						PropertiesActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(constants.COUNTY_ID_KEY, county_selected);
				bundle.putInt(constants.TYPE_ID_KEY, type_selected);
				bundle.putInt(constants.CITY_ID_KEY, city_selected);
				bundle.putInt(constants.PURPOSE_KEY, purpose_selected);
				
		
				String minPriceText = minPrice.getText().toString();
				if (minPriceText != null && !minPriceText.equalsIgnoreCase("")) {
					bundle.putString(constants.MIN_PRICE_KEY, minPriceText);
				}

				String maxPriceText = maxPrice.getText().toString();
				if (maxPriceText != null && !maxPriceText.equalsIgnoreCase("")) {
					bundle.putString(constants.MAX_PRICE_KEY, maxPriceText);
				}

				String areaText = area.getText().toString();
				if (areaText != null && !areaText.equalsIgnoreCase("")) {
					bundle.putString(constants.AREA_KEY, areaText);
				}

				String bedroomText = bedroom.getText().toString();
				if (bedroomText != null && !bedroomText.equalsIgnoreCase("")) {
					bundle.putString(constants.BEDROOM_KEY, bedroomText);
				}

				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		return view;
	}
}
