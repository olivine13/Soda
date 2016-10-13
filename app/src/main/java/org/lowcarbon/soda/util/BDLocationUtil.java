/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.util;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.platform.comapi.location.CoordinateType;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lowcarbon.soda.App;
import org.lowcarbon.soda.model.LocationMessage;

import java.util.List;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/8
 */
public class BDLocationUtil {

    private final static String TAG = BDLocationUtil.class.getSimpleName();

    private static BDLocationUtil INSTANCE;

    public static BDLocationUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BDLocationUtil(App.getInstance());
        }
        return INSTANCE;
    }

    private LocationClient mLocationClient;

    private BDLocationUtil(Context context) {
        mLocationClient = new LocationClient(context);
        initLocation();
    }

    public void register() {
        mLocationClient.registerLocationListener(mLocationListener);
    }

    public void start() {
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    public void stop() {
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    public void unregister() {
        mLocationClient.unRegisterLocationListener(mLocationListener);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(CoordinateType.WGS84);//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private BDLocationListener mLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            try {
                JSONObject json = new JSONObject();
                json.put("time", location.getTime());
                json.put("code", location.getLocType());
                json.put("latitude", location.getLatitude());
                json.put("lontitude", location.getLongitude());
                json.put("radius", location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    json.put("speed", location.getSpeed());
                    json.put("satellite", location.getSatelliteNumber());
                    json.put("height", location.getAltitude());
                    json.put("direction", location.getDirection());
                    json.put("addr", location.getAddrStr());
                    json.put("describe", "gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    json.put("addr", location.getAddrStr());
                    //运营商信息
                    json.put("operationers", location.getOperators());
                    json.put("describe", "网络定位定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    json.put("describe", "离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    json.put("describe", "服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    json.put("describe", "网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    json.put("describe", "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }

                json.put("locationdescribe", location.getLocationDescribe());// 位置语义化信息

                List<Poi> list = location.getPoiList();// POI数据
                if (list != null) {
                    JSONArray array = new JSONArray();
                    for (Poi p : list) {
                        JSONObject poi = new JSONObject();
                        poi.put("id", p.getId());
                        poi.put("name", p.getName());
                        poi.put("rank", p.getRank());
                        array.put(poi);
                    }
                    json.put("pois", array);
                }

                Log.i(TAG,json.toString());
                EventBusUtil.postEvent(new LocationMessage(json.toString()));
            } catch (Exception e) {
                Log.e(TAG, "onReceiveLocation", e);
            }
        }
    };
}
