package org.lowcarbon.soda.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.lowcarbon.soda.R;
import org.lowcarbon.soda.model.DriverInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO description
 *
 * @author zhenqilai@kugou.net
 * @since 18-12-8
 */
public class DriverAdapter extends BaseAdapter<DriverInfo, DriverAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_driver, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DriverInfo info = getItemAt(position);
        holder.name.setText(info.getName());
        holder.rate.setText(String.format(Locale.getDefault(), "安全指数:%.2f", info.getRate()));
        if(position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.rate)
        TextView rate;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
