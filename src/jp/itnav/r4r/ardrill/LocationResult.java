package jp.itnav.r4r.ardrill;

import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.maps.model.LatLng;

import jp.itnav.r4r.ardrill.MyLocation.GetResult;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class LocationResult extends Timer {
	private Context mContext;
	private long mTime;
	private double mLatitude;
	private double mLongitude;
	private int mInterval;
	private Timer mTimer = this;
	private MyLocation mLocation;

	public LocationResult(Context context, int interval) {
		this.mContext = context;
		this.mInterval = interval;

	}

	private double onceLatitude = 0.0;
	private double onceLongitude = 0.0;
	private double mDistance = 0.0;
	private MySQLite sqlite;
	private int playtime = 0;
	private boolean GOAL = false;
	
	public void start() {
		sqlite = new MySQLite(mContext);
		sqlite.Delete(mContext);
		mLocation = new MyLocation(mContext);
		mLocation.start(mInterval);
		mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				playtime += mInterval;
				GetResult result = mLocation.getResult();
				mTime = result.time;
				mLatitude = result.latitude;
				mLongitude = result.longitude;

				if (mLatitude != 0.0 && mLongitude != 0.0) {
					if (onceLatitude != 0.0 && onceLongitude != 0.0) {
						if (mLatitude != onceLatitude
								|| mLongitude != onceLongitude) {
							sqlite.Insert(
									result.latitude,
									result.longitude,
									makeDistance(onceLatitude, onceLongitude,
											result.latitude, result.longitude,
											7) * 1000);
							mDistance = makeDistance(onceLatitude,
									onceLongitude, mLatitude, mLongitude, 7) * 1000;
							// Judge Goal
							if (goalJudge(result.latitude, result.longitude,
									goalPotision().latitude,
									goalPotision().longitude) == true) {
								GOAL = true;
								
							}
						}
					}
				}
				Log.v("oncelati", String.valueOf(onceLatitude));
				Log.v("oncelongi", String.valueOf(onceLongitude));
				Log.v("Latitude", String.valueOf(mLatitude));
				Log.v("Longitude", String.valueOf(mLongitude));
				Log.v("distance", String.valueOf(mDistance));

				onceLatitude = mLatitude;
				onceLongitude = mLongitude;

			}
		}, 0, mInterval);

	}
	
	public double getDistace(){
		return mDistance;
	}
	
	public boolean Goal(){
		return GOAL;
	}

	private boolean goalJudge(double lat1, double lng1, double lat2, double lng2) {
		double distance;
		distance = makeDistance(lat1, lng1, lat2, lng2, 7) * 1000;
		if (distance < 50) {
			return true;
		} else {
			return false;
		}
	}

	private LatLng goalPotision() {
		double latitude = Double.parseDouble(loadPreferences("goallatitude"));
		double longitude = Double.parseDouble(loadPreferences("goallongitude"));
		Log.v("goalPotisionLatitude", String.valueOf(latitude));
		Log.v("goalPotisionLongitude", String.valueOf(longitude));
		return new LatLng(latitude, longitude);
	}

	private double makeDistance(double lat1, double lng1, double lat2,
			double lng2, int precision) {
		// kmで返す
		int R = 6378;
		double lat = Math.toRadians(lat2 - lat1);
		double lng = Math.toRadians(lng2 - lng1);
		double A = Math.sin(lat / 2) * Math.sin(lat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lng / 2)
				* Math.sin(lng / 2);
		double C = 2 * Math.atan2(Math.sqrt(A), Math.sqrt(1 - A));
		double decimalNo = Math.pow(10, precision);
		double distance = R * C;
		distance = Math.round(decimalNo * distance / 1) / decimalNo;
		return distance;
	}

	public void stop() {
		Log.v("playtime", String.valueOf(playtime));
		savePreferences("playtime", String.valueOf(playtime));
		cancel();
		mLocation.stop();
	}

	private void savePreferences(String key, String id) {
		// 保存
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		sp.edit().putString(key, id).commit();
	}

	private String loadPreferences(String key) {
		// 読み込み
		String loadStr;
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		loadStr = sp.getString(key, "nodata");
		return loadStr;
	}

}
