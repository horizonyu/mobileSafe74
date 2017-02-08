package com.itheima.mobilesafe74.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobilesafe74.db.BlackListOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by horizon on 2/7/2017.
 *
 * 添加对数据库的增删改查操作
 */

public class BlackListDBDao {

    private BlackListOpenHelper blackListOpenHelper;
    private static BlackListDBDao blackListdbDao = null;
    private Context mContext;

    //使操作数据库对象成为单例模式，所以使用私有
    private BlackListDBDao(Context mContext) {
        blackListOpenHelper = new BlackListOpenHelper(mContext);
    }

    //获取当前类单例
    public static BlackListDBDao getInstance(Context context){
        if(blackListdbDao == null){
            blackListdbDao = new BlackListDBDao(context);
        }
        return blackListdbDao;
    }

    /**增加一个条目
     * @param phoneNumber 需要拦截的手机号码
     * @param mode  拦截的类型（1:来电 2:短信 3:所有）
     */
    //插入操作
    public int insert(String phoneNumber, String mode) {
        SQLiteDatabase db = blackListOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("phoneNumber",phoneNumber);
        contentValues.put("mode",mode);
        db.insert("blacklist",null,contentValues);

        db.close();
        return 1;
    }

    /**删除一个条目
     * @param phoneNumber  指定需要删除的手机号码对应的数据
     */
    //删除操作
    public void delete( String phoneNumber){
        SQLiteDatabase db = blackListOpenHelper.getWritableDatabase();

        db.delete("blacklist","phoneNumber = ?",new String[]{phoneNumber});

        db.close();
    }

    /**修改一条数据
     * @param phoneNumber 需要修改的手机号码
     * @param mode   需要需改的模式
     */
    //修改操作
    public void update(String phoneNumber,String mode){
        SQLiteDatabase db = blackListOpenHelper.getWritableDatabase();

        ContentValues modeValues = new ContentValues();
        modeValues.put("mode",mode);
        db.update("blacklist",modeValues,"phoneNumber = ?",new String[]{phoneNumber});

        db.close();
    }

    /**
     *从数据库中查询所有的数据
     */
    //查询操作
    public List<BlackListInfoDao> queryAll(){
        SQLiteDatabase db = blackListOpenHelper.getWritableDatabase();

        List<BlackListInfoDao> blackInfoList = new ArrayList<BlackListInfoDao>();

        Cursor cursor = db.query("blacklist", new String[]{"phoneNumber", "mode"}, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            BlackListInfoDao blackListInfoDao = new BlackListInfoDao();
            blackListInfoDao.phoneNumebr = cursor.getString(0);
            blackListInfoDao.mode = cursor.getString(1);

          /*  ContentValues blackInfoValues = new ContentValues();
            blackInfoValues.put("phoneNumber", BlackListInfoDao.phoneNumebr);
            blackInfoValues.put("mode", BlackListInfoDao.mode);*/

            blackInfoList.add(blackListInfoDao);

        }

        db.close();

        return blackInfoList;

    }

}
