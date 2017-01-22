package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.itheima.mobilesafe74.R;

/**
 * Created by horizon on 12/10/2016.
 */

public abstract class BaseActivity extends Activity {
    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

        //1. 创建手势管理对象，用来管理onTouchEvent()传来的手势动作
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {

            //用于监听手势的移动
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
                //界面由右向左滑动
                if ((e1.getRawX() - e2.getRawX()) > 0) {
                    //调用子类的下一页的方法
                    showNext();

                }

                //界面由左向右滑动
                if ((e1.getRawX() - e2.getRawX()) < 0) {
                    //调用子类的上一页的方法
                    showPro();

                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //手势监听器用于监听界面的手势移动
        gestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    //2. 抽象方法，用于显示下一页或是上一页,由于子类的具体实现不一样，所以定义为抽象方法
    protected abstract void showNext();

    protected abstract void showPro();

    //3. 调用按钮的点击方法，当点击按钮时，调用显示上一页或是下一页的方法
    public void prePage(View view) {
        //上一页调用按钮
        showPro();
    }

    public void nextPage(View view) {
        //下一页调用按钮
        showNext();
    }
}
