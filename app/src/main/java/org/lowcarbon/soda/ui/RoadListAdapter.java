/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lowcarbon.soda.R;
import org.lowcarbon.soda.model.RoadInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/15
 */
public class RoadListAdapter extends BaseAdapter<RoadInfo, RoadListAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_road, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RoadInfo info = getItemAt(position);
        holder.name.setText(info.getName());
        holder.time.setText(info.getDuration() + "分钟");
        holder.distance.setText(info.getDistance() + "公里");
        holder.rate.setText(String.format(Locale.getDefault(), "安全指数%d", info.getRate()));
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.road_name)
        TextView name;
        @BindView(R.id.road_time)
        TextView time;
        @BindView(R.id.road_distance)
        TextView distance;
        @BindView(R.id.road_rate)
        TextView rate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
