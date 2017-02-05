package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.itheima.mobilesafe74.view.SettingClickView;
import com.itheima.mobilesafe74.view.SettingItemView;

public class SettingActivity extends Activity {
    private SettingItemView siv_address;
    private CheckBox cb_press;
    private SettingClickView scv_toast_type;
    private String[] mToast_styles;
    private int mToast_tyle_index;
    private SettingClickView scv_toast_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //自动更新
        initUpdate();

        //显示来电归属地信息
        initAddress();

        //初始化提示信息类型
        initAddressType();

        //初始化toast位置
        initToastLocation();
    }

    /**
     * 初始化toast位置
     */
    private void initToastLocation() {
        //获取自定义控件
        scv_toast_location = (SettingClickView) findViewById(R.id.scv_toast_location);
        scv_toast_location.setTitle("归属地提示框位置");
        scv_toast_location.setDes("设置归属地提示框位置");

        //设置控件的点击事件
        scv_toast_location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到一个半透明的activity
                startActivity(new Intent(getApplicationContext(),ToastLocationActivity.class));
            }
        });
    }

    /**
     * 设置toast显示位置
     */
    private void setToastLoacation() {
    }

    private void initAddressType() {
        //获取自定义显示类型控件
        scv_toast_type = (SettingClickView) findViewById(R.id.scv_toast_style);
        scv_toast_type.setTitle("设置归属地显示风格");

        //创建toast显示类型列表
        mToast_styles = new String[]{"透明","橙色","蓝色","灰色","绿色"};
        //默认为0，将此值存入到SP中
        mToast_tyle_index = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        scv_toast_type.setDes(mToast_styles[mToast_tyle_index]);

        //设置控件的点击事件

        scv_toast_type.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示对话框
                showDialog();

            }
        });

    }

    private void showDialog() {
        //显示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //设置图片，设置名称，设置单选框，设置按钮
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("请选择归属地样式");
        builder.setSingleChoiceItems(mToast_styles, mToast_tyle_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击事件中需要做的几件事：
                //获取sp中存储的样式，设置描述值，退出对话框
        //        int toast_tyle = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);

                SpUtil.putInt(getApplicationContext(),ConstantValue.TOAST_STYLE,which);
                dialog.dismiss();
                scv_toast_type.setDes(mToast_styles[which]);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();



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
