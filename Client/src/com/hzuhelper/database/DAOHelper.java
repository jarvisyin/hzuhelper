package com.hzuhelper.database;

import java.util.List;

import com.hzuhelper.AppContext;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DAOHelper extends SQLiteOpenHelper {
	// 数据库的名称
	public static final String DB_NAME = "HzuHelper.db";
	// 数据库版本号
	public static final int DB_VERSION = 1;

	public static List<String> createTableCommanders;

	private static DAOHelper helper;


	public static DAOHelper getInstance() {
		if (helper == null) {
			helper = new DAOHelper(AppContext.getAppContext(), DB_NAME, DB_VERSION);
		} 
		return helper;
	}

	private DAOHelper(Context context, String DBname, int DBversoin) {
		super(context, DBname, null, DBversoin);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String cmdStr : createTableCommanders) {
			db.execSQL(cmdStr);
		}
		createTableCommanders = null;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
