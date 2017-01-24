package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.engine.AddressDao;

/**
 * Created by horizon on 1/8/2017.
 * 查询手机号码的归属地
 */
public class PhoneLocationQueryActivity extends Activity {
    private EditText et_phone;
    private Button bt_location_query;
    private TextView tv_location_display;
    private String phone_number = "";
    private String location = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //2.3 显示具体的归属地信息
            if (location != "") {
                tv_location_display.setText(location);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_query);

        //1. 初始化UI界面
        initUI();

    }

    private void initUI() {
        //1.1 输入需要查询的手机号
        et_phone = (EditText) findViewById(R.id.et_phone_query);
        bt_location_query = (Button) findViewById(R.id.bt_query_location);

        //1.2 显示归属地信息
        tv_location_display = (TextView) findViewById(R.id.tv_location_display);

        //2. 当点击查询按钮时执行查询方法
        bt_location_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryLocation();
            }
        });

        //3. 实时查询手机号码的归属地
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    queryLocation();
            }
        });
    }


    public void queryLocation() {
        //""
        //2.1 获取输入的手机号
        phone_number = et_phone.getText().toString();

        //2.2 获取具体的归属地信息
        if (!TextUtils.isEmpty(phone_number)) {
            //由于打开数据库属于耗时操作，所以需要在线程中开启
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    location = AddressDao.getAddress(phone_number);

                    //由于不能在线程中更改UI界面，所以需要在主线程中修改。
                    mHandler.sendEmptyMessage(0);
                }
            }.start();

        }else{
            //添加抖动效果
            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
            et_phone.startAnimation(shake);


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
