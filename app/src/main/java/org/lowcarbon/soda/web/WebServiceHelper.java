/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.web;

import android.content.Context;
import android.util.Log;

import org.lowcarbon.soda.R;
import org.lowcarbon.soda.model.CarInfo;
import org.lowcarbon.soda.model.DriverInfo;
import org.lowcarbon.soda.model.RoadInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/15
 */
public class WebServiceHelper implements ApiService {

    static WebServiceHelper sInstance;

    public static WebServiceHelper getsInstance(Context context) {
        if (sInstance == null) sInstance = new WebServiceHelper(context);
        return sInstance;
    }

    private ApiService mApiService;

    public WebServiceHelper(Context context) {
        this.mApiService = new Retrofit.Builder()
                .baseUrl(context.getResources().getString(R.string.rest_base_url))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }

    @Override
    public Observable<List<RoadInfo>> getRoadInfos( @Query("start") String start, @Query("end") String end) {
        return Observable.just(Arrays.asList(RoadInfo.getTest(start,end)));
//        return mApiService.getRoadInfos(start,end);
    }

    @Override
    public Observable<List<CarInfo>> getCarInfos(@Query("lontitude") double lontitude, @Query("latitude") double latitude, @Query("num") int num) {
        return Observable.just(CarInfo.readLocal(lontitude, latitude));
    }

    @Override
    public Observable<List<DriverInfo>> getDriverInfos(int num) {
        List<DriverInfo> list = DriverInfo.readLocal();
        List<DriverInfo> result = new ArrayList<>();
        Random rd = new Random();
        while (num > 0) {
            int n = rd.nextInt(list.size());
            if (result.contains(list.get(n))) {
                continue;
            }
            result.add(list.get(n));
            num--;
        }
        return Observable.just(result);
    }

    @Override
    public Observable<DriverInfo> getDriverInfo(@Query("id") String id) {
        List<DriverInfo> list = DriverInfo.readLocal();
        for (DriverInfo info : list) {
            if (info.getId().equals(id)) {
                return Observable.just(info);
            }
        }
        Log.i("Soda", "id:" + id);
        return null;
//        return mApiService.getDriverInfo(id);
    }

}
