package com.bk.lrandom.realestate.services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import com.bk.lrandom.realestate.R;

public class GPSTrackerServices extends Service implements LocationListener {
	Context context;

	Location location;
	double lat;
	double lng;
	boolean isNetworkEnabled = false;
	boolean isGPSEnabled = false;
	boolean canGetLocation = false;

	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	LocationManager locationManager;

	public GPSTrackerServices(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		getLocation();
	}

	public Location getLocation() {
		try {
			locationManager = (LocationManager) context
					.getSystemService(LOCATION_SERVICE);
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (!isGPSEnabled && !isNetworkEnabled) {

			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							lat = location.getLatitude();
							lng = location.getLongitude();
						}
					}
				}

				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								lng = location.getLongitude();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return location;
	}

	public double getLat() {
		if (location != null) {
			lat = location.getLatitude();
		}

		// return latitude
		return lat;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLng() {
		if (location != null) {
			lng = location.getLongitude();
		}

		// return longitude
		return lng;
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	public void showSettings() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getResources().getString(
				R.string.ask_to_turn_gps));
		builder.setTitle(context.getResources().getString(R.string.gps_setting));
		builder.setPositiveButton(
				context.getResources().getString(R.string.ok_label),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						context.startActivity(intent);
					}
				});

		builder.setNegativeButton(
				context.getResources().getString(R.string.cancel_label),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		Dialog dialog = builder.create();
		dialog.show();
	}

	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTrackerServices.this);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
