package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.Service.AddressService;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.ServiceUtil;
import com.itheima.mobilesafe74.utils.SpUtil;
import com.itheima.mobilesafe74.utils.ToastUtil;
import com.itheima.mobilesafe74.view.SettingItemView;

public class SettingActivity extends Activity {
    private SettingItemView siv_address;
    private CheckBox cb_press;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //自动更新
        initUpdate();

        //显示来电归属地信息
        initAddress();
    }

    private void initAddress() {
        //来电显示
        siv_address = (SettingItemView) findViewById(R.id.siv_address);

        //如果将控件的点击状态存放在sp中，因为在服务被终止时，会导致UI显示内容与存在sp中的状态不一致，
        //所以为了保持服务与存储的状态一致，这里使用一个工具类来判断服务是否开启来设置控件的状态,
        //如果服务开启表明控件已经被点击，反之亦成立
    //    boolean aBoolean = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_ADDRESS, false);
        boolean aBoolean = ServiceUtil.isRunning(this,"com.itheima.mobilesafe74.Service.AddressService");

        siv_address.setCheck(aBoolean);
        siv_address.setOnClickListener(new View.OnClickListener() {
            //获取控件的点击状态
              @Override
            public void onClick(View v) {

                  boolean checked = siv_address.isCheck();
                  siv_address.setCheck(!checked);

                if(!checked){
                    //如果开启，则开启此服务
                    startService(new Intent(getApplicationContext(),AddressService.class));
                    ToastUtil.show(getApplicationContext(),"服务开启成功");
                }
                else {
                    //关闭服务
                    stopService(new Intent(getApplicationContext(),AddressService.class));
                    ToastUtil.show(getApplicationContext(),"服务关闭成功");
                }

                //将控件的点击状态存入sp中
         //       SpUtil.putBoolean(getApplicationContext(),ConstantValue.OPEN_ADDRESS,!checked);
            }
        });
    }

    /**
     * 版本更新开关
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);

        //获取已有的开关状态,用作显示
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        //是否选中,根据上一次存储的结果去做决定
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前是选中的,点击过后,变成未选中
                //如果之前是未选中的,点击过后,变成选中

                //获取之前的选中状态
                boolean isCheck = siv_update.isCheck();
                //将原有状态取反,等同上诉的两部操作
                siv_update.setCheck(!isCheck);
                //将取反后的状态存储到相应sp中
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !isCheck);
            }
        });
    }

}
