package com.itheima.mobilesafe74.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 工具类，用于进行手机号归属地的查询
 * Created by horizon on 1/23/2017.
 */
//TODO 成功打开数据库并进行data1表的数据查询，接下来需要做的是通过data1表中的外键outkey从data2中找到对应的手机号的归属地。

public  class AddressDao {
    //1. 设置数据库的路径
    static String path = "data/data/com.itheima.mobilesafe74/files/address.db";

    //2. 进行查询
    public  static void getAddress(String phone){
        phone = phone.substring(0,7);

         //3. 打开数据库
          SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
          Cursor cursor = db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);

        //3.1 如果存在数据则停止查询
        if(cursor.moveToNext()){
            String outkey = cursor.getString(0);

            Log.i("outkey",outkey);
        }
    }
    //3. 如果数据库中存在数据，则进行查询







}
