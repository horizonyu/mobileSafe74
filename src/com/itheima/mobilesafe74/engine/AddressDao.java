package com.itheima.mobilesafe74.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
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
    public  static String getAddress(String phone){

        String location = null;
        if(phone != null){
            //2.1 对手机号码进行正则表达式的判断
                if(checkPhone(phone)){
                phone = phone.substring(0,7);
                //3. 打开数据库
                SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);

                //3.1 首先查询data1表
                Cursor data1 = db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null, null);

                if(data1.moveToNext()){
                    String outkey = data1.getString(0);
                    Log.i("outkey",outkey);

                    //3.2 接着通过外键查询data2
                    Cursor data2 = db.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null, null);

                    if(data2.moveToNext()){
                        location = data2.getString(0);
                        Log.i("location: ", location);
                    }
                }
                return location;
            }
            return "未知号码";
        }
        return "未知号码！！！";
    }

    public static boolean checkPhone(String phone) {
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phone)) return false;
        else return phone.matches(telRegex);
    }






}
