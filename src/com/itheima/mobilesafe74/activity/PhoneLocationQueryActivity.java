package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.engine.AddressDao;

/**
 * Created by horizon on 1/8/2017.
 * 查询手机号码的归属地
 */
public class PhoneLocationQueryActivity extends Activity {
    private EditText et_phone;
    private  Button bt_location_query;
    private TextView tv_location_display;
    private String phone_number = "";
    private String location = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_query);

        //1. 初始化UI界面
        initUI();

        //2. 当点击查询按钮时执行查询方法
        bt_location_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryLocation();
            }
        });


    }

    private void initUI() {
        //1.1 输入需要查询的手机号
        et_phone = (EditText) findViewById(R.id.et_phone_query);
        bt_location_query = (Button) findViewById(R.id.bt_query_location);

        //1.2 显示归属地信息
        tv_location_display = (TextView) findViewById(R.id.tv_location_display );


        //获取输入的手机号
        //    String phone_number = et_phone.getText().toString();

    }


    public  void queryLocation(){
        //""
        //2.1 获取输入的手机号
        phone_number = et_phone.getText().toString();

        //2.2 获取具体的归属地信息
        if(phone_number != ""){
            location = AddressDao.getAddress(phone_number);
        }

        //2.3 显示具体的归属地信息
        if(location != ""){
            tv_location_display.setText(location);
        }

        /*
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
*/

    }

}
