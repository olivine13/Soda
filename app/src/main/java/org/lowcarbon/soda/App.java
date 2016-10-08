/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda;

import android.app.Application;

import org.lowcarbon.soda.util.BDLocationUtil;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/8
 */
public class App extends Application {

    private static App sApp;

    public static App getInstance() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;

        BDLocationUtil.getInstance().register();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        BDLocationUtil.getInstance().unregister();
    }
}
