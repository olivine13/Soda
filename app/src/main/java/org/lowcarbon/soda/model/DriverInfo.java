/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.model;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/18
 */
public class DriverInfo {

    /**
     * rate : 87
     * name : 李师傅
     * phone : 13900000000
     * rank : 23
     * change : 0.01
     * status : 在线
     * onlinetime : 160
     * car : 00001
     */

    private String id;
    private int rate;
    private String name;
    private String phone;
    private int rank;
    private double change;
    private String status;
    private int onlinetime;
    private String car;

    public static DriverInfo[] getTest() {
        DriverInfo[] data = new DriverInfo[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = new DriverInfo("" + i, 100, "师傅" + i, "1390000000" + i, 10 - i, 0, "在线", 160, "" + i);
        }
        return data;
    }

    public DriverInfo(String id, int rate, String name, String phone, int rank, double change, String status, int onlinetime, String car) {
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

    public int getRate() {
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

    public int getOnlinetime() {
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
