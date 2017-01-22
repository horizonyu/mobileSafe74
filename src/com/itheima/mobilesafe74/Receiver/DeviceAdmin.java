package com.itheima.mobilesafe74.Receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by horizon on 12/13/2016.
 */

public class DeviceAdmin extends DeviceAdminReceiver {
    //组件名称
    ComponentName mComponentName ;

    //设备策略管理器
    DevicePolicyManager devicePolicyManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
/*        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(context,DeviceAdmin.class);

        Intent deviceAdmin = new Intent();
        deviceAdmin.addFlags(FLAG_ACTIVITY_NEW_TASK);
        //指定动作名称
        deviceAdmin.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        //指定给哪个组件授权
        deviceAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);

        //开启活动
        context.startActivity(deviceAdmin);*/

/*        //判断组件是否拥有管理员权限
        boolean adminActive = devicePolicyManager.isAdminActive(mComponentName);
        if(adminActive){
            //锁屏
           devicePolicyManager.lockNow();

            //设置密码
            devicePolicyManager.resetPassword("123",0);


        }
        else {
            *//*Intent deviceAdmin = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            deviceAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
            deviceAdmin.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "设备管理器");
            context.startActivity(deviceAdmin);*//*

        }*/

    }
}
