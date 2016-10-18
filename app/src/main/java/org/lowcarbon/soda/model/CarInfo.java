/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.model;

import java.util.Random;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/18
 */
public class CarInfo {

    /**
     * id : 00001
     * driver : 00001
     * gpstime : 09:10:30
     * latitude : 121.3885
     * lontitude : 31.2697
     * empty : true
     * speed : 29
     * direction : 21
     * brake : true
     * elevated : true
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

    public static CarInfo[] getTest() {
        CarInfo[] cars = new CarInfo[5];
        Random random = new Random();
        for (int i = 0; i < cars.length; i++) {
            double rd = random.nextFloat() * cars.length;
            cars[i] = new CarInfo("" + i, "" + i, "" + System.currentTimeMillis(), 120 + rd, 30 + rd, false, 30, 0, false, false);
        }
        return cars;
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
