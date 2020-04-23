package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Data.SQLite;
import com.example.model.LoaiCongViec;

public class SuaLoaiCongViecActivity extends AppCompatActivity {

    EditText edtTenLoaiCV, edtMoTaLoaiCV;
    Button btnSua, btnHuy;
    LoaiCongViec loaiCongViec;
    long id;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_loai_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getLongExtra(ConstClass.INTENT_ID_LOADICONGVIEC, 0);
        initView();
        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);

        Cursor cursor = db.GetData("SELECT * FROM LoaiCongViec where id =" + id);
        cursor.moveToFirst();

        loaiCongViec = new LoaiCongViec(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2)
        );
        String ten = loaiCongViec.getTenLoaiCV();
        String moTa = loaiCongViec.getMoTaLoaiCV();

        edtTenLoaiCV.setText(ten);
        edtMoTaLoaiCV.setText(moTa);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TenLoaiCV = edtTenLoaiCV.getText().toString();
                String MoTaLoaiCV = edtMoTaLoaiCV.getText().toString();
                db.QueryData("UPDATE LoaiCongViec SET TenLoaiCV = '"+TenLoaiCV+"', MoTaLoaiCV = '"+MoTaLoaiCV+"' where id = " + id);
                onBackPressed();
            }
        });
    }

    private void initView() {
        edtTenLoaiCV = findViewById(R.id.edtTenLoaiCV);
        edtMoTaLoaiCV = findViewById(R.id.edtMoTaLoaiCV);
        btnSua = findViewById(R.id.btnSua);
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
