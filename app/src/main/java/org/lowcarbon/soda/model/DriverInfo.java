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
import java.util.Random;

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
    private CarInfo car;

    private static List<DriverInfo> sDriverList;

    public static List<DriverInfo> readLocal() {
        if (sDriverList != null) {
            return sDriverList;
        }
        String result = AssetsUtils.readFromAssets(App.getInstance(), "driver_info.json");
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(result).getAsJsonArray();
        sDriverList = new ArrayList<>();
        Random rd = new Random();
        for (int i = 0; i < array.size(); i++) {
            try {
                JsonObject json = array.get(i).getAsJsonObject();
                String id = json.get("car_id").getAsString();
                double rate = Double.parseDouble(json.get("rate").getAsString());
                int rank = Integer.parseInt(json.get("rank").getAsString());
                String status = json.get("status").getAsString();
                double online = Double.parseDouble(json.get("onlinetime").getAsString());
                CarInfo carInfo = null;
                List<CarInfo> carInfos = CarInfo.readLocal(0, 0);
                if (carInfos != null) {
                    for (CarInfo car : carInfos) {
                        if (car.getId().equals(id)) {
                            carInfo = car;
                            break;
                        }
                    }
                }
                sDriverList.add(new DriverInfo(id, rate, surName[rd.nextInt(surName.length)] + "师傅", null, rank, 0, status, online, carInfo));
            } catch (Exception ignore) {

            }
        }
        return sDriverList;
    }

    static String surName[] = {
            "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "楮", "卫", "蒋", "沈", "韩", "杨",
            "朱", "秦", "尤", "许", "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜",
            "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
            "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐",
            "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
            "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄",
            "和", "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧",
            "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒", "屈", "项", "祝", "董", "梁",
            "杜", "阮", "蓝", "闽", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童", "颜", "郭",
            "梅", "盛", "林", "刁", "锺", "徐", "丘", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍",
            "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "应", "宗",
            "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚",
            "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁", "荀", "羊", "於", "惠", "甄", "麹", "家", "封",
            "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓",
            "牧", "隗", "山", "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫",
            "宁", "仇", "栾", "暴", "甘", "斜", "厉", "戎", "祖", "武", "符", "刘", "景", "詹", "束", "龙",
            "叶", "幸", "司", "韶", "郜", "黎", "蓟", "薄", "印", "宿", "白", "怀", "蒲", "邰", "从", "鄂",
            "索", "咸", "籍", "赖", "卓", "蔺", "屠", "蒙", "池", "乔", "阴", "郁", "胥", "能", "苍", "双",
            "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍",
            "郤", "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "郏", "浦", "尚", "农",
            "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹", "习", "宦", "艾", "鱼", "容",
            "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘",
            "匡", "国", "文", "寇", "广", "禄", "阙", "东", "欧", "殳", "沃", "利", "蔚", "越", "夔", "隆",
            "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶", "空",
            "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红",
            "游", "竺", "权", "逑", "盖", "益", "桓", "公", "晋", "楚", "阎", "法", "汝", "鄢", "涂", "钦",
            "岳", "帅", "缑", "亢", "况", "后", "有", "琴", "商", "牟", "佘", "佴", "伯", "赏", "墨", "哈",
            "谯", "笪", "年", "爱", "阳", "佟"};

    public DriverInfo(String id, double rate, String name, String phone, int rank, double change, String status, double onlinetime, CarInfo car) {
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

    public CarInfo getCar() {
        return car;
    }

    public void setCar(CarInfo car) {
        this.car = car;
    }
}
