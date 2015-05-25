package com.hzuhelper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hzuhelper.AppContext;

public class DAOHelper extends SQLiteOpenHelper{

    private static DAOHelper helper;

    public static DAOHelper getInstance(){
        if (helper==null) {
            synchronized (DAOHelper.class) {
                if (helper==null) {
                    helper = new DAOHelper(AppContext.getAppContext(),"HzuHelper.db",1);
                }
            }
        }
        return helper;
    }

    private DAOHelper(Context context,String DBname,int DBversoin){
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
