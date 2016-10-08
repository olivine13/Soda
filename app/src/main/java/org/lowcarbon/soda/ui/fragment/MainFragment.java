package org.lowcarbon.soda.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.android.map.MapView;

import org.lowcarbon.soda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laizhenqi on 2016/10/7.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.map)
    MapView mMapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main,container,false);
        ButterKnife.bind(this,root);
        initView();
        return root;
    }

    private void initView() {
        mMapView.enableWrapAround(true);
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
}
