package com.bk.lrandom.realestate;

import java.util.ArrayList;

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
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.NYXDigital.NiceSupportMapFragment;
import com.bk.lrandom.realestate.adapters.GalleryAdapter;
import com.bk.lrandom.realestate.business.JSONFetchTask;
import com.bk.lrandom.realestate.business.Ultils;
import com.bk.lrandom.realestate.business.UserSessionManager;
import com.bk.lrandom.realestate.confs.constants;
import com.bk.lrandom.realestate.fragments.DetailContentFragment;
import com.bk.lrandom.realestate.models.Properties;
import com.bk.lrandom.realestate.models.User;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

@SuppressLint({ "NewApi", "HandlerLeak" })
public class DetailActivity extends ActionBarParentActivity {
	TextView type, purpose, county, cities, address, bathroom, bedroom, title,
			price, area;
	ProgressDialog dialog;
	public static final String IMAGES_RESPONSE = "images_feed";
	int properties_id;
	String user_id, email, phone_text;
	ImageButton btnEmail, btnSMS, btnCall, btnProfile;
	EditText message;
	User logedUser;
	GridView gallery, amenities;
	int columnWidth;
	ArrayList<String> paths;
	ArrayList<String> amenitiesText;
	private GoogleMap gmap;
	Marker gpin = null;

	public static final String TAG = "DetailActivity";

