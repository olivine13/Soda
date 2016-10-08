/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.util;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;

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
    }

    public void register() {
        mLocationClient.registerLocationListener(mLocationListener);
    }

    public void unregister() {
        mLocationClient.unRegisterLocationListener(mLocationListener);
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

                EventBusUtil.postEvent(new LocationMessage(json.toString()));
            } catch (Exception e) {
                Log.e(TAG, "onReceiveLocation", e);
            }
        }
    };
}
