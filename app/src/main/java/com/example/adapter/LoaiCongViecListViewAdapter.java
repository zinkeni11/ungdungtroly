package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.model.LoaiCongViec;
import com.example.myapplication.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LoaiCongViecListViewAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<LoaiCongViec> loaiCongViecList;

    public LoaiCongViecListViewAdapter(Context context, int layout, List<LoaiCongViec> loaiCongViecList) {
        this.context = context;
        this.layout = layout;
        this.loaiCongViecList = loaiCongViecList;
    }

    @Override
    public int getCount() {
        return loaiCongViecList.size();
    }

    @Override
    public Object getItem(int position) {
        return loaiCongViecList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return loaiCongViecList.get(position).getId();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_loai_cong_viec, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTenLoaiCongViec = convertView.findViewById(R.id.tvTenLoaiCongViec);
            viewHolder.tvMoTaLoaiCongViec = convertView.findViewById(R.id.tvMoTaLoaiCongViec);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        LoaiCongViec loaiCongViec = loaiCongViecList.get(position);

        viewHolder.tvTenLoaiCongViec.setText(loaiCongViec.getTenLoaiCV());
        viewHolder.tvMoTaLoaiCongViec.setText(loaiCongViec.getMoTaLoaiCV());

        return convertView;
    }

    static class ViewHolder {
        TextView tvTenLoaiCongViec, tvMoTaLoaiCongViec;
    }

}
