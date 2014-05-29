/** 2014 0313 Itnav Ishinomaki Kyohei Tsuda
 * 
 */
package jp.itnav.r4r.ardrill;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static int version = 1;
	private static String name = "ranking";
	private String mSql;

	public DatabaseHelper(Context context) {
		super(context, name, null, version);

	}

	// 新規作成時に呼ばれる
	@Override
	public void onCreate(SQLiteDatabase db) {
		mSql = "create table MyTable ("
				+"Secret integer primary key autoincrement"
				+ ",Latitude REAL"
				+ ",Longitude REAL" 
				+ ",Distance REAL " 
				+ ")";
		db.execSQL(mSql);
		Log.v("テーブル作成", "テーブルを作成しました。");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
