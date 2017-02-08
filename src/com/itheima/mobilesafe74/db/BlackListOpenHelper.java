package com.itheima.mobilesafe74.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by horizon on 2/7/2017.
 */

public class BlackListOpenHelper extends SQLiteOpenHelper {
    public BlackListOpenHelper(Context context) {
        super(context, "blacklist.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        db.execSQL("create table blacklist (_id Integer primary key autoincrement, phonenumber varchar(20) ,mode varchar(20));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