	private void initGallery() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				constants.GRID_PADDING, r.getDisplayMetrics());

		columnWidth = (int) ((Ultils.getScreenWidth(DetailActivity.this) - ((constants.NUM_OF_COLUMNS + 1) * padding)) / constants.NUM_OF_COLUMNS);

		gallery.setNumColumns(constants.NUM_OF_COLUMNS);
		gallery.setColumnWidth(columnWidth);
		gallery.setStretchMode(GridView.NO_STRETCH);
		gallery.setHorizontalSpacing((int) padding);
		gallery.setVerticalSpacing((int) padding);
	}

	Handler handlerImages = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey(IMAGES_RESPONSE)) {
				String jsonString = bundle.getString(IMAGES_RESPONSE);
				new JSONFetchTask(getResources().getString(
						R.string.estate_json_url)
						+ "amenities?id=" + properties_id, handlerAmenities)
						.execute();
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						String path = getResources().getString(
								R.string.domain_url)
								+ jsonObj.getString("path");
						paths.add(path);
					}
					GalleryAdapter adapter = new GalleryAdapter(
							DetailActivity.this, paths, columnWidth);
					gallery.setAdapter(adapter);
				} catch (Exception e) {
				}
			}
		};
	};

	Handler handlerAmenities = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey(JSONFetchTask.KEY_RESPONSE)) {
				String jsonString = bundle
						.getString(JSONFetchTask.KEY_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					amenitiesText = new ArrayList<String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						String amenitiesJSON = jsonObj.getString("name");
						amenitiesText.add(amenitiesJSON);
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							DetailActivity.this,
							R.layout.amenities_grid_item_layout, amenitiesText);
					amenities.setAdapter(adapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_layout);
		gallery = (GridView) findViewById(R.id.gallery);
		amenities = (GridView) findViewById(R.id.amenities);
		initGallery();

		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(constants.COMMON_KEY)) {
			btnSMS = (ImageButton) findViewById(R.id.btnSmS);
			btnEmail = (ImageButton) findViewById(R.id.btnEmail);
			btnCall = (ImageButton) findViewById(R.id.btnPhone);
			btnProfile = (ImageButton) findViewById(R.id.btnProfile);

			title = (TextView) findViewById(R.id.title);
			type = (TextView) findViewById(R.id.type);
			county = (TextView) findViewById(R.id.county);
			cities = (TextView) findViewById(R.id.cities);
			purpose = (TextView) findViewById(R.id.purpose);
			address = (TextView) findViewById(R.id.address);
			bathroom = (TextView) findViewById(R.id.bathroom);
			paths = new ArrayList<String>();
			bedroom = (TextView) findViewById(R.id.bedroom);
			price = (TextView) findViewById(R.id.price);
			area = (TextView) findViewById(R.id.area);
			properties_id = getIntent().getExtras()
					.getInt(constants.COMMON_KEY);
			new JSONFetchTask(getResources()
					.getString(R.string.estate_json_url)
					+ "estates?estate_id="
					+ properties_id, handler).execute();
		}

		dialog = new ProgressDialog(this);
		dialog.setMessage(getResources().getString(R.string.loading));
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setTitle(getResources().getString(R.string.detail_label));
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.containsKey(JSONFetchTask.KEY_RESPONSE)) {
				dialog.dismiss();
				String jsonString = bundle
						.getString(JSONFetchTask.KEY_RESPONSE);
				try {
					JSONArray jsonArray = new JSONArray(jsonString);
					if (jsonArray.length() == 1) {
						JSONObject obj = jsonArray.getJSONObject(0);
						parseProperties(obj);
					}
				} catch (Exception e) {
					Log.e(TAG, "error parse properties");
					// TODO: handle exception
				}
			}
		};
	};

	public void parseProperties(JSONObject jsonObj) {
		new JSONFetchTask(getResources().getString(R.string.images_json_url)
				+ "images/estate_id/" + properties_id, handlerImages,
				IMAGES_RESPONSE).execute();
		try {
			user_id = jsonObj.getString("user_id");
			email = jsonObj.getString("email");
			phone_text = jsonObj.getString("phone");

			btnSMS.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!Ultils.isConnectingToInternet(DetailActivity.this)) {
						showMsg(getResources().getString(R.string.open_network));
						return;
					}

					UserSessionManager userSession = new UserSessionManager(
							DetailActivity.this);

					if (userSession.getUserSession() == null) {
						Intent intent = new Intent(DetailActivity.this,
								AuthenticationActivity.class);
						startActivity(intent);
					} else {
						logedUser = userSession.getUserSession();
						if (logedUser.getPhone() == null
								|| phone_text.equals("")) {
							showDialog(getResources().getString(
									R.string.this_user_not_share_phone_number));
						} else {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri
									.parse("sms:" + phone_text)));
						}
					}
				}

			});

			btnCall.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!Ultils.isConnectingToInternet(DetailActivity.this)) {
						showMsg(getResources().getString(R.string.open_network));
						return;
					}

					UserSessionManager userSession = new UserSessionManager(
							DetailActivity.this);

					if (userSession.getUserSession() == null) {
						Intent intent = new Intent(DetailActivity.this,
								AuthenticationActivity.class);
						startActivity(intent);
					} else {
						logedUser = userSession.getUserSession();
						if (logedUser.getPhone() == null
								|| phone_text.equals("")) {
							showDialog(getResources().getString(
									R.string.this_user_not_share_phone_number));
						} else {
							startActivity(new Intent(Intent.ACTION_CALL, Uri
									.parse("tel:" + phone_text)));
						}
					}
				}
			});

			btnEmail.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!Ultils.isConnectingToInternet(DetailActivity.this)) {
						showMsg(getResources().getString(R.string.open_network));
						return;
					}

					UserSessionManager userSession = new UserSessionManager(
							DetailActivity.this);

					if (userSession.getUserSession() == null) {
						Intent intent = new Intent(DetailActivity.this,
								AuthenticationActivity.class);
						startActivity(intent);
					} else {
						logedUser = userSession.getUserSession();
						if (logedUser.getId() == Integer.parseInt(user_id)) {
							showDialog(getResources().getString(
									R.string.enquiry_alert));
							return;
						}

						LayoutInflater inflater = LayoutInflater
								.from(DetailActivity.this);
						View promptsView = inflater.inflate(
								R.layout.enquiry_prompts_layout, null);

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								DetailActivity.this);

						alertDialogBuilder.setView(promptsView);

						message = (EditText) promptsView
								.findViewById(R.id.editTextDialogUserInput);
						alertDialogBuilder.setMessage(getResources().getString(
								R.string.send_enquiry));
						alertDialogBuilder
								.setCancelable(false)
								.setPositiveButton(
										getResources().getString(
												R.string.ok_label),
										new DialogInterface.OnClickListener() {
											public void onClick(

											DialogInterface dialog, int id) {
												if (message.getText()
														.toString()
														.equalsIgnoreCase("")) {
													showDialog(getResources()
															.getString(
																	R.string.type_message));
													return;
												}
												new SendEnquiry().execute();
											}
										})
								.setNegativeButton(
										getResources().getString(
												R.string.cancel_label),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
											}
										});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
						// show it
						alertDialog.show();
					}
				}
			});

			btnProfile.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(DetailActivity.this,
							ProfileActivity.class);
					intent.putExtra(constants.COMMON_KEY, user_id);
					startActivity(intent);
				}
			});

			String titleText = jsonObj.getString(Properties.TAG_TITLE);
			title.setText(titleText);
			String addressText = jsonObj.getString(Properties.TAG_ADDESS);
			address.setText(" " + addressText);
			String typeText = jsonObj.getString(Properties.TAG_TYPES_NAME);
			type.setText(" " + typeText);
			String coutyText = jsonObj.getString(Properties.TAG_COUNTY_NAME);
			county.setText(" " + coutyText);
			String citiesText = jsonObj.getString(Properties.TAG_CITIES);
			cities.setText(" " + citiesText);
			String bathroomText = jsonObj.getString(Properties.TAG_BATHROOM);
			bathroom.setText(" " + bathroomText);
			String bedroomText = jsonObj.getString(Properties.TAG_BEDROOM);
			bedroom.setText(" " + bedroomText);
			String areaText = jsonObj.getString(Properties.TAG_AREA);
			area.setText(" " + areaText);
			String priceText = jsonObj.getString(Properties.TAG_PRICE);
			String currency = jsonObj.getString(Properties.TAG_CURRENCY);
			priceText = currency + " " + priceText;
			int timeRate = jsonObj.getInt("time_rate");
			if (timeRate != -1) {
				Resources r = getResources();
				if (timeRate == constants.WEEK) {
					priceText += " / " + r.getString(R.string.week);
				}
				if (timeRate == constants.MONTH) {
					priceText += " / " + r.getString(R.string.month);
				}
				if (timeRate == constants.YEAR) {
					priceText += " / " + r.getString(R.string.year);
				}
			}
			price.setText(" " + priceText);
			int purposeInt = Integer.parseInt(jsonObj
					.getString(Properties.TAG_PURPOSE));
			String purposeText = "";
			if (purposeInt == constants.SALES_VALUE) {
				purposeText = getResources().getString(R.string.sales_label);
			}
			if (purposeInt == constants.SALES_AND_RENT_VALUE) {
				purposeText = getResources().getString(
						R.string.sales_and_rent_label);
			}
			if (purposeInt == constants.RENT_VALUE) {
				purposeText = getResources().getString(R.string.rent_label);
			}

			String lat = jsonObj.getString("lat");
			String lng = jsonObj.getString("lng");
			final String markerPath = jsonObj.getString("path");
			gmap = ((NiceSupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
					Double.parseDouble(lat), Double.parseDouble(lng)));
			gmap.moveCamera(center);
			gmap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
			final MarkerOptions markerOpt = new MarkerOptions()
					.position(new LatLng(Double.parseDouble(lat), Double
							.parseDouble(lng)));
			if (markerPath == null || markerPath.equalsIgnoreCase("")
					|| markerPath == "null" || markerPath == "") {
				markerOpt.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_pin));
				gpin = gmap.addMarker(markerOpt);
			} else {
				Ion.with(DetailActivity.this)
						.load(getResources().getString(R.string.domain_url)
								+ markerPath).asBitmap()
						.setCallback(new FutureCallback<Bitmap>() {

							@Override
							public void onCompleted(Exception arg0, Bitmap bmp) {
								// TODO Auto-generated method stub
								markerOpt.icon(BitmapDescriptorFactory
										.fromBitmap(bmp));
								gpin = gmap.addMarker(markerOpt);
							}

						});

			}

			purpose.setText(" " + purposeText);
			String content = jsonObj.getString(Properties.TAG_CONTENT);
			DetailContentFragment fragment = DetailContentFragment
					.newInstance();
			Bundle bundle = new Bundle();
			bundle.putString(DetailContentFragment.DETAIL_KEY, content);
			fragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, fragment).commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private class SendEnquiry extends AsyncTask<Void, Void, Boolean> {

		public SendEnquiry() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			dialog.dismiss();
		}

		protected void onPostExecute() {
			dialog.dismiss();
		};

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(DetailActivity.this);
			dialog.setMessage(DetailActivity.this.getResources().getString(
					R.string.please_wait_msg));
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String handleInserUrl = getResources().getString(
					R.string.users_json_url)
					+ "send_enquiry";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(handleInserUrl);
				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("email", new StringBody(email));
				reqEntity.addPart("message", new StringBody(message.getText()
						.toString()));
				reqEntity.addPart("reply_to",
						new StringBody(logedUser.getEmail()));
				reqEntity.addPart("user_name",
						new StringBody(logedUser.getUserName()));
				post.setEntity(reqEntity);
				HttpResponse response = client.execute(post);
				HttpEntity resEntity = response.getEntity();
				final String response_str = EntityUtils.toString(resEntity);
				if (resEntity != null) {
					Log.i("RESPONSE", response_str);
					runOnUiThread(new Runnable() {
						public void run() {
							try {
								dialog.dismiss();
								JSONObject jsonObj = new JSONObject(
										response_str);
								if (jsonObj.getString("ok").equals("0")) {
									showDialog(getResources().getString(
											R.string.spam_msg));
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			} catch (Exception ex) {
				Log.e("Debug", "error: " + ex.getMessage(), ex);
			}
			return null;
		}
	};
}
