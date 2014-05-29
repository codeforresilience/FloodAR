package jp.itnav.r4r.ardrill;

import java.util.TimerTask;

import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InputFieldActivity extends Activity implements OnClickListener {
	private EditText editAge;
	private EditText editHeight;
	private RadioGroup radioGroup;
	private RadioButton radioButton;
	private String mInputAge;
	private String mInputHeight;
	private int mInputGender;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_field);

		editAge = (EditText) findViewById(R.id.ageEditText);

		editHeight = (EditText) findViewById(R.id.heightEditText);
		editHeight
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (event != null
								&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
							if (event.getAction() == KeyEvent.ACTION_UP) {
								// ソフトキーボードを隠す
								((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
										.hideSoftInputFromWindow(
												v.getWindowToken(), 0);

							}
							return true;
						}
						return false;
					}
				});

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radioButton = (RadioButton) findViewById(radioGroup
				.getCheckedRadioButtonId());
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						RadioButton radioButton = (RadioButton) findViewById(checkedId);
						if (radioButton.getText().toString().equals("Male")) {
							mInputGender = ARdrillJNIView.AVATAR_TYPE_MALE;
						} else if (radioButton.getText().toString()
								.equals("FeMale")) {
							mInputGender = ARdrillJNIView.AVATAR_TYPE_FEMALE;
						}

					}
				});

		button = (Button) findViewById(R.id.setGoalButton);
		button.setOnClickListener(this);

		RelativeLayout startButton = (RelativeLayout) findViewById(R.id.startButton);
		startButton.setOnClickListener(this);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != -1) {

			Intent intent = new Intent(this, ResultActivity.class);
			startActivity(intent);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startButton:
			editAge.selectAll();
			mInputAge = editAge.getText().toString();
			editHeight.selectAll();
			mInputHeight = editHeight.getText().toString();
			Intent intent = new Intent(this, ARdrillActivity.class);
			intent.putExtra("Gender", mInputGender);
			intent.putExtra("Age", mInputAge);
			intent.putExtra("Height", mInputHeight);

			try {
				if (mInputAge != null && mInputHeight != null) {
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(this);
					sp.edit().remove("playtime").commit();
					
					
					Log.v("goalPotisionLatitude", String.valueOf(Double
							.parseDouble(loadPreferences("goallatitude"))));
					Log.v("goalPotisionLongitude", String.valueOf(Double
							.parseDouble(loadPreferences("goallongitude"))));
					
					startActivityForResult(intent, 0);
				} else {
					Toast.makeText(this,
							"Error There are missing fields. Back",
							Toast.LENGTH_LONG).show();
				}
			} catch (NullPointerException e) {
				Toast.makeText(this, "Error There are missing fields. Back",
						Toast.LENGTH_LONG).show();
			} catch (NumberFormatException e) {
				Toast.makeText(this, "Error There are missing fields. Back",
						Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.setGoalButton:
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(this);
			sp.edit().remove("goallatitude").remove("goallongitude").commit();
			Intent intent2 = new Intent(this, MapActivity.class);
			intent2.putExtra("type", "destination");
			startActivityForResult(intent2, 0);
			break;
		}

	}

	private String loadPreferences(String key) {
		// 読み込み
		String loadStr;
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		loadStr = sp.getString(key, "nodata");
		return loadStr;
	}

}
