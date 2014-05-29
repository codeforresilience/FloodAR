package jp.itnav.r4r.ardrill;

import java.math.BigDecimal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultActivity extends Activity implements OnClickListener {

	private RelativeLayout ShowMapButton;
	private ResultObject mResultObject;
	private TextView mDistanceText;
	private TextView mAverageText;
	private TextView mPlayTimeText;
	private TextView mNormalTimeText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		mResultObject = new ResultObject(this);

		ShowMapButton = (RelativeLayout) findViewById(R.id.ShowMapButton);
		ShowMapButton.setOnClickListener(this);

		setdistance();

		setAverage();

		setPlayTime();

		setNormalTime();
	}

	private void setdistance() {
		BigDecimal bi = new BigDecimal(String.valueOf(mResultObject.getFeet()));
		mDistanceText = (TextView) findViewById(R.id.distanceText);
		mDistanceText.setText(String.valueOf(bi.setScale(1,
				BigDecimal.ROUND_DOWN).doubleValue())
				+ "ft.");
	}

	private void setAverage() {
		try {
			BigDecimal bi = new BigDecimal(String.valueOf(mResultObject
					.getAverage()));
			mAverageText = (TextView) findViewById(R.id.averageText);
			mAverageText.setText("0.0mph");
			mAverageText.setText(String.valueOf(bi.setScale(2,
					BigDecimal.ROUND_DOWN).doubleValue())
					+ "mph");
		} catch (NumberFormatException e) {

		}
	}

	private void setPlayTime() {
		mPlayTimeText = (TextView) findViewById(R.id.timeText);
		mPlayTimeText.setText(mResultObject.getPlayTime());
	}

	private void setNormalTime() {
		mNormalTimeText = (TextView) findViewById(R.id.speedText);
		mNormalTimeText.setText(mResultObject.getNormalTime());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ShowMapButton:
			Intent intent = new Intent(this, MapActivity.class);
			intent.putExtra("type", "result");
			startActivity(intent);
		}

	}

}
