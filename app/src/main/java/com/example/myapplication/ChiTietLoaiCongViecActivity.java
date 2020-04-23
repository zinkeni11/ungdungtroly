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

public class ChiTietLoaiCongViecActivity extends AppCompatActivity {

    EditText edtTenLoaiCV, edtMoTaLoaiCV;
    Button btnQuayLai;
    long id;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_loai_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getLongExtra(ConstClass.INTENT_ID_LOADICONGVIEC, 0);

        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);
        initView();

        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Cursor cursor = db.GetData("SELECT * FROM LoaiCongViec where id = " + id);
        cursor.moveToFirst();

        String ten = cursor.getString(1);
        String moTa = cursor.getString(2);
        edtTenLoaiCV.setText(ten);
        edtMoTaLoaiCV.setText(moTa);

        cursor.close();
    }

    private void initView() {
        edtTenLoaiCV = findViewById(R.id.edtTenLoaiCV);
        edtMoTaLoaiCV = findViewById(R.id.edtMoTaLoaiCV);
        btnQuayLai = findViewById(R.id.btnQuayLai);
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
