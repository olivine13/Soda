/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.model;

import java.util.List;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/13
 */
public class RoadInfo {


    /**
     * rate : 78
     * paths : [{"latitue":123,"lontitue":21},{"latitue":125,"lontitue":22}]
     */

    private int rate;
    /**
     * latitue : 123
     * lontitue : 21
     */

    private List<PathsBean> paths;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public List<PathsBean> getPaths() {
        return paths;
    }

    public void setPaths(List<PathsBean> paths) {
        this.paths = paths;
    }

    public static class PathsBean {
        private int latitue;
        private int lontitue;

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
