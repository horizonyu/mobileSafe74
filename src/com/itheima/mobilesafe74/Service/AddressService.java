package com.itheima.mobilesafe74.Service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.view.Gravity;
import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.engine.AddressDao;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;
import com.itheima.mobilesafe74.utils.ToastUtil;
import com.itheima.mobilesafe74.view.SettingItemView;

//import com.itheima.mobilesafe74.R;

//import com.Android.settings.R;
/**
 * 来电显示的服务
 * Created by horizon on 12/12/2016.
 */

public class AddressService extends Service {
    private SettingItemView siv_address;
    private TelephonyManager tm;
    private MyPhoneStateListener mp;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private View mToast = null;
    private WindowManager mWM;
    private String mAddress;
    private TextView tv_toast;
    //消息机制
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_toast.setText(mAddress);

        }
    };
    private int[] mDrawableId;
    private int mWidth;
    private int mHeight;


    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mp = new MyPhoneStateListener();
        tm.listen(mp,PhoneStateListener.LISTEN_CALL_STATE);

        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);

        //获取屏幕的宽高
        mWidth = mWM.getDefaultDisplay().getWidth();
        mHeight = mWM.getDefaultDisplay().getHeight();

    }

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    //空闲
                    ToastUtil.show(getApplicationContext(),"空闲..............");

                    //挂断电话后，去除toast view
                    if(mToast != null && mWM != null)
                    mWM.removeView(mToast);

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃状态
                //    Log.i("AddressServoce",);
                    showToast(incomingNumber);
                    ToastUtil.show(getApplicationContext(),"响铃...............");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //接通电话
                    //去掉toast view
                    mWM.removeView(mToast);
                //    Log.i("AddressService","接通...............");
                    ToastUtil.show(getApplicationContext(),"接通...............");

            }
        }
    }

    /**
     * 显示来电归属地
     * @param incomingNumber 来电号码
     *
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showToast(String incomingNumber) {
     //   Toast.makeText(getApplicationContext(),"响铃",Toast.LENGTH_LONG);
        //由于需要自定义Toast的显示，通过查看makeText()方法的缘，源码，进行自定义的更改
        // XXX This should be changed to use a Dialog, with a Theme.Toast
        // defined that sets up the layout params appropriately.
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
          //      | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE   默认可以触摸
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

        params.format = PixelFormat.TRANSLUCENT;
    //    params.windowAnimations = com.android.internal.R.style.Animation_Toast;

        //toast的显示等级,和来电等级一致
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.gravity = Gravity.TOP + Gravity.LEFT;
        //设置自定义toast的的位置
        params.x = SpUtil.getInt(getApplicationContext(),ConstantValue.TOAST_LOCATION_X,0);
        params.y = SpUtil.getInt(getApplicationContext(),ConstantValue.TOAST_LOCATION_Y,0);



        //xml->view对象
        mToast = View.inflate(getApplicationContext(),R.layout.toast_view, null);

        //获取布局文件中的textview控件
        tv_toast = (TextView) mToast.findViewById(R.id.toast_view);

        //设置toast的触摸事件
        mToast.setOnTouchListener(new View.OnTouchListener() {
            public int endY;
            public int endX;
            public int startY;
            public int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                        params.x = params.x + moveX;
                        params.y = params.y + moveY;

                        if(params.x < 0){
                            params.x = 0;
                        }
                        if(params.y < 0){
                            params.y = 0;
                        }
                        if(params.x > mWidth - mToast.getWidth()){
                            params.x = mWidth - mToast.getWidth();
                        }
                        if(params.y > mHeight - mToast.getHeight()){
                            params.y = mHeight - mToast.getHeight() - 46;
                        }

                        //随时设置显示控件的位置
                        mWM.updateViewLayout(mToast,params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_LOCATION_X,params.x);
                        SpUtil.putInt(getApplicationContext(), ConstantValue.TOAST_LOCATION_Y,params.y);
                        break;
                }
                return true;

            }
        });

        //自定义来电显示背景颜色
        mDrawableId = new int[]{
                R.drawable.call_locate_white,
                R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,
                R.drawable.call_locate_gray,
                R.drawable.call_locate_green
        };

        int toastStyleIndex = SpUtil.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
        tv_toast.setBackgroundResource(mDrawableId[toastStyleIndex]);

        //将此view对象挂在到windows窗口中,与此同时，还需要添加权限
        mWM.addView(mToast,params);

        //查询来电的归属地
        query(incomingNumber);

    }

    private void query(final String incomingNumber) {
        //对数据库的查询操作是耗时操作，所以需要在子线程中进行
        new Thread(){
            @Override
            public void run() {
                super.run();
                mAddress = AddressDao.getAddress(incomingNumber);

                //对控件的修改在子线程中不能操作，需要在主线程中进行操作
                mHandler.sendEmptyMessage(0);
            }
        }.start();

    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(tm != null && mp != null){
            tm.listen(mp,PhoneStateListener.LISTEN_NONE);
        }
    }
}
