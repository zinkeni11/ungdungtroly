package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.Data.SQLite;
import com.example.adapter.CongViecListViewAdapter;
import com.example.helper.AlarmHelper;
import com.example.model.CongViec;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CongViecActivity extends AppCompatActivity {
    private static final String TAG = "CongViecActivity";

    private List<CongViec> congViecList, mCongViecList;
    GridView gridView;
    EditText edtKeyword;
    CongViecListViewAdapter adapter;
    static SQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cong_viec);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(CongViecActivity.this, ThemCongViecActivity.class));
            }
        });

        congViecList = new ArrayList<>();
        mCongViecList = new ArrayList<>();

        initView();
        adapter = new CongViecListViewAdapter(this, congViecList, db);
        registerForContextMenu(gridView);

        gridView.setAdapter(adapter);

        edtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyword = editable.toString();
                UtilLog.log_d(TAG, keyword);
//                String keyword = edtKeyword.getText().toString().trim();
                int text_length = keyword.length();
                congViecList.clear();
                if (text_length > 0) {
                    for (int i = 0; i < mCongViecList.size(); i++) {
                        CongViec mCongViec = mCongViecList.get(i);
                        if (mCongViec.getMoTa().toLowerCase().contains(keyword.toLowerCase())) {
                            congViecList.add(mCongViec);
                        }
                    }
                } else {
                    congViecList.addAll(mCongViecList);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Cursor cursor = db.GetData("SELECT * FROM CongViec");

        while (cursor.moveToNext()) {
            UtilLog.log_d(TAG, cursor.getLong(3) + "");
            congViecList.add(
                    new CongViec(
                            cursor.getLong(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getLong(3),
                            cursor.getString(4),
                            cursor.getInt(5),
                            cursor.getInt(6)
                    ));
        }
        cursor.close();
        mCongViecList.addAll(congViecList);

        int count = countCongViec();
        Toast.makeText(CongViecActivity.this, "Hiện tại có tất cả " + count + " công việc trong trong tháng 9", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        gridView = findViewById(R.id.gridView);
        edtKeyword = findViewById(R.id.edtKeyword);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.general_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
        final CongViec congViec;
        Intent intent;
        switch (item.getItemId()) {
            case R.id.detail:
                congViec = congViecList.get(position);
                intent = new Intent(CongViecActivity.this, ChiTietCongViecActivity.class);
                intent.putExtra(ConstClass.INTENT_ID_CONGVIEC, congViec.getId());
                startActivity(intent);
                break;
            case R.id.update:
                congViec = congViecList.get(position);
                intent = new Intent(CongViecActivity.this, SuaCongViecActivity.class);
                intent.putExtra(ConstClass.INTENT_ID_CONGVIEC, congViec.getId());
                startActivity(intent);
                break;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(CongViecActivity.this);
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
                        AlarmHelper.deleteAlarm(CongViecActivity.this, congViecList.get(position));
                        db.QueryData("DELETE FROM CongViec where id = " + congViecList.get(position).getId());
                        congViecList.clear();
                        Cursor cursor = db.GetData("SELECT * FROM CongViec");
                        while (cursor.moveToNext()) {
                            congViecList.add(
                                    new CongViec(
                                            cursor.getLong(0),
                                            cursor.getString(1),
                                            cursor.getString(2),
                                            cursor.getLong(3),
                                            cursor.getString(4),
                                            cursor.getInt(5),
                                            cursor.getInt(6)
                                    ));
                        }
                        adapter.notifyDataSetChanged();

                        int count = countCongViec();
                        Toast.makeText(CongViecActivity.this, "Hiện tại có tất cả " + count + " công việc", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = db.GetData("SELECT * FROM CongViec");
        congViecList.clear();
        while (cursor.moveToNext()) {
            congViecList.add(
                    new CongViec(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getLong(3),
                            cursor.getString(4),
                            cursor.getInt(5),
                            cursor.getInt(6)
                    ));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private int countCongViec() {
        String query = "select  count (id) as count from CongViec where ThoiGian >= 1535785200000 and ThoiGian <= 1538290800000";
        Cursor cursor = db.GetData(query);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
}
