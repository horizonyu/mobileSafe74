package com.itheima.mobilesafe74.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by horizon on 1/31/2017.
 */

public class ServiceUtil {
    private static ActivityManager mAM;
    /**
     * 此方法用于判断控件的状态，如果服务已经开启，则表明控件处于on的状态，反之亦成立。
     * @param ctx 上下文环境
     * @param serviceName 需要判断的服务的名称
     * @return true 服务开启 false 服务关闭
     */
    public static boolean isRunning(Context ctx,String serviceName){
        //获取activity管理器
        mAM = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        //获取activity正在运行的服务的数量
        List<ActivityManager.RunningServiceInfo> runningServices = mAM.getRunningServices(100);

        //循环遍历服务中是否存在指定的服务信息
        for (ActivityManager.RunningServiceInfo runningServiceInfo:runningServices) {
            if(runningServiceInfo.service.getClassName().equals(serviceName)){
                //服务正在运行
                return true;
            }
        }

        //服务没有在运行
        return false;
    }
}
