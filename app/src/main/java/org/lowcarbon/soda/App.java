/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda;

import android.app.Application;
import android.util.Log;

import org.lowcarbon.soda.util.BDLocationUtil;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/8
 */
public class App extends Application {

    private final static String TAG = App.class.getSimpleName();

    private static App sApp;

    public static App getInstance() {
        return sApp;
    }

    @Override
    public void onCreate() {
        Log.i(TAG,"onCreate");
        super.onCreate();
        sApp = this;

        BDLocationUtil.getInstance().register();
    }

    @Override
    public void onTerminate() {
        Log.i(TAG,"onTerminate");
        super.onTerminate();
        BDLocationUtil.getInstance().unregister();
    }
}
