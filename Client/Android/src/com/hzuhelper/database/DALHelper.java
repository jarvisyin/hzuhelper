package com.hzuhelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hzuhelper.AppContext;
import com.hzuhelper.database.table.CourseDB;
import com.hzuhelper.database.table.ScoreDB;

public class DALHelper extends SQLiteOpenHelper{

    private static DALHelper helper;

    public static DALHelper getInstance(){
        if (helper==null) {
            synchronized (DALHelper.class) {
                if (helper==null) {
                    helper = new DALHelper(AppContext.getAppContext(),"HzuHelper.db",1);
                }
            }
        }
        return helper;
    }

    private DALHelper(Context context,String DBname,int DBversoin){
        super(context, DBname, null, DBversoin);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CourseDB.createTableCommander());
        db.execSQL(ScoreDB.createTableCommander());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
