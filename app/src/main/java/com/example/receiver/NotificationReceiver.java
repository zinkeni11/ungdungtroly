package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.Data.SQLite;
import com.example.helper.AlarmHelper;
import com.example.model.CongViec;
import com.example.myapplication.ConstClass;

import java.util.Objects;

import static com.example.myapplication.ConstClass.ACTION_OFF_TOAST;
import static com.example.myapplication.ConstClass.ACTION_ON_TOAST;
import static com.example.myapplication.ConstClass.DELETE_SUCCESS;
import static com.example.myapplication.ConstClass.INTENT_ID_CONGVIEC;
import static com.example.myapplication.ConstClass.SNOOZE_SUCCESS;

public class NotificationReceiver extends BroadcastReceiver {
    static SQLite db;
    Cursor cursor;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            long id = Objects.requireNonNull(intent.getExtras()).getLong(INTENT_ID_CONGVIEC, 0);
            db = new SQLite(context, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);
            cursor = db.GetData("SELECT * FROM CongViec where id = " + id);
            cursor.moveToFirst();

            String ten = cursor.getString(1);
            String moTa = cursor.getString(2);
            long thoiGian = cursor.getLong(3);
            String diaDiem = cursor.getString(4);
            int maLoaiCV = cursor.getInt(5);
            int thoiGianLap = cursor.getInt(6);
            CongViec congViec = new CongViec(
                    id,
                    ten,
                    moTa,
                    thoiGian,
                    diaDiem,
                    maLoaiCV,
                    thoiGianLap
            );

            switch (Objects.requireNonNull(intent.getAction())) {
                case ACTION_ON_TOAST:
                    Toast.makeText(context, SNOOZE_SUCCESS, Toast.LENGTH_SHORT).show();
                    AlarmHelper.deleteAlarm(context, congViec);
                    AlarmHelper.SnoozeAlarm(context, congViec);
                    break;
                case ACTION_OFF_TOAST:
                    Toast.makeText(context, DELETE_SUCCESS, Toast.LENGTH_SHORT).show();
                    AlarmHelper.deleteAlarm(context, congViec);
                    break;
            }
        }
    }
}