package org.lowcarbon.soda.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.lowcarbon.soda.R;
import org.lowcarbon.soda.model.LocationMessage;
import org.lowcarbon.soda.util.BDLocationUtil;
import org.lowcarbon.soda.util.EventBusUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laizhenqi on 2016/10/4.
 */
public class MainActivity extends RxAppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private final static int REQUEST_SEARCH_START = 0x0a;
    private final static int REQUEST_SEARCH_DESTINATION = 0x0b;

    @BindView(R.id.drawerlayout_main)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    ImageView mBtnOpen;

    @BindView(R.id.tablayout_main)
    TabLayout mTabLayout;

    @BindView(R.id.map)
    MapView mMapView;
    ArcGISFeatureLayer mFeatureLayer;
    GraphicsLayer mGraphicsLayer;

    @BindView(R.id.textview_main_start)
    TextView mLabelStart;

    @BindView(R.id.textview_main_destination)
    TextView mLabelDestination;

    private String mFeatureServiceURL ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFeatureServiceURL = getString(R.string.featureServiceURL);
        EventBusUtil.register(this);

        initToolbar();
        initTabLayout();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        BDLocationUtil.getInstance().start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.unpause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onStop() {
        BDLocationUtil.getInstance().stop();
        super.onStop();
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        Log.i(TAG, "setSupportActionBar");
        super.setSupportActionBar(toolbar);
        if (toolbar != null) {
            mBtnOpen = (ImageView) toolbar.findViewById(R.id.image_toolbar);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        EventBusUtil.unregister(this);
        mDrawerLayout.removeDrawerListener(mDrawerListener);
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String result = data.getDataString();
            switch (requestCode) {
                case REQUEST_SEARCH_START:
                    mLabelStart.setText(result);
                    break;
                case REQUEST_SEARCH_DESTINATION:
                    mLabelDestination.setText(result);
                    break;
                default:
                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBDLocationReceived(LocationMessage message) {
        Log.i(TAG, message.getContent());
        try {
            JSONObject json = new JSONObject(message.getContent());
            double latitude = json.getDouble("latitude");
            double lontitue = json.getDouble("lontitude");
//            mMapView.setMapOptions(new MapOptions(MapOptions.MapType.STREETS, latitude, lontitue, 16));
        } catch (Exception e) {
            Log.e(TAG, "json error", e);
        }
        BDLocationUtil.getInstance().stop();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void initTabLayout() {
        String[] carTypes = getResources().getStringArray(R.array.car_type);
        for (String type : carTypes) {
            mTabLayout.addTab(mTabLayout.newTab().setText(type));
        }
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, tab.getText(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initView() {
        mDrawerLayout.setDrawerShadow(R.drawable.shadow, GravityCompat.START);
        mDrawerLayout.addDrawerListener(mDrawerListener);

        if (mBtnOpen != null) {
            mBtnOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        mMapView.enableWrapAround(true);
        mFeatureLayer = new ArcGISFeatureLayer(mFeatureServiceURL, ArcGISFeatureLayer.MODE.ONDEMAND);
        mMapView.addLayer(mFeatureLayer);
        mGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicsLayer);

        mLabelStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.setAction(SearchActivity.ACTION_SEARCH_START);
                startActivityForResult(intent, REQUEST_SEARCH_START);
            }
        });
        mLabelDestination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.setAction(SearchActivity.ACTION_SEARCH_DESTINATION);
                startActivityForResult(intent, REQUEST_SEARCH_DESTINATION);
            }
        });
    }

    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
}
