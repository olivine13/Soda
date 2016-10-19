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

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/15
 */
public class RoadListAdapter extends RecyclerView.Adapter<RoadListAdapter.ViewHolder> {


    private final List<RoadInfo> mData = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_road_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RoadInfo info = mData.get(position);
        holder.name.setText(info.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public RoadInfo getItemAt(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void add(RoadInfo item) {
        mData.add(item);
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void refresh(List<RoadInfo> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.road_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
