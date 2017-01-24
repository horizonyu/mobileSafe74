package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;

public class SetupOverActivity extends Activity {
    private TextView tv_safe_number;
    private Button bt_setup_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (setup_over) {
            //密码输入成功,并且四个导航界面设置完成----->停留在设置完成功能列表界面
            setContentView(R.layout.activity_setup_over);

            initUI();
        } else {
            //密码输入成功,四个导航界面没有设置完成----->跳转到导航界面第1个
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);

            //开启了一个新的界面以后,关闭功能列表界面
            finish();
        }
    }

    private void initUI() {
        //获取设置的安全号码
        String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        tv_safe_number = (TextView) findViewById(R.id.tv_safe_number);
        tv_safe_number.setText(phone);

        //设置重置按钮的点击事件
        bt_setup_reset = (Button) findViewById(R.id.bt_setup_reset);
        bt_setup_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SetupOverActivity.this, Setup1Activity.class));
                finish();
            }
        });
    }
}
