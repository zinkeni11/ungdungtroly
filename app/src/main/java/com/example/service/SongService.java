package com.example.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.Data.SQLite;
import com.example.model.CongViec;
import com.example.myapplication.ChiTietCongViecActivity;
import com.example.myapplication.ConstClass;
import com.example.myapplication.UtilLog;
import com.example.myapplication.R;
import com.example.receiver.NotificationReceiver;

import java.io.IOException;

public class SongService extends Service {
    private static final String TAG = "SongService";

    MediaPlayer player;
    private NotificationReceiver mNotification;
    static SQLite db;
    long id;
    CongViec congViec;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotification = new NotificationReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConstClass.ACTION_ON_TOAST);
        mFilter.addAction(ConstClass.ACTION_OFF_TOAST);
        registerReceiver(mNotification, mFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String string_receive = intent.getExtras().getString(ConstClass.EXTRA_ON_OF, null);
        id = intent.getExtras().getLong(ConstClass.INTENT_ID_CONGVIEC);
        db = new SQLite(getBaseContext(), ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);

        if (string_receive.equals(ConstClass.OFF)) {
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                    player.reset();
                    player.release();
                    player = null;
                }
            }

            NotificationManager notifManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notifManager.cancelAll();
//            stopForeground(Service.STOP_FOREGROUND_REMOVE);
            stopForeground(true);

        } else {
            try {
                player = new MediaPlayer();
                Cursor cursor = db.GetData("SELECT * FROM CongViec where id = " + id);
                cursor.moveToFirst();

                String ten = cursor.getString(1);
                String moTa = cursor.getString(2);
                long thoiGian = cursor.getLong(3);
                String diaDiem = cursor.getString(4);
                int maLoaiCV = cursor.getInt(5);
                int thoiGianLap = cursor.getInt(6);

                congViec = new CongViec(
                        id,
                        ten,
                        moTa,
                        thoiGian,
                        diaDiem,
                        maLoaiCV,
                        thoiGianLap
                );
                cursor.close();

                Intent mIntent = new Intent(getBaseContext(), ChiTietCongViecActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.putExtra(ConstClass.INTENT_ID_CONGVIEC, id);
                mIntent.putExtra(ConstClass.INTENT_FROM_SERVICE, true);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                        mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent mIntent1 = new Intent();
                mIntent1.setAction(ConstClass.ACTION_ON_TOAST);
                mIntent1.putExtra(ConstClass.INTENT_ID_CONGVIEC, id);
                PendingIntent pendingIntentOn = PendingIntent.getBroadcast(this, (int) id, mIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

                Intent mIntent2 = new Intent();
                mIntent2.setAction(ConstClass.ACTION_OFF_TOAST);
                mIntent2.putExtra(ConstClass.INTENT_ID_CONGVIEC, id);
                PendingIntent pendingIntentOff = PendingIntent.getBroadcast(this, (int) id, mIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_clock)
                        .setAutoCancel(true)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_clock))
                        .setColor(Color.BLUE)
                        .setContentTitle("BTL Android")
                        .setContentText(congViec.getTenCV())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setFullScreenIntent(null, true)
                        .addAction(R.mipmap.snooze, "Snooze", pendingIntentOn)
                        .addAction(R.mipmap.cancel, "Dismiss", pendingIntentOff)
                        .setContentIntent(contentIntent)
                        .build();
                NotificationManager notificationService =
                        (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                // Hide the notification after its selected
                builder.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationService.notify(ConstClass.MY_NOTIFICATION_ID, builder);
                startForeground(ConstClass.MY_NOTIFICATION_ID, builder);

                AssetFileDescriptor descriptor = getAssets().openFd("nhac_chuong.mp3");
                player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                player.prepare();
                player.start();

//                Intent mIntent = new Intent(getBaseContext(), ChiTietCongViecActivity.class);
//                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mIntent.putExtra(ConstClass.INTENT_ID_CONGVIEC, id);
//                mIntent.putExtra(ConstClass.INTENT_FROM_SERVICE, true);
//                getApplication().startActivity(mIntent);

            } catch (IOException e) {
                UtilLog.log_d(TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNotification);
    }
}
