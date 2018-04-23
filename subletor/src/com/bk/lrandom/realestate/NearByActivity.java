package com.bk.lrandom.realestate;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.NYXDigital.NiceSupportMapFragment;
import com.bk.lrandom.realestate.business.JSONFetchTask;
import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.models.Properties;
import com.bk.lrandom.realestate.services.GPSTrackerServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class NearByActivity extends ActionBarParentActivity {
	GPSTrackerServices gpsTracker;
	GoogleMap gmap;
	Marker gpin = null;
	MarkerOptions markerOpt;
	HashMap<Marker, Properties> properties_list = new HashMap<Marker, Properties>();
	ArrayList<Properties> tmp_properties_list = new ArrayList<Properties>();
	ArrayList<Marker> marker_list = new ArrayList<Marker>();
	Marker marker = null;
	TextView radiusView;
	Button filterButton;
	String radius;
	Circle circle = null; // in kms
	Properties properties;
	int index;
	ClusterManager<Properties> clusterManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_layout);
		radiusView = (TextView) findViewById(R.id.radius);
		radius = constants.DEFAULT_RADIUS + "";
		radiusView.setText(radius);
		filterButton = (Button) findViewById(R.id.filter);
		filterButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				radius = radiusView.getText().toString();
				// TODO Auto-generated method stub
				if (Integer.parseInt(radius) <= 0) {
					showDialog(getResources().getString(R.string.radius_valid));
				} else {
					String feed_api = getResources().getString(
							R.string.estate_json_url)
							+ "nearby?lat="
							+ gpsTracker.getLat()
							+ "&lng="
							+ gpsTracker.getLng() + "&radius=" + radius;
					new JSONFetchTask(feed_api, handler,
							JSONFetchTask.KEY_RESPONSE).execute();
				}
			}
		});

		gpsTracker = new GPSTrackerServices(this);
		if (gpsTracker.canGetLocation()) {
			try {
				gmap = ((NiceSupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.map)).getMap();
				gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
						gpsTracker.getLat(), gpsTracker.getLng()));
				gmap.moveCamera(center);
				gmap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
				gpin = gmap.addMarker(new MarkerOptions()
						.title("you are here")
						.position(
								new LatLng(gpsTracker.getLat(), gpsTracker
										.getLng()))
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_pin)));
				gpin.showInfoWindow();
				gmap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

					@Override
					public View getInfoWindow(Marker arg0) {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public View getInfoContents(Marker marker) {
						// TODO Auto-generated method stub
						View view = getLayoutInflater().inflate(
								R.layout.custom_marker, null);
						final Properties properties = properties_list
								.get(marker);
						if (properties != null) {
							TextView titleView = (TextView) view
									.findViewById(R.id.title);
							titleView.setText(properties.getTitle());
							TextView priceView = (TextView) view
									.findViewById(R.id.price);
							TextView addressView = (TextView) view
									.findViewById(R.id.address);
							addressView.setText(properties.getAddress());
							ImageView thumbView = (ImageView) view
									.findViewById(R.id.thumb);
							Ion.with(
									NearByActivity.this,
									getResources().getString(
											R.string.domain_url)
											+ properties.getImages_path())
									.withBitmap().resize(200, 200).centerCrop()
									.error(R.drawable.no_photo)
									.placeholder(R.drawable.no_photo)
									.intoImageView(thumbView);
							String priceText = properties.getCurrency() + " "
									+ properties.getPrice();
							int timeRate = properties.getTime_rate();
							if (timeRate != -1) {
								Resources r = getResources();
								if (timeRate == constants.WEEK) {
									priceText += " / "
											+ r.getString(R.string.week);
								}
								if (timeRate == constants.MONTH) {
									priceText += " / "
											+ r.getString(R.string.month);
								}
								if (timeRate == constants.YEAR) {
									priceText += " / "
											+ r.getString(R.string.year);
								}
							}
							priceView.setText(priceText);
							return view;
						}

						return null;
					}
				});

				gmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker arg0) {
						// TODO Auto-generated method stub
						if (properties_list.get(arg0) != null) {
							Intent intent = new Intent(NearByActivity.this,
									DetailActivity.class);
							intent.putExtra(constants.COMMON_KEY,
									properties_list.get(arg0).getId());
							startActivity(intent);
						}
					}
				});
				CircleOptions circleOptions = new CircleOptions()
						.center(new LatLng(gpsTracker.getLat(), gpsTracker
								.getLng()))
						.radius(Integer.parseInt(radius) * 1000)
						.strokeColor(0xFF0000FF).strokeWidth(1)
						.fillColor(0x110000FF); // In
												// meters
				circle = gmap.addCircle(circleOptions);
				gmap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

					@Override
					public void onMapLoaded() {
						// TODO Auto-generated method stub
						String feed_api = getResources().getString(
								R.string.estate_json_url)
								+ "nearby?lat="
								+ gpsTracker.getLat()
								+ "&lng="
								+ gpsTracker.getLng() + "&radius=" + radius;
						new JSONFetchTask(feed_api, handler,
								JSONFetchTask.KEY_RESPONSE).execute();
					}
				});
				clusterManager = new ClusterManager<Properties>(this, gmap);
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else {
			gpsTracker.showSettings();
		}
		setTitle(getResources().getString(R.string.nearby));
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey(JSONFetchTask.KEY_RESPONSE)) {
				CircleOptions circleOptions = new CircleOptions()
						.center(new LatLng(gpsTracker.getLat(), gpsTracker
								.getLng()))
						.radius(Integer.parseInt(radius) * 1000)
						.strokeColor(0xFF0000FF).strokeWidth(1)
						.fillColor(0x110000FF); // In meters
				if (circle != null) {
					circle.remove();
					circle = gmap.addCircle(circleOptions);
				}
				String jsonString = bundle
						.getString(JSONFetchTask.KEY_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						final int id = jsonObj.getInt(Properties.TAG_ID);
						final String title = jsonObj
								.getString(Properties.TAG_TITLE);
						final String price = jsonObj
								.getString(Properties.TAG_PRICE);
						final String address = jsonObj
								.getString(Properties.TAG_ADDESS);
						final int timeRate = jsonObj
								.getInt(Properties.TAG_TIME_RATE);
						final String currency = jsonObj
								.getString(Properties.TAG_CURRENCY);
						Double tmpLat = Double.parseDouble(jsonObj
								.getString("lat"));
						Double tmpLng = Double.parseDouble(jsonObj
								.getString("lng"));
						final String markerPath = jsonObj.getString("path");
						final String thumb = jsonObj
								.getString(Properties.TAG_IMAGES);

						properties = new Properties();
						properties.setId(id);
						properties.setAddress(address);
						properties.setTime_rate(timeRate);
						properties.setCurrency(currency);
						properties.setPrice(price);
						properties.setTitle(title);
						properties.setImages_path(thumb);
						properties.setMarker_path(markerPath);
						properties.setLat(tmpLat);
						properties.setLng(tmpLng);

						markerOpt = new MarkerOptions().position(new LatLng(
								tmpLat, tmpLng));
						marker = gmap.addMarker(markerOpt);
						properties_list.put(marker, properties);
						tmp_properties_list.add(properties);
						marker_list.add(marker);
						clusterManager.addItem(properties);
					}

					for (index = 0; index < properties_list.size(); index++) {
						String markerPath = tmp_properties_list.get(index)
								.getMarker_path();
						// marker = marker_list.get(index);
						if (markerPath == "null" || markerPath == "") {
							marker_list.get(index).setIcon(
									BitmapDescriptorFactory
											.fromResource(R.drawable.ic_pin));
						} else {
							Ion.with(NearByActivity.this)
									.load(getResources().getString(
											R.string.domain_url)
											+ markerPath).asBitmap()
									.setCallback(new FutureCallback<Bitmap>() {
										@Override
										public void onCompleted(Exception arg0,
												Bitmap bmp) {
											// TODO Auto-generated method stub
											marker_list.get(index).setIcon(
													BitmapDescriptorFactory
															.fromBitmap(bmp));
										}
									});
						}
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

	};

	public void onLowMemory() {
		System.gc();
	};
}
