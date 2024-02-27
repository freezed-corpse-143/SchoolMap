package com.example.project_map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.RequiresApi;

import java.util.function.Consumer;

import static android.content.Context.HARDWARE_PROPERTIES_SERVICE;
import static android.content.Context.LOCATION_SERVICE;

public class SelfLocation {
    private Activity activity;


    public Activity getActivity() {
        return activity;
    }
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private double longitude=0;
    private double latitude=0;
    //监听位置信息的改变
    private LocationListener locationListener=new LocationListener() {
        //当坐标改变时触发此函数
        //需要成员变量longitude，类：double
        //需要成员变量latitude，类：double
        public void onLocationChanged(Location location) {
            longitude=location.getLongitude();
            latitude=location.getLatitude();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        @Override
        public void onProviderEnabled(String provider) {

        }
        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    //开始监听方位
    private boolean dolaunch = false;
    public void launch(){
        if(!dolaunch){
            init();
            dolaunch=true;
        }
    }

    //监听变量变化，传入处理方法
    public void moniterLocation(Consumer<Object> func,int what,String tag){
        Handler handler = new Handler(Looper.myLooper()){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void handleMessage(Message msg) {
                if(what==msg.what){
                    func.accept(new double[]{longitude,latitude});
                }
            }
        };
        Thread t = new Thread(tag){
            @Override
            public void run() {
                double lon = 0;
                double lat = 0;
                while(true){
                    if(lon!=longitude||lat!=latitude){
                        lon=longitude;
                        lat=latitude;
                        if(handler.hasMessages(what)){
                            handler.removeMessages(what);
                        }
                        Message msg = handler.obtainMessage(what);
                        msg.obj = new double[]{lon,lat};
                        handler.sendMessage(msg);
                    }
                }
            }
        };
        t.start();
    }


    private LocationManager locationManager;
    @SuppressLint("MissingPermission")
    void init(){
        //获取到LocationManager对象
        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        //创建一个Criteria对象
        Criteria criteria = new Criteria();
        //设置粗略精确度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否需要返回海拔信息
        criteria.setAltitudeRequired(false);
        //设置是否需要返回方位信息
        criteria.setBearingRequired(false);
        //设置是否允许付费服务
        criteria.setCostAllowed(true);
        //设置电量消耗等级
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        //设置是否需要返回速度信息
        criteria.setSpeedRequired(false);
        //根据设置的Criteria对象，获取最符合此标准的provider对象
        String currentProvider = locationManager.getBestProvider(criteria, true);
        //根据当前provider对象获取最后一次位置信息
        Location currentLocation = locationManager.getLastKnownLocation(currentProvider);
        locationManager.requestLocationUpdates(currentProvider, 0, 0,locationListener);
    }

    //处理方法
    public void ondestroy(){
        locationManager.removeUpdates(locationListener);
        locationListener=null;
    }

}
