/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.model;

import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/13
 */
public class RoadInfo {


    /**
     * rate : 78
     * paths : [{"name":"xx",latitue":123,"lontitue":21},{"name":"yy","latitue":125,"lontitue":22}]
     */

    private String name;

    private int rate;

    private int duration;

    private int distance;

    private DrivingRouteOverlay overlay;

    public RoadInfo(String name, int rate, int duration, int distance, DrivingRouteOverlay overlay) {
        this.name = name;
        this.rate = rate;
        this.duration = duration;
        this.distance = distance;
        this.overlay = overlay;
    }

    public RoadInfo(String name, int rate, List<PathsBean> paths) {
        this.name = name;
        this.rate = rate;
        this.paths = paths;
    }

    /**
     * latitue : 123
     * lontitue : 21
     */



    private List<PathsBean> paths;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public DrivingRouteOverlay getOverlay() {
        return overlay;
    }

    public void setOverlay(DrivingRouteOverlay overlay) {
        this.overlay = overlay;
    }

    public List<PathsBean> getPaths() {
        return paths;
    }

    public void setPaths(List<PathsBean> paths) {
        this.paths = paths;
    }

    public static RoadInfo[] getTest(String start, String end) {
        RoadInfo[] list = new RoadInfo[8];
        for( int i = 0 ;i < list.length ; i++ ) {
            list[i] = new RoadInfo(start+"-"+end+i,80+i,new ArrayList<PathsBean>());
        }
        return list;
    }

    public static class PathsBean {
        private String name;
        private int latitue;
        private int lontitue;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLatitue() {
            return latitue;
        }

        public void setLatitue(int latitue) {
            this.latitue = latitue;
        }

        public int getLontitue() {
            return lontitue;
        }

        public void setLontitue(int lontitue) {
            this.lontitue = lontitue;
        }
    }
}
