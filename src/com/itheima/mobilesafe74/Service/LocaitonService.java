package com.itheima.mobilesafe74.Service;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;

/**
 * Created by horizon on 12/12/2016.
 */

public class LocaitonService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        //1. 获取位置管理器
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //2. 由于环境会随时变化，为了能够获取较为精确的定位，需要通过Criteria根据不同的环境采取不同的定位方式
        Criteria criteria = new Criteria();

        //2.0 允许花费
        criteria.setCostAllowed(true);

        //2.1 设置准确度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        //2.2 获取提供位置的提供器
        String bestProvider = locationManager.getBestProvider(criteria, true);

        //2.3 获取位置监听器的实例
        MyLocationListener myLocationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //3.1 获取纬度
            double latitude = location.getLatitude();

            //3.2 获取经度
            double longitude = location.getLongitude();

            //4. 将经纬度信息通过短信发给指定的安全号码
            //  首先获取安全号码
            String contact_number = SpUtil.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");

            //打印中间信息
            System.out.println(contact_number);

            //  将经纬度信息发送给安全号码
            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage(contact_number, null, "经度：" + longitude + " ,纬度：" + latitude, null, null);

            //打印位置信息
            System.out.println("location" + "经度：" + longitude + " ,纬度：" + latitude);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
