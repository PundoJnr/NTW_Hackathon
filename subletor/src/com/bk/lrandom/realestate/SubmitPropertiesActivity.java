package com.bk.lrandom.realestate;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.NYXDigital.NiceSupportMapFragment;
import com.bk.lrandom.realestate.adapters.MarkerAdapter;
import com.bk.lrandom.realestate.business.JSONFetchTask;
import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.business.UserSessionManager;
import com.bk.lrandom.realestate.fragments.SelectAmenitiesDialog;
import com.bk.lrandom.realestate.interfaces.SelectAmenitiesComunicator;
import com.bk.lrandom.realestate.models.Amenities;
import com.bk.lrandom.realestate.models.County;
import com.bk.lrandom.realestate.models.MyMarker;
import com.bk.lrandom.realestate.models.Types;
import com.bk.lrandom.realestate.models.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SubmitPropertiesActivity extends ActionBarParentActivity implements
		SelectAmenitiesComunicator {
	Cursor cursor;
	String[] categories_name, county_name, city_name, purpose_name,
			time_rate_name;
	int[] categories_id, county_id, city_id;
	String[] purpose_id, time_rate_id;
	String lat = "", lng = "";
	Spinner typeSpinner, countySpinner, purposeSpinner, citiesSpinner,
			markerSpinner, timeRateSpinner;
	String TAG = "UploadActivity";
	static final String KEY_TYPE_RESPONSE = "KEY_TYPE_RESPONSE",
			KEY_COUNTY_RESPONSE = "KEY_COUNTY_RESPONSE",
			KEY_CITIES_RESPONSE = "KEY_CITIES_RESPONSE",
			KEY_MARKER_RESPONSE = "KEY_MARKER_RESPONSE",
			KEY_AMENITIES_RESPONSE = "KEY_AMENITIES_RESPONSE";

	EditText price, title, content, address, bathroom, bedroom, area;
	int type_selected = 0, county_selected = 0, city_selected = 0,
			marker_selected = 0, purpose_selected = -1,
			time_rate_selected = -1;
	Button btnUpload, btnBrowserAmenities;
	ProgressDialog dialog;
	ArrayList<MyMarker> markers = new ArrayList<MyMarker>();
	ArrayList<Amenities> amenities_list = new ArrayList<Amenities>();
	ArrayList<Amenities> selected_amenities_list = new ArrayList<Amenities>();

	private GoogleMap gmap;
	Marker gpin = null;

	@SuppressLint("HandlerLeak")
	Handler handler_type = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			if (data.containsKey(KEY_TYPE_RESPONSE)) {
				String jsonString = data.getString(KEY_TYPE_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					categories_id = new int[jsonArray.length()];
					categories_name = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject categoriesJSON = jsonArray.getJSONObject(i);
						int id = categoriesJSON.getInt(Types.TAG_ID);
						String name = categoriesJSON.getString(Types.TAG_NAME);
						categories_id[i] = id;
						categories_name[i] = name;
					}

					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							SubmitPropertiesActivity.this,
							android.R.layout.simple_list_item_1,
							categories_name);
					typeSpinner.setAdapter(adapter);
					typeSpinner
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									type_selected = categories_id[arg2];
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}

							});

					new JSONFetchTask(getResources().getString(
							R.string.county_json_url)
							+ "county", handler_county, KEY_COUNTY_RESPONSE)
							.execute();

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
	};

	@SuppressLint("HandlerLeak")
	Handler handler_county = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			if (data.containsKey(KEY_COUNTY_RESPONSE)) {
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
							SubmitPropertiesActivity.this,
							android.R.layout.simple_list_item_1, county_name);
					countySpinner.setAdapter(adapter);
					countySpinner
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									county_selected = county_id[arg2];
									new JSONFetchTask(getResources().getString(
											R.string.cities_json_url)
											+ "cities_by_county_id/id/"
											+ county_selected, handler_cities,
											KEY_CITIES_RESPONSE).execute();
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

	@SuppressLint("HandlerLeak")
	Handler handler_cities = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			if (data.containsKey(KEY_CITIES_RESPONSE)) {
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
							SubmitPropertiesActivity.this,
							android.R.layout.simple_list_item_1, city_name);
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
					new JSONFetchTask(getResources().getString(
							R.string.marker_json_url)
							+ "marker", handler_marker, KEY_MARKER_RESPONSE)
							.execute();
				} catch (Exception e) {
					// TODO: handle exception
					city_name = new String[0];
					city_selected = 0;
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							SubmitPropertiesActivity.this,
							android.R.layout.simple_list_item_1, city_name);
					citiesSpinner.setAdapter(adapter);
					e.printStackTrace();
				}
			}
		};
	};

	Handler handler_marker = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			if (data.containsKey(KEY_MARKER_RESPONSE)) {
				String jsonString = data.getString(KEY_MARKER_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					city_id = new int[jsonArray.length()];
					city_name = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject markerJSON = jsonArray.getJSONObject(i);
						int id = markerJSON.getInt("id");
						String path = markerJSON.getString("path");
						MyMarker myMarker = new MyMarker();
						myMarker.setId(id);
						myMarker.setPath(path);
						markers.add(myMarker);
					}
					;

					MarkerAdapter adapter = new MarkerAdapter(
							SubmitPropertiesActivity.this,
							R.layout.marker_item_layout, markers);
					markerSpinner.setAdapter(adapter);
					markerSpinner
							.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									marker_selected = markers.get(arg2).getId();
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
							SubmitPropertiesActivity.this,
							android.R.layout.simple_list_item_1, city_name);
					citiesSpinner.setAdapter(adapter);
					e.printStackTrace();
				}
			}
		};
	};

	Handler handler_amenities = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			if (data.containsKey(KEY_AMENITIES_RESPONSE)) {
				String jsonString = data.getString(KEY_AMENITIES_RESPONSE);
				amenities_list.clear();

				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					city_id = new int[jsonArray.length()];
					city_name = new String[jsonArray.length()];
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject countyJSON = jsonArray.getJSONObject(i);
						int id = countyJSON.getInt("id");
						String name = countyJSON.getString("name");
						Amenities amenities = new Amenities();
						amenities.setId(id);
						amenities.setName(name);
						amenities_list.add(amenities);
					}
					;

					SelectAmenitiesDialog amenitiesDialog = new SelectAmenitiesDialog(
							amenities_list);
					amenitiesDialog.show(getSupportFragmentManager(), "dialog");

				} catch (Exception e) {
					// TODO: handle exception
					city_name = new String[0];
					city_selected = 0;
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							SubmitPropertiesActivity.this,
							android.R.layout.simple_list_item_1, city_name);
					citiesSpinner.setAdapter(adapter);
					e.printStackTrace();
				}
			}
		};
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_properties_activity_layout);

		typeSpinner = (Spinner) findViewById(R.id.type_spinner);
		countySpinner = (Spinner) findViewById(R.id.county_spinner);
		purposeSpinner = (Spinner) findViewById(R.id.purpose_spinner);
		citiesSpinner = (Spinner) findViewById(R.id.cities_spinner);
		markerSpinner = (Spinner) findViewById(R.id.marker_spinner);
		timeRateSpinner = (Spinner) findViewById(R.id.time_rate_spinner);

		btnBrowserAmenities = (Button) findViewById(R.id.select_amenities);

		purpose_id = getResources().getStringArray(R.array.purpose_id);
		purpose_name = getResources().getStringArray(R.array.purpose_name);

		time_rate_id = getResources().getStringArray(R.array.time_rate_id);
		time_rate_name = getResources().getStringArray(R.array.time_rate_name);

		btnBrowserAmenities.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new JSONFetchTask(getResources().getString(
						R.string.amenities_json_url)
						+ "amenities", handler_amenities,
						KEY_AMENITIES_RESPONSE).execute();
			}
		});
		ArrayAdapter<String> purposeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, purpose_name);
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

		ArrayAdapter<String> timeRateAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, time_rate_name);
		timeRateSpinner.setAdapter(timeRateAdapter);
		timeRateSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						time_rate_selected = Integer
								.valueOf(time_rate_id[arg2]);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});

		title = (EditText) findViewById(R.id.title);
		price = (EditText) findViewById(R.id.price);
		content = (EditText) findViewById(R.id.content);
		address = (EditText) findViewById(R.id.address);
		bathroom = (EditText) findViewById(R.id.bathroom);
		bedroom = (EditText) findViewById(R.id.bedroom);
		area = (EditText) findViewById(R.id.area);

		btnUpload = (Button) findViewById(R.id.btn_submit);
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "submit clicked");
				if (!Ultils
						.isConnectingToInternet(SubmitPropertiesActivity.this)) {
					showMsg(getResources().getString(R.string.open_network));
				} else {
					doUpload();
				}
			}
		});

		new JSONFetchTask(getResources().getString(R.string.type_json_url)
				+ "type", handler_type, KEY_TYPE_RESPONSE).execute();

		LinkedHashSet<Integer> disableItem = new LinkedHashSet<Integer>();
		disableItem.add(R.id.btn_action_upload);
		setDisableItem(disableItem);
		setTitle(R.string.upload_label);

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		try {
			gmap = ((NiceSupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

				@Override
				public void onMapClick(LatLng point) {
					// // TODO Auto-generated method stub
					if (gpin != null) {
						gpin.remove();
					}
					gpin = gmap.addMarker(new MarkerOptions().position(
							new LatLng(point.latitude, point.longitude)).icon(
							BitmapDescriptorFactory
									.fromResource(R.drawable.ic_pin)));
					lat = point.latitude + "";
					lng = point.longitude + "";
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public boolean validateBeforeUpload() {
		String titleText = title.getText().toString();
		String priceText = price.getText().toString();
		String addresText = address.getText().toString();
		String areaText = area.getText().toString();
		String contentText = content.getText().toString();

		if (lat.equalsIgnoreCase("") || lng.equalsIgnoreCase("")) {
			showDialog(getResources().getString(R.string.valid_location));
			return false;
		}

		if (titleText.equalsIgnoreCase("") || contentText.equalsIgnoreCase("")
				|| type_selected == 0 || county_selected == 0
				|| city_selected == 0 || priceText.equalsIgnoreCase("")
				|| addresText.equalsIgnoreCase("")
				|| areaText.equalsIgnoreCase("")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getResources().getString(R.string.vl_required))
					.setTitle(getResources().getString(R.string.alert))
					.setPositiveButton(
							getResources().getString(R.string.ok_label),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
			AlertDialog dialog = builder.create();
			dialog.show();
		} else {
			return true;
		}
		return false;
	}

	@SuppressLint("NewApi")
	private void doUpload() {
		boolean valid = validateBeforeUpload();
		if (valid) {
			UserSessionManager sessionManager = new UserSessionManager(this);
			final User user = sessionManager.getUserSession();
			if (user != null) {
				final ProgressDialog prgDialog = new ProgressDialog(
						SubmitPropertiesActivity.this);
				prgDialog.setMessage(getResources().getString(
						R.string.please_wait_msg));
				prgDialog.show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						String handleInserUrl = getResources().getString(
								R.string.estate_json_url)
								+ "estates";
						try {
							HttpClient client = new DefaultHttpClient();
							HttpPost post = new HttpPost(handleInserUrl);
							MultipartEntity reqEntity = new MultipartEntity();
							String titleText = title.getText().toString();
							String priceText = price.getText().toString();
							String contentText = content.getText().toString();
							String bathroomText = bathroom.getText().toString();
							String bedroomText = bedroom.getText().toString();
							String areaText = area.getText().toString();
							String addressText = address.getText().toString();

							reqEntity.addPart("address", new StringBody(
									addressText));
							reqEntity.addPart("user_id",
									new StringBody(user.getId() + ""));
							reqEntity.addPart("title",
									new StringBody(titleText));
							reqEntity.addPart("price",
									new StringBody(priceText));
							reqEntity.addPart("content", new StringBody(
									contentText));
							reqEntity.addPart("cities", new StringBody(
									city_selected + ""));
							reqEntity.addPart("types", new StringBody(
									type_selected + ""));
							reqEntity.addPart("county", new StringBody(
									county_selected + ""));
							reqEntity.addPart("purpose", new StringBody(
									purpose_selected + ""));
							reqEntity.addPart("area", new StringBody(areaText));
							reqEntity.addPart("bathrooms", new StringBody(
									bathroomText));
							reqEntity.addPart("bedrooms", new StringBody(
									bedroomText));
							reqEntity.addPart("time_rate", new StringBody(
									time_rate_selected + ""));
							reqEntity.addPart("marker", new StringBody(
									marker_selected + ""));
							reqEntity.addPart("lat", new StringBody(lat));
							reqEntity.addPart("lng", new StringBody(lng));
							for (int i = 0; i < selected_amenities_list.size(); i++) {
								reqEntity.addPart("amen[]", new StringBody(
										selected_amenities_list.get(i).getId()
												+ ""));
							}
							post.setEntity(reqEntity);
							HttpResponse response = client.execute(post);
							HttpEntity resEntity = response.getEntity();
							final String response_str = EntityUtils
									.toString(resEntity);
							if (resEntity != null) {
								Log.i("RESPONSE", response_str);
								runOnUiThread(new Runnable() {
									public void run() {
										try {
											prgDialog.dismiss();
											title.setText("");
											price.setText("");
											content.setText("");
											address.setText("");
											area.setText("");
											bathroom.setText("");
											bedroom.setText("");
											selected_amenities_list.clear();
											marker_selected = 0;
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});
							}
						} catch (Exception ex) {
							Log.e("Debug", "error: " + ex.getMessage(), ex);
						}
					}
				}).start();

			} else {
				Intent intent = new Intent(this, AuthenticationActivity.class);
				startActivity(intent);
			}
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (cursor != null) {
			cursor.close();
		}
	}

	@Override
	public void getAmenities(ArrayList<Amenities> amenities) {
		// TODO Auto-generated method stub
		selected_amenities_list = amenities;
	}
}
