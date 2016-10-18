/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.web;

import org.lowcarbon.soda.model.CarInfo;
import org.lowcarbon.soda.model.DriverInfo;
import org.lowcarbon.soda.model.RoadInfo;

import java.util.Date;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/13
 */
public interface ApiService {

    @GET("/roads")
    Observable<RoadInfo> getRoadInfos(@Query("date") Date date, @Query("start") String start, @Query("end") String end);

    @GET("/cars")
    Observable<List<CarInfo>> getCarInfos(@Query("lontitude") double lontitude, @Query("latitude") double latitude, @Query("num") int num);

    @GET("/drivers")
    public Observable<DriverInfo> getDriverInfo(@Query("id") String id);
}
