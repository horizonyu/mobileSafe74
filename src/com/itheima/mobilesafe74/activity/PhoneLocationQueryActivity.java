package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.utils.ToastUtil;

/**
 * Created by horizon on 1/8/2017.
 * 查询手机号码的归属地
 */
public class PhoneLocationQueryActivity extends Activity {
    private EditText et_phone;
    private  Button bt_location_query;
    private TextView tv_location_display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_query);

        //初始化UI界面
        query_location();

    }

    private void query_location() {
        //输入需要查询的手机号
        et_phone = (EditText) findViewById(R.id.et_phone_query);

        bt_location_query = (Button) findViewById(R.id.bt_query_location);

        //显示归属地信息
        tv_location_display = (TextView) findViewById(R.id.tv_query_phoneNumber_location );

        //获取输入的手机号
        //    String phone_number = et_phone.getText().toString();

        //执行查询功能
        queryLocation("13000001234");


    }
    public  void queryLocation(final String phone_number){
        final String number = phone_number.substring(0,7);

        //1. 设置数据库的路径
        String path = "data/data/com.itheima.mobilesafe74/files/address.db";

        //2. 开启数据库连接查询
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);

        //3. 数据库查询
        Cursor query = db.query("data1", new String[]{"outkey"}, "id = ?",new String[]{number}, null, null, null);

        //4. 查到即可
        if(query.moveToNext()){
            String outkey = query.getString(0);

            ToastUtil.show(PhoneLocationQueryActivity.this,outkey);

            tv_location_display.setText(outkey);
        }

    }

}
