/**
 * Copyright 2016 CVTE All Rights Reserved.
 */
package org.lowcarbon.soda.ui;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.lowcarbon.soda.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @author: laizhenqi
 * @date: 2016/10/8
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onClick(View v, int type, int position);
    }

    public final static int TYPE_HISTORY = 0x00;
    public final static int TYPE_ASSOCIATE = 0X01;
    public final static int TYPE_CLEAR = 0x02;

    private List<String> mData = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false));
        if (viewType == TYPE_HISTORY) {
            vh.icon.setVisibility(View.VISIBLE);
            vh.labelSub.setVisibility(View.GONE);
            vh.labelMain.setVisibility(View.VISIBLE);

            vh.icon.setImageResource(R.drawable.ic_search_black);
        } else if (viewType == TYPE_ASSOCIATE) {
            vh.icon.setVisibility(View.VISIBLE);
            vh.labelSub.setVisibility(View.VISIBLE);
            vh.labelMain.setVisibility(View.VISIBLE);
            vh.icon.setImageResource(R.drawable.ic_location_on_black);
        } else if (viewType == TYPE_CLEAR) {
            vh.icon.setVisibility(View.GONE);
            vh.labelSub.setVisibility(View.GONE);
            vh.labelMain.setVisibility(View.VISIBLE);

            vh.labelMain.setTextColor(parent.getResources().getColor(R.color.fontColorDark));
            vh.labelMain.setGravity(Gravity.CENTER);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String string = mData.get(position);
        if (string.startsWith("history:")) {
            holder.labelMain.setText(string.replace("history:", ""));
        } else if (string.startsWith("clear:")) {
            holder.labelMain.setText("清除历史记录");
        } else if (string.startsWith("associate:")) {
            holder.labelMain.setText(string.replace("associate:", ""));
            holder.labelSub.setText("街道名测试");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(v, holder.getItemViewType(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String getItem(int position) {
        return mData.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        String key = mData.get(position);
        if (key.startsWith("history:")) {
            return TYPE_HISTORY;
        } else if (key.startsWith("associate:")) {
            return TYPE_ASSOCIATE;
        } else if (key.startsWith("clear:")) {
            return TYPE_CLEAR;
        }
        return super.getItemViewType(position);
    }

    public void add(String s) {
        mData.add(s);
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView labelMain;
        TextView labelSub;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon_search_list_item);
            labelMain = (TextView) itemView.findViewById(R.id.textview_search_list_item_main);
            labelSub = (TextView) itemView.findViewById(R.id.textview_search_list_item_sub);
        }
    }
}
