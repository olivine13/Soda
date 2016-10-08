package org.lowcarbon.soda.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esri.android.map.MapView;

import org.lowcarbon.soda.R;
import org.lowcarbon.soda.ui.SearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laizhenqi on 2016/10/7.
 */
public class MainFragment extends Fragment {

    private final static int REQUEST_SEARCH_START = 0x0a;
    private final static int REQUEST_SEARCH_DESTINATION = 0x0b;

    @BindView(R.id.map)
    MapView mMapView;

    @BindView(R.id.textview_main_start)
    TextView mLabelStart;

    @BindView(R.id.textview_main_destination)
    TextView mLabelDestination;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    private void initView() {
        mMapView.enableWrapAround(true);

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
}
