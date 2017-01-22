package com.itheima.mobilesafe74.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;

/**
 * Created by horizon on 12/12/2016.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Hello", "收到开机广播");
        //1. 首先获取存储在本地的手机卡序列号
        String spSimSerialNumber = SpUtil.getString(context, ConstantValue.SIM_NUMBER, "");

        //2. 获取手机中sim卡的序列号
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = tm.getSimSerialNumber() + "******";

        //3. 将两个序列号进行比较
        if (!spSimSerialNumber.equals(simSerialNumber)) {
            //4. 如果不同则向安全号码发送报警短信
            //4.1 获取安全号码
            String safe_number = SpUtil.getString(context, ConstantValue.CONTACT_PHONE, "");

            //4.2 通过短信管理器向安全号码发送短信
            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage(safe_number, "", "sim卡变更！！！", null, null);
        }

    }
}
