package org.lowcarbon.soda.ui;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.lowcarbon.soda.model.DriverInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO description
 *
 * @author zhenqilai@kugou.net
 * @since 18-12-8
 */
public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private List<T> mData = new ArrayList<>();

    public void setData(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void addData(T item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public T getItemAt(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
