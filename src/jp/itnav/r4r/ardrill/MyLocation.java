package jp.itnav.r4r.ardrill;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.content.Context;

public class MyLocation {

	private LocationClient mLocationClient;
	private GetResult mGetResult;
	private Context mContext;

	// private TextView _textResult;
	public MyLocation(Context context) {
		mContext = context;
		mGetResult = new GetResult();
	}

	private final LocationListener mLocationListener = new LocationListener() {
		@Override
		public void onLocationChanged(final Location location) {
			((Activity) mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mGetResult.time = location.getTime();
					mGetResult.latitude = location.getLatitude();
					mGetResult.longitude = location.getLongitude();
					mGetResult.accuracy = location.getAccuracy();
					mGetResult.speed = location.getSpeed();
					mGetResult.hasspeed = location.hasSpeed();
					mGetResult.altitude = location.getAltitude();
					Log.v("mGetResult.latitude",
							String.valueOf(mGetResult.latitude));
					Log.v("mGetResult.latitude",
							String.valueOf(mGetResult.longitude));
				}
			});
		}
	};

	class GetResult {
		long time;
		double latitude;
		double longitude;
		float speed;
		boolean hasspeed;
		float accuracy;
		double altitude;
	}

	public GetResult getResult() throws NullPointerException {
		return mGetResult;
	}

	public void start(final int interval) {
		Log.i("Start", "OK");
		mLocationClient = new LocationClient((Activity) mContext,
				new ConnectionCallbacks() {

					@Override
					public void onConnected(Bundle bundle) {
						Log.i("LocationConnected", "OK");
						
						
						LocationRequest request = LocationRequest
								.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
								.setFastestInterval(interval)
								.setInterval(interval);
						mLocationClient.requestLocationUpdates(request,
								mLocationListener);
						
					}

					@Override
					public void onDisconnected() {
						Log.i("LocationDisconnected", "OK");
						mLocationClient = null;
					}
				}, new OnConnectionFailedListener() {
					@Override
					public void onConnectionFailed(ConnectionResult result) {
						Log.i("ConnectionResult", String.valueOf(result));
					}
				});

		mLocationClient.connect();
	}

	public void stop() {
		if (mLocationClient == null || !mLocationClient.isConnected()) {
			return;
		}

		mLocationClient.removeLocationUpdates(mLocationListener);
		mLocationClient.disconnect();
	}

}
