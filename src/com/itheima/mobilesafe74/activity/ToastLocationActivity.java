package com.itheima.mobilesafe74.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;
import com.itheima.mobilesafe74.utils.ToastUtil;

/**
 * Created by horizon on 2/2/2017.
 */
public class ToastLocationActivity extends Activity {

    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private WindowManager mWM;
    private int mHeight;
    private int mWidth;
    private ImageView iv_drag;
    private Button bt_top;
    private Button bt_bottom;
    private  long[] clicks = new long[2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_location);

        initToastUI();
    }

    /**
     * 设置toast的移动事件
     */
    private void initToastUI() {
        iv_drag = (ImageView) findViewById(R.id.iv_drag);
        bt_top = (Button) findViewById(R.id.bt_top);
        bt_bottom = (Button) findViewById(R.id.bt_bottom);

        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getApplicationContext(),"图片被点击");
            }
        });
        //通过屏幕管理器获取屏幕的宽高
        mWM = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        mHeight = mWM.getDefaultDisplay().getHeight();
        mWidth = mWM.getDefaultDisplay().getWidth();

        final int sLeft = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_LOCATION_X, 0);
        final int sTop = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_LOCATION_Y, 0);

        if(sTop > mHeight/2){
            bt_top.setVisibility(View.VISIBLE);
            bt_bottom.setVisibility(View.INVISIBLE);
        }else {
            bt_top.setVisibility(View.INVISIBLE);
            bt_bottom.setVisibility(View.VISIBLE);
        }

        //新建一个布局
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        //设置布局的边界
        layoutParams.leftMargin = sLeft;
        layoutParams.topMargin = sTop;
        iv_drag.setLayoutParams(layoutParams);


        //设置图片的双击居中显示功能
        iv_drag.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                System.arraycopy(clicks,1,clicks,0,1);
                clicks[clicks.length-1] = System.currentTimeMillis();
                if((clicks[1] - clicks[0]) < 500){

                    //控件处于屏幕的中间位置
                    int left = mWidth/2 - iv_drag.getWidth()/2;
                    int top = mHeight/2 - iv_drag.getHeight()/2;
                    int right = mWidth/2 + iv_drag.getWidth()/2;
                    int bottom = mHeight/2 + iv_drag.getHeight()/2;
                    //（left,top,right,bottom）-> (mWidth/2 + iv.drag.getWidth()/2,mHeight/2 + iv_drag.getHeight()/2,mWidth/2 + iv_drag.getWidth()/2,mHeight/2 + iv_drag.getHeight()/2)
                    iv_drag.layout(left,top,right,bottom);
                    SpUtil.putInt(getApplicationContext(),ConstantValue.TOAST_LOCATION_X,left);
                    SpUtil.putInt(getApplicationContext(),ConstantValue.TOAST_LOCATION_Y,top);
                    ToastUtil.show(getApplicationContext(),"双击");
                }
            }
        });

   //     iv_drag.layout(sLeft, sTop, sRight, sBottom);

        //设置图片的点击事件
        iv_drag.setOnTouchListener(new View.OnTouchListener() {

            private int bottom;
            private int right;
            private int top;
            private int left;

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //图片的点击事件（按下，移动，抬起）
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //鼠标按下获取控件为（x,y）坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //鼠标移动时，通过公式随时计算控件的位置
                        endX = (int) event.getRawX();
                        endY = (int) event.getRawY();

                        //获取控件已经移动过得位置
                        int moveX = endX - startX;
                        int moveY = endY - startY;

                        //计算控件现在所处的位置
                        left = iv_drag.getLeft() + moveX;
                        top = iv_drag.getTop() + moveY;
                        right = iv_drag.getRight() + moveX;
                        bottom = iv_drag.getBottom() + moveY;

                        //设置图片的显示边界不能超过屏幕的边界
                        if(left <= 0 || right >= mWidth || top <= 0 || bottom >= mHeight -46){
                            return true;
                        }

                        if(top > mHeight/2){
                            bt_bottom.setVisibility(View.INVISIBLE);
                            bt_top.setVisibility(View.VISIBLE);
                        }else {
                            bt_bottom.setVisibility(View.VISIBLE);
                            bt_top.setVisibility(View.INVISIBLE);
                        }

                        //随时设置显示控件的位置
                        iv_drag.layout(left, top, right, bottom);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_LOCATION_X, iv_drag.getLeft());
                        SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_LOCATION_Y, iv_drag.getTop());
                        break;
                }

                //false 表示不响应，true 表示响应
                //如果既要响应点击事件（onClickListener）又要响应触摸事件（OnTouchListener）,需要返回false
                //当dispatchTouchEvent()值为true时，响应点击事件 http://blog.csdn.net/yanzi1225627/article/details/22592831
                return false;



            }

        });




    }


}
