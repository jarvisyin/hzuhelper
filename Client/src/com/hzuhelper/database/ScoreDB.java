package com.hzuhelper.database;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hzuhelper.model.ScoreInfo;

public class ScoreDB {

	public static final String TABLE_NAME = "Score";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_XN = "xn";
	public static final String COLUMN_XQ = "xq";
	public static final String COLUMN_KCMC = "kcmc";
	public static final String COLUMN_KCLX = "kclx";
	public static final String COLUMN_KHFS = "khfs";
	public static final String COLUMN_PSCJ = "pscj";
	public static final String COLUMN_QZCJ = "qzcj";
	public static final String COLUMN_SYCJ = "sycj";
	public static final String COLUMN_QMCJ = "qmcj";
	public static final String COLUMN_ZPCJ = "zpcj";
	public static final String COLUMN_JD = "jd";
	public static final String COLUMN_XF = "xf";
	public static final String COLUMN_CXCJ = "cxcj";

	private ScoreDB() {}	

	public static List<ScoreInfo> getList() {
		SQLiteDatabase db = DAOHelper.getInstance().getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
				+ "  order by xn desc,xq desc", null);
		List<ScoreInfo> sList = new ArrayList<ScoreInfo>();
		while (cursor.moveToNext()) {
			ScoreInfo model = new ScoreInfo();
			model.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
			model.setXn(cursor.getString(cursor.getColumnIndex(COLUMN_XN)));
			model.setXq(cursor.getString(cursor.getColumnIndex(COLUMN_XQ)));
			model.setKcmc(cursor.getString(cursor.getColumnIndex(COLUMN_KCMC)));
			model.setKclx(cursor.getString(cursor.getColumnIndex(COLUMN_KCLX)));
			model.setKhfs(cursor.getString(cursor.getColumnIndex(COLUMN_KHFS)));
			model.setPscj(cursor.getString(cursor.getColumnIndex(COLUMN_PSCJ)));
			model.setQzcj(cursor.getString(cursor.getColumnIndex(COLUMN_QZCJ)));
			model.setSycj(cursor.getString(cursor.getColumnIndex(COLUMN_SYCJ)));
			model.setQmcj(cursor.getString(cursor.getColumnIndex(COLUMN_QMCJ)));
			model.setZpcj(cursor.getString(cursor.getColumnIndex(COLUMN_ZPCJ)));
			model.setJd(cursor.getString(cursor.getColumnIndex(COLUMN_JD)));
			model.setXf(cursor.getString(cursor.getColumnIndex(COLUMN_XF)));
			model.setCxcj(cursor.getString(cursor.getColumnIndex(COLUMN_CXCJ)));
			sList.add(model);
		}
		db.close();
		return sList;
	}

	public static void delete() {
		SQLiteDatabase db = DAOHelper.getInstance().getWritableDatabase();
		db.execSQL("delete from " + TABLE_NAME);
		db.close();
	}

	public static void save(ScoreInfo model) {
		SQLiteDatabase db = DAOHelper.getInstance().getReadableDatabase();
		db.execSQL(
				"insert into " + TABLE_NAME + " (" + COLUMN_XN + ","
						+ COLUMN_XQ + "," + COLUMN_KCMC + "," + COLUMN_KCLX
						+ "," + COLUMN_KHFS + "," + COLUMN_PSCJ + ","
						+ COLUMN_QZCJ + "," + COLUMN_SYCJ + "," + COLUMN_QMCJ
						+ "," + COLUMN_ZPCJ + "," + COLUMN_JD + "," + COLUMN_XF
						+ "," + COLUMN_CXCJ
						+ ") values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { model.getXn(), model.getXq(), model.getKcmc(),
						model.getKclx(), model.getKhfs(), model.getPscj(),
						model.getQzcj(), model.getSycj(), model.getQmcj(),
						model.getZpcj(), model.getJd(), model.getXf(),
						model.getCxcj() });
		db.close();
	}

	public static String createTableCommander() {
		return "create table " + TABLE_NAME + " (" + COLUMN_ID
				+ " integer primary key AUTOINCREMENT, " + COLUMN_XN
				+ " text, " + COLUMN_XQ + " text, " + COLUMN_KCMC + " text, "
				+ COLUMN_KCLX + " text, " + COLUMN_KHFS + " text, "
				+ COLUMN_PSCJ + " text, " + COLUMN_QZCJ + " text, "
				+ COLUMN_SYCJ + " text, " + COLUMN_QMCJ + " text, "
				+ COLUMN_ZPCJ + " text, " + COLUMN_JD + " text, " + COLUMN_XF
				+ " text, " + COLUMN_CXCJ + " text) ";
	}

	public static int count() {
		SQLiteDatabase db = DAOHelper.getInstance().getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME, null);
		int count = 0;
		if (cursor.moveToLast()) {
			count = cursor.getInt(0);
		}
		db.close();
		return count;
	}
}
