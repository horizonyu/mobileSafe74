package com.itheima.mobilesafe74.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe74.R;

public class SettingClickView extends RelativeLayout {

    private TextView tv_type_title;
    private TextView tv_type_des;

    public SettingClickView(Context context) {
        this(context, null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        //xml--->view	将设置界面的一个条目转换成view对象,直接添加到了当前SettingItemView对应的view中
        View.inflate(context, R.layout.setting_click_view, this);

        //等同于以下两行代码
        /*View view = View.inflate(context, R.layout.setting_item_view, null);
		this.addView(view);*/

        //自定义组合控件中的标题描述
        tv_type_title = (TextView) findViewById(R.id.tv_type_title);
        tv_type_des = (TextView) findViewById(R.id.tv_type_des);

    }

    /**
     * @param title 设置标题
     */
    public void setTitle(String title){
        tv_type_title.setText(title);
    }

    /**
     * @param des 设置描述
     */
    public void setDes(String des){
        tv_type_des.setText(des);
    }




}
