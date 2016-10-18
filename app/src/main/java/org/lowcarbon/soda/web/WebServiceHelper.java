/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.web;

import android.content.Context;

import org.lowcarbon.soda.R;
import org.lowcarbon.soda.model.CarInfo;
import org.lowcarbon.soda.model.DriverInfo;
import org.lowcarbon.soda.model.RoadInfo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
                .baseUrl(context.getResources().getString(R.string.reset_base_url))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }

    @Override
    public Observable<RoadInfo> getRoadInfos(@Query("date") Date date, @Query("start") String start, @Query("end") String end) {
        return null;
    }

    @Override
    public Observable<List<CarInfo>> getCarInfos(@Query("lontitude") double lontitude, @Query("latitude") double latitude, @Query("num") int num) {
        return Observable.just(Arrays.asList(CarInfo.getTest()));
//        return mApiService.getCarInfos(lontitude, latitude, num);
    }

    @Override
    public Observable<DriverInfo> getDriverInfo(@Query("id") String id) {
        DriverInfo[] list = DriverInfo.getTest();
        for (DriverInfo info : list) {
            if (info.getId().equals(id)) {
                return Observable.just(info);
            }
        }
        return null;
//        return mApiService.getDriverInfo(id);
    }

}
