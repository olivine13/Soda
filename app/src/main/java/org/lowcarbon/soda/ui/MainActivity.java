package org.lowcarbon.soda.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.lowcarbon.soda.R;
import org.lowcarbon.soda.model.CarInfo;
import org.lowcarbon.soda.model.DriverInfo;
import org.lowcarbon.soda.model.LocationMessage;
import org.lowcarbon.soda.model.RoadInfo;
import org.lowcarbon.soda.util.ArcGISUtils;
import org.lowcarbon.soda.util.BDLocationUtil;
import org.lowcarbon.soda.util.EventBusUtil;
import org.lowcarbon.soda.web.WebServiceHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by laizhenqi on 2016/10/4.
 */
public class MainActivity extends RxAppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private final static int REQUEST_SEARCH_START = 0x0a;
    private final static int REQUEST_SEARCH_DESTINATION = 0x0b;

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
    ArcGISDynamicMapServiceLayer mImageLayer;
    GraphicsLayer mGraphicLayer;

    @BindView(R.id.textview_main_start)
    TextView mLabelStart;

    @BindView(R.id.textview_main_destination)
    TextView mLabelDestination;

    @BindView(R.id.btn_go)
    Button mBtnGo;

    @BindView(R.id.recyclerview_road_list)
    RecyclerView mRoadList;
    RoadListAdapter mRoadAdapter;

    private String mImageServiceURL;
    private String mWebServiceURL;

    private Map<CarInfo, DriverInfo> mPoint = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mImageServiceURL = getString(R.string.rest_base_url) + getString(R.string.imageServiceURL);

        EventBusUtil.register(this);

        initToolbar();
        initTabLayout();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始定位
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
            double lontitude = json.getDouble("lontitude");
//            测试阶段恒定120,30位置
//            if (BuildConfig.DEBUG) {
//                latitude = 31.5;
//                lontitude = 121.5;
//            }
            mMapView.setMapOptions(new MapOptions(MapOptions.MapType.STREETS, latitude, lontitude, 12));

            //查询当前位置附近的汽车数量
            WebServiceHelper.getsInstance(getBaseContext())
                    .getCarInfos(lontitude, latitude, 20)
                    .subscribeOn(Schedulers.newThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            //清楚地图上的兴趣点
                            mPoint.clear();
                        }
                    }).subscribeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1<List<CarInfo>, Observable<CarInfo>>() {
                        @Override
                        public Observable<CarInfo> call(List<CarInfo> carInfos) {
                            return Observable.from(carInfos);
                        }
                    }).subscribeOn(Schedulers.newThread())
                    .flatMap(new Func1<CarInfo, Observable<DriverInfo>>() {
                        @Override
                        public Observable<DriverInfo> call(CarInfo carInfo) {
                            Log.d(TAG, "纬度:" + carInfo.getLatitude() + " 经度:" + carInfo.getLontitude());
                            //绘制mark点
                            SimpleMarkerSymbol marker = new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.CIRCLE);

                            Point point = new Point(carInfo.getLontitude(), carInfo.getLatitude());
                            Graphic pointGraphic = new Graphic(ArcGISUtils.wgs2geometry(point, mMapView), marker);

                            mGraphicLayer.addGraphic(pointGraphic);

                            return WebServiceHelper.getsInstance(getBaseContext()).getDriverInfo(carInfo.getDriver())
                                    .subscribeOn(Schedulers.newThread());
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<DriverInfo>() {
                        @Override
                        public void call(DriverInfo driverInfo) {
                            //获取司机信息
                        }
                    });
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
        mLeftMenu.setAdapter(new ArrayAdapter<>(getBaseContext(), R.layout.item_road_list, R.id.road_name, new String[]{"个人信息", "设置"}));

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                Log.d(TAG, o.toString() + " status:" + status.name() + " error:" + status.getError());
            }
        });

        mImageLayer = new ArcGISDynamicMapServiceLayer(mImageServiceURL);
        mMapView.addLayer(mImageLayer);

        //加载mark图层
        mGraphicLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicLayer);

        //加载快速路图层的Url
        mMapView.enableWrapAround(true);

        mMapView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
                    switch (event.getAction()) {
                        // process the scroll wheel movement...处理滚轮事件
                        case MotionEvent.ACTION_SCROLL:
                            //获得垂直坐标上的滚动方向,也就是滚轮向下滚
                            if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f) {
                                mMapView.zoomout();
                            }
                            //获得垂直坐标上的滚动方向,也就是滚轮向上滚
                            else {
                                mMapView.zoomin();
                            }
                            return true;
                    }
                }
                return false;
            }
        });
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
        mRoadList.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mRoadList.setAdapter(mRoadAdapter = new RoadListAdapter());
        mRoadAdapter.setOnItemClickListener(new RoadListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getBaseContext(), "选中路段:" + mRoadAdapter.getItemAt(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        Observable
                .combineLatest(RxTextView.textChanges(mLabelStart), RxTextView.textChanges(mLabelDestination), new Func2<CharSequence, CharSequence, String>() {
                    @Override
                    public String call(CharSequence charSequence, CharSequence charSequence2) {
                        if (!TextUtils.isEmpty(charSequence) && !TextUtils.isEmpty(charSequence2)) {
                            return charSequence + "&" + charSequence2;
                        } else {
                            mBtnGo.setVisibility(View.GONE);
                            mRoadAdapter.clear();
                            return null;
                        }
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.<String>bindToLifecycle())
                .flatMap(new Func1<String, Observable<List<RoadInfo>>>() {
                    @Override
                    public Observable<List<RoadInfo>> call(String ret) {
                        if (ret != null) {
                            String[] split = ret.split("&");
                            return WebServiceHelper.getsInstance(getBaseContext()).getRoadInfos(split[0], split[1]);
                        } else
                            return null;
                    }
                }).subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<List<RoadInfo>, Observable<RoadInfo>>() {
                    @Override
                    public Observable<RoadInfo> call(List<RoadInfo> roadInfos) {
                        return Observable.from(roadInfos);
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mRoadAdapter.clear();
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        mRoadAdapter.notifyDataSetChanged();
                    }
                })
                .subscribe(new Action1<RoadInfo>() {
                    @Override
                    public void call(RoadInfo roadInfo) {
                        mBtnGo.setVisibility(View.VISIBLE);
                        mRoadAdapter.add(roadInfo);
                    }
                });
        mBtnGo.setVisibility(View.GONE);
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "即将开始为您导航", Toast.LENGTH_SHORT).show();
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
