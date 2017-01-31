package com.itheima.mobilesafe74.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.itheima.mobilesafe74.R;
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
    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mp = new MyPhoneStateListener();
        tm.listen(mp,PhoneStateListener.LISTEN_CALL_STATE);

        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
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
                    showToast();
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

    private void showToast() {
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

        //设置自定义toast的的位置
        params.gravity = Gravity.CENTER;

        //xml->view对象
       mToast = View.inflate(getApplicationContext(),R.layout.toast_view, null);

        //将此view对象挂在到windows窗口中,与此同时，还需要添加权限
        mWM.addView(mToast,params);

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
