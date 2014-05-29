package jp.itnav.r4r.ardrill;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.MediaController;

public class ResultObject {
	private Context mContext;
	private MySQLite sqlite;

	public ResultObject(Context context) {
		this.mContext = context;
		sqlite = new MySQLite(context);
	}

	public double getMeter() {
		double distance;
		distance = sqlite.DistanceSerch();
		BigDecimal bi = new BigDecimal(String.valueOf(distance));
		double meter = bi.setScale(2, BigDecimal.ROUND_UP).doubleValue();
		Log.v("メートル", String.valueOf(meter));
		return meter;
	}

	public double getFeet() {
		return getMeter() * 3.28;
	}

	public double getAverage() {
		return sqlite.getAverageMeter() * 2.23;
	}

	public String getPlayTime() {
			return stringForTime(Integer.parseInt(loadPreferences("playtime")));
		
	}

	public String getNormalTime() {
		return stringForTime(Integer.parseInt(loadPreferences("playtime")) / 2);
	}

	private String loadPreferences(String key) {
		// 読み込み
		String loadStr;
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		loadStr = sp.getString(key, "nodata");
		return loadStr;
	}

	StringBuilder mFormatBuilder = new StringBuilder();
	Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault()); // java.util.Formatterクラス

	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
					.toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

}
