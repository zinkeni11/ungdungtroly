package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.Data.SQLite;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    Button btnLoaiCongViec, btnCongViec, btnFilter;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        btnLoaiCongViec.setOnClickListener(this);
        btnCongViec.setOnClickListener(this);
        btnFilter.setOnClickListener(this);

        try {
            db = new SQLite(MainActivity.this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);
            // tao bang
            db.QueryData("DROP TABLE IF EXISTS LoaiCongViec");
            db.QueryData("DROP TABLE IF EXISTS CongViec");

            db.QueryData("CREATE TABLE IF NOT EXISTS LoaiCongViec (id INTEGER PRIMARY KEY AUTOINCREMENT, TenLoaiCV VARCHAR, MoTaLoaiCV VARCHAR)");
            db.QueryData("CREATE TABLE IF NOT EXISTS CongViec (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "TenCV VARCHAR, " +
                    "MoTa VARCHAR, " +
                    "ThoiGian INTEGER, " +
                    "DiaDiem VARCHAR, " +
                    "MaLoaiCV INTEGER," +
                    "ThoiGianLap INTEGER" +
                    ")");
        } catch (Exception e) {
            UtilLog.log_d(TAG, e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        btnLoaiCongViec = findViewById(R.id.btnLoaiCongViec);
        btnCongViec = findViewById(R.id.btnCongViec);
        btnFilter = findViewById(R.id.btnFilter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoaiCongViec:
                startActivity(new Intent(MainActivity.this, LoaiCongViecActivity.class));
                break;
            case R.id.btnCongViec:
                startActivity(new Intent(MainActivity.this, CongViecActivity.class));
                break;
            case R.id.btnFilter:
                startActivity(new Intent(MainActivity.this, LocActivity.class));
                break;
            default:
                break;
        }
    }
}