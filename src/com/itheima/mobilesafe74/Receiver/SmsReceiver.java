package com.itheima.mobilesafe74.Receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.Service.LocaitonService;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by horizon on 12/12/2016.
 */

public class SmsReceiver extends BroadcastReceiver {
    DevicePolicyManager mDPM;
    ComponentName mComponentName;

    @Override
    public void onReceive(Context context, Intent intent) {

        //0. 判断手机是否已经开启手机防盗功能
        boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);

        if (open_security) {
            // 1.1 监听手机是否收到短信
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object object : objects) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);

                //1.2 获取短信的内容x
                String message = sms.getMessageBody();

                //1.3 判断短信中是否包含#*alarm*#
                if (message.contains("#*alarm*#")) {

                    //1.4 如果包含则播放报警音乐 MediaPlayer
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }

                //1.5 判断短信中是否包含#*location*#
                if (message.contains("#*location*#")) {

                    //1.5.1 将获置取位置信息的功能放在服务中
                    Intent service = new Intent(context, LocaitonService.class);
                    context.startService(service);
                }

                //1.6.0 获取设备策略管理器
                mDPM =
                        (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
                //组件名称
                mComponentName = new ComponentName(context, DeviceAdmin.class);

                //判断此组件是否已经获得管理员权限
                boolean adminActive = mDPM.isAdminActive(mComponentName);

                //1.6 判断短信中是否包含#*wipedata*#
                if (message.contains("#*wipedata*#")) {
                    if (adminActive) {
                        //清除数据，恢复出厂设置
                        mDPM.wipeData(0);

                        //清除外存上的数据
//                        mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                    } else {
                        //若没有开启设备保护，则提示用户开启
                        Toast.makeText(context, "请开启设备保护", Toast.LENGTH_SHORT).show();

                       /* Intent deviceAdmin = new Intent();
                        deviceAdmin.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        //指定动作名称
                        deviceAdmin.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

                        //指定给哪个组件授权
                        deviceAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);

                        context.startActivity(deviceAdmin);*/
                    }


                }

                //1.7 判断短信中是否包含#*lockscreen*#
                if (message.contains("#*lockscreen*#")) {
                    if (adminActive) {
                        //设置解屏密码
                        mDPM.resetPassword("1234", 0);

                        //立即锁屏
                        mDPM.lockNow();


                    } else {
                        //若没有开启设备保护，则提示用户开启
                        Toast.makeText(context, "请开启设备保护", Toast.LENGTH_SHORT).show();

                       /* Intent deviceAdmin = new Intent();
                        deviceAdmin.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        //指定动作名称
                        deviceAdmin.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

                        //指定给哪个组件授权
                        deviceAdmin.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);

                        context.startActivity(deviceAdmin);*/
                    }


                }

                if(message.contains("#*uninstall*#")){
                    if (!adminActive) {
                        //卸载此应用
                        Intent uninstall = new Intent("android.intent.action.DELETE");
                        uninstall.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        uninstall.addCategory("android.intent.category.DEFAULT");
                        uninstall.setData(Uri.parse("package:"+context.getPackageName()));
                        context.startActivity(uninstall);


                    } else {
                        //若开启设备保护，则提示用户关闭
                        Toast.makeText(context, "请关闭设备保护", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        }


    }
}
