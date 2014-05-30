/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.itnav.r4r.ardrill;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;

import java.io.File;

public class ARdrillActivity extends Activity implements SensorEventListener, LocationResult.LocationResultListener {

	private SensorManager mSensorManager;
	ARdrillJNIView mView;
	private LocationResult mLocationResult;
	RelativeLayout warningDialog;
	private float mAgeSpeed;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.ardrill);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		WakeLock lock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My tag");
		lock.acquire();

		Intent intent = getIntent();

		int avatarType = intent.getIntExtra("Gender",
				ARdrillJNIView.AVATAR_TYPE_MALE);
		mView = new ARdrillJNIView(getApplication(), true, 24, 0, avatarType);
		setAnimation(0);

		// ft. →　cm
		float height = intent.getFloatExtra("Height", 5.0f) * 30.48f;
		float sheight = 10 - (height / 50);
		mView.setWaterLevel(sheight * 10);

		int age = intent.getIntExtra("Age", 20);
		mAgeSpeed = AgeSpeed.getSpeed(age);
		mView.setAnimationSpeed(0.2f);

		mView.setZOrderMediaOverlay(true);
		addContentView(mView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		mLocationResult = new LocationResult(this, 1000);
		mLocationResult.setListener(this);
		mLocationResult.start();

	}

	private boolean FLAG = false;

	void showWarning() {
		Log.v("warnig", "OK津田");
		warningDialog = new RelativeLayout(this);
		warningDialog.setBackgroundColor(Color.RED);
		warningDialog.setAlpha(0.5f);
		addContentView(warningDialog, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		TextView warning = new TextView(this);
		warning.setGravity(Gravity.CENTER);
		warning.setTextColor(Color.WHITE);
		warning.setText("Pace down !");
		warning.setTextSize(70.0f);
		RelativeLayout.LayoutParams param = createParam(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		warningDialog.addView(warning, param);

	}

	private RelativeLayout.LayoutParams createParam(int w, int h) {
		return new RelativeLayout.LayoutParams(w, h);
	}

	private void deleteWarning() {
		Log.v("deletewarnig", "OK津田");
		warningDialog.setVisibility(View.INVISIBLE);
		FLAG = false;
	}

	void setupSensors() {
		mSensorManager = (SensorManager)getSystemService(
				Context.SENSOR_SERVICE);
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) != null) {
			Log.i("ARdrillActivity", "TYPE_GAME_ROTATION_VECTOR was found.");
			mSensorManager.registerListener(this,
					mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR),
					SensorManager.SENSOR_DELAY_FASTEST);
		} else if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null) {
			Log.i("ARdrillActivity", "TYPE_ROTATION_VECTOR was found.");
			mSensorManager.registerListener(this,
					mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
					SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onPause() {
		super.onPause();
        mSensorManager.unregisterListener(this);
		mView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mView.onResume();
        setupSensors();
	}

	@Override
	public void onRestart() {
		super.onRestart();

	}

	int count = 0;

	private float q[] = new float[4];
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// q is stored as [w, x, y, z]
		if (event.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
			SensorManager.getQuaternionFromVector(q, event.values);
			mView.setCameraAxis(q[2], q[1], q[3], q[0]);
		} else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			SensorManager.getQuaternionFromVector(q, event.values);
			mView.setCameraAxis(q[2], q[1], q[3], q[0]);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() {
		mLocationResult.stop();
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mLocationResult.stop();
			MyCountDownTimer mcd = new MyCountDownTimer(1500, 500);
			mcd.start();
			return true;
		}
		return false;
	}

	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		@Override
		public void onFinish() {
			finish();

		}

		@Override
		public void onTick(long millisUntilFinished) {
		}
	}

	@Override
	public void onReachedGoal() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mLocationResult.stop();
				MyCountDownTimer mcd = new MyCountDownTimer(1500, 500);
				mcd.start();
			}
		});
	}

	private void setAnimation(double distance) {
		Log.v("setAnimation distance",String.valueOf(distance));
		if (distance < 0.01) {
			mView.setAnimationNumber(ARdrillJNIView.ANIMATION_IDLE);
		} else if (distance < 1.0) {
			mView.setAnimationNumber(ARdrillJNIView.ANIMATION_WALK);
		} else if (distance < 1.5) {
			mView.setAnimationNumber(ARdrillJNIView.ANIMATION_RUN);
		}
	}
	
	@Override
	public void showTooFastWarning() {
		if (FLAG == false) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showWarning();
				}
			});
			FLAG = true;
		}
	}

	@Override
	public void deleteTooFastWarning() {
		if (FLAG == true) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					deleteWarning();
				}
			});
			FLAG = false;
		}
	}

	@Override
	public void onChangedSpeed(final double distance) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Log.v("onChangedSpeed distance",String.valueOf(distance));
				setAnimation(distance);
			}
		});
	}

}
