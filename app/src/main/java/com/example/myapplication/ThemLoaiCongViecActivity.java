package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Data.SQLite;

public class ThemLoaiCongViecActivity extends AppCompatActivity {

    EditText edtTenLoaiCV, edtMoTaLoaiCV;
    Button btnThem, btnHuy;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_loai_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initView();
        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TenLoaiCV = edtTenLoaiCV.getText().toString();
                String MoTaLoaiCV = edtMoTaLoaiCV.getText().toString();
                db.QueryData("INSERT INTO LoaiCongViec VALUES (null, '"+TenLoaiCV+"', '"+MoTaLoaiCV+"')");
                onBackPressed();
            }
        });
    }

    private void initView() {
        edtTenLoaiCV = findViewById(R.id.edtTenLoaiCV);
        edtMoTaLoaiCV = findViewById(R.id.edtMoTaLoaiCV);
        btnThem = findViewById(R.id.btnThem);
        btnHuy = findViewById(R.id.btnHuy);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

}
