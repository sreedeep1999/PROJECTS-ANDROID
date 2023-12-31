package com.example.smartscheduling.Utils;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartscheduling.LoginActivity;
import com.example.smartscheduling.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GPS_service extends Service {

	public static final String BROADCAST_ACTION = "Hello World";
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	public LocationManager locationManager;
	public MyLocationListener listener;
	public Location previousBestLocation = null;

	Intent intent;
	private String ip;
	private String uid;

	@Override
	public void onCreate() {
		super.onCreate();
		GlobalPreference globalPreference = new GlobalPreference(getApplicationContext());
		ip = globalPreference.RetriveIP();
		uid = globalPreference.RetriveUID();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		listener = new MyLocationListener();
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, (LocationListener) listener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}


	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}


	@Override
	public void onDestroy() {
		// handler.removeCallbacks(sendUpdatesToUI);
		super.onDestroy();
		Log.v("STOP_SERVICE", "DONE");
		locationManager.removeUpdates(listener);
	}

	public static Thread performOnBackgroundThread(final Runnable runnable) {
		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					runnable.run();
				} finally {

				}
			}
		};
		t.start();
		return t;
	}

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(final Location loc) {
			Log.i("*****", "Location changed");
			if (isBetterLocation(loc, previousBestLocation)) {
				loc.getLatitude();
				loc.getLongitude();
				updateLocation(loc.getLatitude(),loc.getLongitude());
				/*			intent.putExtra("Latitude", loc.getLatitude());
				intent.putExtra("Longitude", loc.getLongitude());
				intent.putExtra("Provider", loc.getProvider());
				sendBroadcast(intent);*/

			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
		}


		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
		}
	}

	private void updateLocation(final double latitude, final double longitude) {

		StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/SmartScheduling/API/updateUserLocation.php", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		}){
			@Override
			protected Map<String, String> getParams() {
				Map<String,String> params = new HashMap<>();
				params.put("uid",uid);
				params.put("latitude", String.valueOf(latitude));
				params.put("longitude", String.valueOf(longitude));
				return params;
			}
		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);

	}
}