package com.example.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helper.AlarmHelper;
import com.example.model.CongViec;
import com.example.myapplication.R;
import com.example.Data.SQLite;

import java.util.Date;
import java.util.List;

public class CongViecListViewAdapter extends BaseAdapter {

    private Context context;
    private List<CongViec> congViecList;
    private SQLite db;

    public CongViecListViewAdapter(Context context, List<CongViec> congViecList, SQLite db) {
        this.context = context;
        this.congViecList = congViecList;
        this.db = db;
    }

    @Override
    public int getCount() {
        return congViecList.size();
    }

    @Override
    public Object getItem(int position) {
        return congViecList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return congViecList.get(position).getId();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_cong_viec, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTenCongViec = (TextView) convertView.findViewById(R.id.tvTenCongViec);
            viewHolder.tvMoTaCongViec = (TextView) convertView.findViewById(R.id.tvMoTaCongViec);
            viewHolder.tvThoiGianCongViec = (TextView) convertView.findViewById(R.id.tvThoiGianCongViec);
            viewHolder.tvTDiaDiemCongViec = (TextView) convertView.findViewById(R.id.tvDiaDiemCongViec);
            viewHolder.tvMaLoaiCongViec = (TextView) convertView.findViewById(R.id.tvMaLoaiCongViec);
            viewHolder.tvThoiGianLap = (TextView) convertView.findViewById(R.id.tvThoiGianLap);
            viewHolder.rowCongViec = (LinearLayout) convertView.findViewById(R.id.rowCongViec);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CongViec congViec = congViecList.get(position);

        viewHolder.tvTenCongViec.setText(congViec.getTenCV());
        viewHolder.tvMoTaCongViec.setText(congViec.getMoTa());
        viewHolder.tvThoiGianCongViec.setText(AlarmHelper.formatDateTime(congViec));
        viewHolder.tvTDiaDiemCongViec.setText(congViec.getDiaDiem());
        viewHolder.tvThoiGianLap.setText("Lặp lai sau: " + congViec.getThoiGianLap() + " phút");

        long congViecDateTimeMillis = congViec.getThoigian();
        long currentTime = new Date().getTime();
        // so sánh thời gian hẹn giờ với lớn hơn thời gian hiện tai
        if (congViecDateTimeMillis > currentTime) {
            viewHolder.rowCongViec.setBackgroundColor(Color.WHITE);
        } else {
            viewHolder.rowCongViec.setBackgroundColor(Color.GREEN);
        }
        Cursor cursor = db.GetData("SELECT * FROM LoaiCongViec where id = " + congViec.getMaLoaiCV());

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String ten = cursor.getString(1);
            viewHolder.tvMaLoaiCongViec.setText(ten);
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvTenCongViec,
                tvMoTaCongViec,
                tvThoiGianCongViec,
                tvTDiaDiemCongViec,
                tvMaLoaiCongViec,
                tvThoiGianLap;
        LinearLayout rowCongViec;
    }

}

