/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.model;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.lowcarbon.soda.App;
import org.lowcarbon.soda.util.AssetsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/18
 */
public class DriverInfo {

    /**
     * "car_id": "28524",
     * "rate": "60.47",
     * "rank": "51",
     * "status": "在线",
     * "onlinetime": "6.00"
     */

    private String id;
    private double rate;
    private String name;
    private String phone;
    private int rank;
    private double change;
    private String status;
    private double onlinetime;
    private String car;

    private static List<DriverInfo> sDriverList;

    public static List<DriverInfo> readLocal() {
        if (sDriverList != null) {
            return sDriverList;
        }
        String result = AssetsUtils.readFromAssets(App.getInstance(), "driver_info.json");
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(result).getAsJsonArray();
        sDriverList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            try {
                JsonObject json = array.get(i).getAsJsonObject();
                String id = json.get("car_id").getAsString();
                double rate = Double.parseDouble(json.get("rate").getAsString());
                int rank = Integer.parseInt(json.get("rank").getAsString());
                String status = json.get("status").getAsString();
                double online = Double.parseDouble(json.get("onlinetime").getAsString());
                sDriverList.add(new DriverInfo(id, rate, null, null, rank, 0, status, online, id));
            } catch (Exception ignore) {

            }
        }
        return sDriverList;
    }

    public DriverInfo(String id, double rate, String name, String phone, int rank, double change, String status, double onlinetime, String car) {
        this.id = id;
        this.rate = rate;
        this.name = name;
        this.phone = phone;
        this.rank = rank;
        this.change = change;
        this.status = status;
        this.onlinetime = onlinetime;
        this.car = car;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getOnlinetime() {
        return onlinetime;
    }

    public void setOnlinetime(int onlinetime) {
        this.onlinetime = onlinetime;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }
}
