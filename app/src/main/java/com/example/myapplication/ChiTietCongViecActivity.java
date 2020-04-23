package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Data.SQLite;
import com.example.helper.AlarmHelper;
import com.example.model.CongViec;

public class ChiTietCongViecActivity extends AppCompatActivity {

    Button btnQuayLai;
    TextView tvTenCV, tvMoTaCV, tvDate, tvTime, tvDiaDiem, tvLoaiCV;
    CongViec congViec;
    long id;
    boolean isFromService;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getLongExtra(ConstClass.INTENT_ID_CONGVIEC, 0);
        isFromService = intent.getBooleanExtra(ConstClass.INTENT_FROM_SERVICE, false);

        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);
        initView();

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Cursor cursor = db.GetData("SELECT * FROM CongViec where id = " + id);
        cursor.moveToFirst();

        long id = cursor.getLong(0);
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

        tvTenCV.setText(ten);
        tvMoTaCV.setText(moTa);
        tvTime.setText(AlarmHelper.formatDateTime(congViec));
        tvDiaDiem.setText(diaDiem);

        Cursor cursorQr = db.GetData("SELECT * FROM LoaiCongViec where id = " + maLoaiCV);
        cursorQr.moveToFirst();
        String tenLoaiCV = cursorQr.getString(1);
        cursorQr.close();

        tvLoaiCV.setText(tenLoaiCV);
    }

    private void initView() {
        tvTenCV = findViewById(R.id.tvTenCV);
        tvMoTaCV = findViewById(R.id.tvMoTaCV);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvDiaDiem = findViewById(R.id.tvDiaDiem);
        tvLoaiCV = findViewById(R.id.tvLoaiCV);
        btnQuayLai = findViewById(R.id.btnQuayLai);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.baothuc, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        final int position = info.position;
        if (item.getItemId() == R.id.delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietCongViecActivity.this);
            builder.setMessage("Bạn có chắc chắn muốn xóa");
            builder.setCancelable(true);
            builder.setTitle("Xác nhận xóa");
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlarmHelper.deleteAlarm(ChiTietCongViecActivity.this, congViec);
                    Toast.makeText(ChiTietCongViecActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isFromService) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
