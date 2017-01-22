package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.itheima.mobilesafe74.R;

/**
 * Created by horizon on 1/8/2017.
 * 高级工具页面
 */
public class AToolActivity extends Activity {
    private TextView tv_query_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);

        //1. 归属地查询
        phone_location_query();
    }

    private void phone_location_query() {
        tv_query_location = (TextView) findViewById(R.id.tv_query_phoneNumber_location);
        tv_query_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AToolActivity.this,PhoneLocationQueryActivity.class));
            }
        });
    }

}
