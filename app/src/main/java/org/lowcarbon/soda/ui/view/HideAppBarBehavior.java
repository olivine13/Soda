/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.ui.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/14
 */
public class HideAppBarBehavior extends CoordinatorLayout.Behavior {

    private final static String TAG = HideAppBarBehavior.class.getSimpleName();

    public HideAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        Log.i(TAG,"dependency top = "+dependency.getTop()+" , right = "+dependency.getRight());
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return false;
//        return dependency.getId()== R.id.slidingUpPanel_main;
    }

}
