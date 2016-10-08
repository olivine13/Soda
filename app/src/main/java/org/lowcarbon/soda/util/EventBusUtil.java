package org.lowcarbon.soda.util;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Tridom on 2015/5/15.
 */
public class EventBusUtil {

    public static void postEvent(Object object) {
        EventBus.getDefault().post(object);
    }

    public static void register(Object object) {
        EventBus.getDefault().unregister(object);
        EventBus.getDefault().register(object);
    }

    public static void unregister(Object object) {
        EventBus.getDefault().unregister(object);
    }
}
