/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lowcarbon.soda.App;
import org.lowcarbon.soda.util.AssetsUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/18
 */
public class CarInfo {

    /**
     * "carID": "10272",
     * "timestamp": "2015/12/31 14:25",
     * "lon": "123.4759",
     * "lat": "41.748156",
     * "empty": "0",
     * "speed": "0",
     * "direction": "228",
     * "alert": "0"
     */

    private String id;
    private String driver;
    private String gpstime;
    private double latitude;
    private double lontitude;
    private boolean empty;
    private int speed;
    private int direction;
    private boolean brake;
    private boolean elevated;

    public static List<CarInfo> readLocal(double lontitude, double latitude) {
        String result = AssetsUtils.readFromAssets(App.getInstance(), "car_info.json");
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(result).getAsJsonArray();
        List<CarInfo> list = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < array.size(); i++) {
            try {
                JsonObject json = array.get(i).getAsJsonObject();
                String id = json.get("carID").getAsString();
                String time = json.get("timestamp").getAsString();
                double xr = random.nextDouble();
                double yr = random.nextDouble();
                double lon = lontitude - 0.1 + yr / 10;
                double lat = latitude - 0.1 + xr / 10;
                boolean empty = TextUtils.equals(json.get("empty").getAsString(), "1");
                int speed = Integer.parseInt(json.get("speed").getAsString());
                int direction = Integer.parseInt(json.get("direction").getAsString());
                list.add(new CarInfo(id, id, time, lat, lon, empty, speed, direction, false, false));
            } catch (Exception ignore) {

            }
        }
        return list;
    }

    public CarInfo(String id, String driver, String gpstime, double latitude, double lontitude, boolean empty, int speed, int direction, boolean brake, boolean elevated) {
        this.id = id;
        this.driver = driver;
        this.gpstime = gpstime;
        this.latitude = latitude;
        this.lontitude = lontitude;
        this.empty = empty;
        this.speed = speed;
        this.direction = direction;
        this.brake = brake;
        this.elevated = elevated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getGpstime() {
        return gpstime;
    }

    public void setGpstime(String gpstime) {
        this.gpstime = gpstime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLontitude() {
        return lontitude;
    }

    public void setLontitude(double lontitude) {
        this.lontitude = lontitude;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isBrake() {
        return brake;
    }

    public void setBrake(boolean brake) {
        this.brake = brake;
    }

    public boolean isElevated() {
        return elevated;
    }

    public void setElevated(boolean elevated) {
        this.elevated = elevated;
    }
}
