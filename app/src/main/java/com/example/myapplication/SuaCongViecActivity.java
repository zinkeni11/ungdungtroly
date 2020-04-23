package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Data.SQLite;
import com.example.helper.AlarmHelper;
import com.example.model.CongViec;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SuaCongViecActivity extends AppCompatActivity {

    ArrayAdapter<String> arrLoaiCongViec;
    ArrayAdapter<Integer> arrThoiGianLap;
    EditText edtTen, edtMoTa, edtDate, edtTime, edtDiaDiem;
    Spinner spnLoaiCV, spnThoiGianLap;
    Button btnDatePicker, btnTimePicker, btnSubmit, btnCancel;
    Calendar calendar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Cursor cursor;
    int maLoaiCV, thoiGianLap;
    long id;
    List<String> tenLoaiCVList;
    List<Integer> thoiGianLapList, maLoaiCVList;
    CongViec congViec;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_cong_viec);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getLongExtra(ConstClass.INTENT_ID_CONGVIEC, 0);

        initView();
        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);

        cursor = db.GetData("SELECT * FROM CongViec where id = " + id);
        cursor.moveToFirst();

        id = cursor.getLong(0);
        String ten = cursor.getString(1);
        String moTa = cursor.getString(2);
        long thoiGian = cursor.getLong(3);
        String diaDiem = cursor.getString(4);
        maLoaiCV = cursor.getInt(5);
        thoiGianLap = cursor.getInt(6);
        congViec = new CongViec(id, ten, moTa, thoiGian, diaDiem, maLoaiCV, thoiGianLap);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(congViec.getThoigian());
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        edtTen.setText(ten);
        edtMoTa.setText(moTa);
        edtDate.setText(AlarmHelper.formatDate(congViec));
        edtTime.setText(AlarmHelper.formatTime(congViec));
        edtDiaDiem.setText(diaDiem);

        cursor.close();

        cursor = db.GetData("SELECT * FROM LoaiCongViec");

        maLoaiCVList = new ArrayList<>();
        tenLoaiCVList = new ArrayList<>();
        thoiGianLapList = new ArrayList<>();

        while (cursor.moveToNext()) {
            maLoaiCVList.add(cursor.getInt(0));
            tenLoaiCVList.add(cursor.getString(1));
        }
        cursor.close();

        for (int i = 0; i < 60; i++) {
            thoiGianLapList.add(i);
        }

        arrLoaiCongViec = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenLoaiCVList);
        arrLoaiCongViec.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnLoaiCV.setAdapter(arrLoaiCongViec);

        arrThoiGianLap = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, thoiGianLapList);
        arrThoiGianLap.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnThoiGianLap.setAdapter(arrThoiGianLap);

        spnLoaiCV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maLoaiCV = maLoaiCVList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spnThoiGianLap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thoiGianLap = thoiGianLapList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        for (int i = 0; i < maLoaiCVList.size(); i++) {
            if (maLoaiCVList.get(i) == maLoaiCV) {
                spnLoaiCV.setSelection(i);
            }
        }
        for (int i = 0; i < thoiGianLapList.size(); i++) {
            if (thoiGianLapList.get(i) == thoiGianLap) {
                spnThoiGianLap.setSelection(i);
            }
        }

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SuaCongViecActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SuaCongViecActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        edtTime.setText(hourOfDay + ":" + minute);
                        mHour = hourOfDay;
                        mMinute = minute;
                    }
                }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(mYear, mMonth, mDay, mHour, mMinute, 0);

                CongViec congViec = new CongViec(
                        id,
                        edtTen.getText().toString(),
                        edtMoTa.getText().toString(),
                        calendar.getTimeInMillis(),
                        edtDiaDiem.getText().toString(),
                        maLoaiCV,
                        thoiGianLap
                );

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put("TenCV", congViec.getTenCV());
                values.put("MoTa", congViec.getMoTa());
                values.put("ThoiGian", congViec.getThoigian());
                values.put("DiaDiem", congViec.getDiaDiem());
                values.put("MaLoaiCV", congViec.getMaLoaiCV());
                values.put("ThoiGianLap", congViec.getThoiGianLap());

                int row_afftected = db.Update("CongViec", values, id);
                if (row_afftected > 0) {
                    AlarmHelper.deleteAlarm(SuaCongViecActivity.this, congViec);
                    AlarmHelper.createAlarm(SuaCongViecActivity.this, congViec);
                    onBackPressed();
                } else {
                    Toast.makeText(SuaCongViecActivity.this, "Update fail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        edtTen = findViewById(R.id.edtTenCV);
        edtMoTa = findViewById(R.id.edtMoTaCV);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        edtDiaDiem = findViewById(R.id.edtDiaDiem);
        spnLoaiCV = findViewById(R.id.spnLoaiCV);
        spnThoiGianLap = findViewById(R.id.spnThoiGianLap);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnTimePicker = findViewById(R.id.btnTimePicker);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
