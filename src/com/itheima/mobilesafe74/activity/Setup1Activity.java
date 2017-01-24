package com.itheima.mobilesafe74.activity;

import android.content.Intent;
import android.os.Bundle;

import com.itheima.mobilesafe74.R;

public class Setup1Activity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    @Override
    protected void showNext() {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();

        //开启平移动画
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
    }

    @Override
    protected void showPro() {
        //由于是第一个界面，没有上一页的操作
    }

}
