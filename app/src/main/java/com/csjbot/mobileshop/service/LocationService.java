package com.csjbot.mobileshop.service;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.text.TextUtils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LocationService extends BaseService {

    public LocationClient mLocationClient = null;
    private MyLocationListener mListener;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkWifi();
        initConfig();
        startQuery();
    }

    private void startQuery() {
        mLocationClient.start();
    }

    private void initConfig() {
        mListener = new MyLocationListener();
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(mListener);


        LocationClientOption option = new LocationClientOption();

        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        option.setCoorType("bd09ll");
        //// 打开gps
        option.setOpenGps(true);

        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(5000);

        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(true);

        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);
        /*
         默认false，设置是否需要地址信息
         返回省、市、区、街道等地址信息，这个api用处很大，
         很多新闻类app会根据定位返回的市区信息推送用户所在市的新闻
        */
        option.setIsNeedAddress(true);

        option.setNeedDeviceDirect(true);

        option.setLocationNotify(true);

        mLocationClient.setLocOption(option);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(mListener);
    }

    /**
     * 检查Wifi如果是关闭状态则开启Wifi
     */
    private void checkWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int state = wifiManager.getWifiState();
        if (state == WifiManager.WIFI_STATE_DISABLED) {
            wifiManager.setWifiEnabled(true);
        }
    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            switch (bdLocation.getLocType()) {
                case BDLocation.TypeNetWorkLocation:
                case BDLocation.TypeOffLineLocation:
                    CsjlogProxy.getInstance().info("定位地址:" + bdLocation.getAddrStr());
                    if (!TextUtils.isEmpty(bdLocation.getAddrStr())) {
                        Constants.LocationInfo.latitude = bdLocation.getLatitude();
                        Constants.LocationInfo.longitude = bdLocation.getLongitude();
                        Constants.LocationInfo.radius = bdLocation.getRadius();
                        Constants.LocationInfo.address = bdLocation.getAddrStr();
                        Constants.LocationInfo.city = bdLocation.getCity();

                        try {
                            JSONObject object = new JSONObject();
                            object.put("sn", Robot.SN);
                            object.put("address", bdLocation.getAddrStr());
                            object.put("latitude", bdLocation.getLatitude());
                            object.put("longitude", bdLocation.getLongitude());
                            object.put("coordinates", "bd09ll");
                            CsjlogProxy.getInstance().info("地理位置上传json : " + object.toString());
                            //上传定位信息
                            ServerFactory.createApi().uploadLocation(Robot.SN, object.toString(), new Observer<ResponseBean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(ResponseBean stringResponseBean) {
                                    if (stringResponseBean.getCode() == 200) {
                                        CsjlogProxy.getInstance().info("地理位置上传成功");
                                    } else {
                                        CsjlogProxy.getInstance().info("地理位置上传失败，Error：" + stringResponseBean.getMsg() + ",code: " + stringResponseBean.getCode());
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    CsjlogProxy.getInstance().info("地理位置上传，网络请求失败，Error：" + e.getMessage());
                                }

                                @Override
                                public void onComplete() {
                                    stopSelf();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
