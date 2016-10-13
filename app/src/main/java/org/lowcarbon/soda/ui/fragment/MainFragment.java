package org.lowcarbon.soda.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.lowcarbon.soda.R;
import org.lowcarbon.soda.model.LocationMessage;
import org.lowcarbon.soda.ui.SearchActivity;
import org.lowcarbon.soda.util.BDLocationUtil;
import org.lowcarbon.soda.util.EventBusUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laizhenqi on 2016/10/7.
 */
public class MainFragment extends Fragment {

    private final static String TAG = MainFragment.class.getSimpleName();

    private final static int REQUEST_SEARCH_START = 0x0a;
    private final static int REQUEST_SEARCH_DESTINATION = 0x0b;

    @BindView(R.id.map)
    MapView mMapView;
    ArcGISFeatureLayer mFeatureLayer;
    GraphicsLayer mGraphicsLayer;

    @BindView(R.id.textview_main_start)
    TextView mLabelStart;

    @BindView(R.id.textview_main_destination)
    TextView mLabelDestination;

    private String mFeatureServiceURL = getString(R.string.featureServiceURL);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBusUtil.register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        BDLocationUtil.getInstance().start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.unpause();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    public void onStop() {
        BDLocationUtil.getInstance().stop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBusUtil.unregister(this);
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

    private void initView() {
        mMapView.enableWrapAround(true);
        mFeatureLayer = new ArcGISFeatureLayer(mFeatureServiceURL, ArcGISFeatureLayer.MODE.ONDEMAND);
        mMapView.addLayer(mFeatureLayer);
        mGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicsLayer);

        mLabelStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.setAction(SearchActivity.ACTION_SEARCH_START);
                startActivityForResult(intent, REQUEST_SEARCH_START);
            }
        });
        mLabelDestination.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.setAction(SearchActivity.ACTION_SEARCH_DESTINATION);
                startActivityForResult(intent, REQUEST_SEARCH_DESTINATION);
            }
        });
    }

}
