package org.lowcarbon.soda.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.lowcarbon.soda.R;
import org.lowcarbon.soda.model.CarInfo;
import org.lowcarbon.soda.model.DriverInfo;
import org.lowcarbon.soda.model.RoadInfo;
import org.lowcarbon.soda.web.WebServiceHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by laizhenqi on 2016/10/4.
 */
public class MainActivity extends RxAppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private static final double LATITUDE = 39.18;
    private static final double LONGITUDE = 117.24;

    private final static int REQUEST_SEARCH_START = 0x0a;
    private final static int REQUEST_SEARCH_DESTINATION = 0x0b;

    private static Bitmap sCarBitmap;

    @BindView(R.id.drawerlayout_main)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.lv_left_menu)
    ListView mLeftMenu;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    ImageView mBtnOpen;

    @BindView(R.id.tablayout_main)
    TabLayout mTabLayout;

    @BindView(R.id.map)
    MapView mMapView;
    BaiduMap mBaiduMap;

    @BindView(R.id.textview_main_start)
    TextView mLabelStart;

    @BindView(R.id.textview_main_destination)
    TextView mLabelDestination;

    @BindView(R.id.btn_go)
    Button mBtnGo;

    @BindView(R.id.recyclerView_driver)
    RecyclerView mDriverList;
    DriverAdapter mDriverAdapter;

    @BindView(R.id.recyclerView_road)
    RecyclerView mRoadList;
    RoadListAdapter mRoadListAdapter;

    private String mImageServiceURL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mImageServiceURL = getString(R.string.rest_base_url) + getString(R.string.imageServiceURL);

        initToolbar();
        initTabLayout();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
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
        mDrawerLayout.removeDrawerListener(mDrawerListener);
        super.onDestroy();
        mMapView.onDestroy();
        if (mSearch != null) {
            mSearch.destroy();
        }
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
        mLeftMenu.setAdapter(new ArrayAdapter<>(getBaseContext(), R.layout.item_road, R.id.road_name, new String[]{"个人信息", "设置"}));

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(LATITUDE, LONGITUDE)));
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
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
        mRoadList.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
        mRoadList.setAdapter(mRoadListAdapter = new RoadListAdapter() {
            @Override
            public void onBindViewHolder(ViewHolder holder, final int position) {
                super.onBindViewHolder(holder, position);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        select(position);
                        notifyDataSetChanged();
                    }
                });
            }
        });
        mDriverList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mDriverList.setAdapter(mDriverAdapter = new DriverAdapter() {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startSearchRoadInfo(mLabelStart.getText().toString(), mLabelDestination.getText().toString());
                    }
                });
            }
        });
        mLabelStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(mLabelStart.getText()) && !TextUtils.isEmpty(mLabelDestination.getText())) {
                    startSearchDriver();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mLabelDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(mLabelStart.getText()) && !TextUtils.isEmpty(mLabelDestination.getText())) {
                    startSearchDriver();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mBtnGo.setVisibility(View.GONE);
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "即将开始为您导航", Toast.LENGTH_SHORT).show();
                Observable.timer(5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                                       @Override
                                       public void call(Long aLong) {
                                           new CommonDialog.Builder(MainActivity.this).message("司机已偏移路线").build().show();
                                       }
                                   }
                        );
            }
        });

        mBaiduMap.clear();
        //查询当前位置附近的汽车数量
        WebServiceHelper.getsInstance(getBaseContext())
                .getCarInfos(LONGITUDE, LATITUDE, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CarInfo>>() {
                    @Override
                    public void call(List<CarInfo> carInfos) {
                        if (sCarBitmap == null) {
                            sCarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car);
                        }
                        List<OverlayOptions> options = new ArrayList<>();
                        for (CarInfo carInfo : carInfos) {
                            OverlayOptions opt = new MarkerOptions()
                                    .position(new LatLng(carInfo.getLatitude(), carInfo.getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(sCarBitmap));
                            options.add(opt);
                        }
                        mBaiduMap.addOverlays(options);
                    }
                });
    }

    private void startSearchDriver() {
        // 随机展示5个司机信息，按安全指数排序
        mRoadList.setVisibility(View.GONE);
        mDriverAdapter.clearData();
        mBaiduMap.clear();
        WebServiceHelper.getsInstance(this)
                .getDriverInfos(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<DriverInfo>>() {
                    @Override
                    public void call(List<DriverInfo> driverInfos) {
                        mDriverAdapter.setData(driverInfos);
                        if (sCarBitmap == null) {
                            sCarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car);
                        }
                        List<OverlayOptions> options = new ArrayList<>();
                        for (DriverInfo d : driverInfos) {
                            CarInfo carInfo = d.getCar();
                            if (carInfo == null) continue;
                            OverlayOptions opt = new MarkerOptions()
                                    .position(new LatLng(carInfo.getLatitude(), carInfo.getLongitude()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(sCarBitmap));
                            options.add(opt);
                        }
                        mBaiduMap.addOverlays(options);
                        mBtnGo.setVisibility(View.VISIBLE);
                    }
                });
    }

    private Dialog mWaitingDialog;
    private RoutePlanSearch mSearch;

    private void startSearchRoadInfo(final String st, final String dst) {
        if (mWaitingDialog == null) {
            mWaitingDialog = new CommonDialog.Builder(this).message("正在查询路线请稍后...").build();
        }
        mWaitingDialog.setCancelable(false);
        if (!mWaitingDialog.isShowing()) {
            mWaitingDialog.show();
        }
        if (mSearch == null) {
            mSearch = RoutePlanSearch.newInstance();
            mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
                @Override
                public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

                }

                @Override
                public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

                }

                @Override
                public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

                }

                @Override
                public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                    List<DrivingRouteLine> result = drivingRouteResult.getRouteLines();
                    if (result != null) {
                        List<RoadInfo> list = new ArrayList<>();
                        int i = 0;
                        int[] rate = new int[]{94, 74, 56};
                        for (DrivingRouteLine line : result) {
                            if (i >= 3) {
                                break;
                            }
                            DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
                            overlay.setData(line);
                            overlay.addToMap();
                            overlay.zoomToSpan();
                            overlay.setFocus(false);
                            list.add(new RoadInfo("路线" + (i + 1), rate[i], line.getDuration(), line.getDistance(), overlay));
                            i++;
                        }
                        if (!list.isEmpty()) {
                            mRoadList.setVisibility(View.VISIBLE);
                            mRoadListAdapter.setData(list);
                        }
                    }
                    mWaitingDialog.dismiss();
                }

                @Override
                public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

                }

                @Override
                public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

                }
            });
        }
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("天津", st);
        PlanNode dstNode = PlanNode.withCityNameAndPlaceName("天津", dst);
        mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(dstNode));
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
