/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.util;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/19
 */
public class ArcGISUtils {

    public static Geometry wgs2geometry(Point p, MapView map) {
        return GeometryEngine.project(p, SpatialReference.create(4326),map.getSpatialReference());
    }
}
