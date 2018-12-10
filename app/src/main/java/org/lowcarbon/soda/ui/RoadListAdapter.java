/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
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

    SparseBooleanArray mSelectedArray = new SparseBooleanArray();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_road, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RoadInfo info = getItemAt(position);
        holder.name.setText(info.getName());
        holder.time.setText(String.format(Locale.getDefault(), "%d分钟", info.getDuration() / 60));
        holder.distance.setText(String.format(Locale.getDefault(), "%d公里", info.getDistance() / 1000));
        SpannableString span = new SpannableString(String.format(Locale.getDefault(), "安全指数%d", info.getRate()));
        ForegroundColorSpan colorSpan;
        if (position == 0) {
            colorSpan = new ForegroundColorSpan(Color.GREEN);
        } else if (position == 1) {
            colorSpan = new ForegroundColorSpan(Color.YELLOW);
        } else {
            colorSpan = new ForegroundColorSpan(Color.RED);
        }
        span.setSpan(colorSpan, 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.rate.setText(span);

        if (mSelectedArray.get(position, false)) {
            holder.itemView.setBackgroundColor(Color.GRAY);
            info.getOverlay().setFocus(true);
            info.getOverlay().addToMap();
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            info.getOverlay().setFocus(false);
            info.getOverlay().removeFromMap();
        }
    }

    public void select(int position) {
        mSelectedArray.clear();
        mSelectedArray.put(position, true);
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
