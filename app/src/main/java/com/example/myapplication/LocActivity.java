package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Data.SQLite;
import com.example.adapter.CongViecListViewAdapter;
import com.example.model.CongViec;

import java.util.ArrayList;
import java.util.List;

public class LocActivity extends AppCompatActivity {
    private EditText edtKeyword;
    private Spinner spnFilterBy;
    private Button btnFilter;
    GridView gridView;
    CongViecListViewAdapter adapter;

    private String keyword = null;
    private int idFilterBy = 0;
    static SQLite db;
    Cursor cursor;
    private List<CongViec> congViecList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filler);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initView();
        db = new SQLite(this, ConstClass.DATABASE_NAME, null, ConstClass.DATABASE_VERSION);
        congViecList = new ArrayList<>();

        registerForContextMenu(gridView);

        adapter = new CongViecListViewAdapter(this, congViecList, db);
        gridView.setAdapter(adapter);

        String[] arr = getResources().getStringArray(R.array.testArray);
        ArrayAdapter<String> adapterSpn = new ArrayAdapter<>
                (
                        this,
                        android.R.layout.simple_spinner_item,arr
                );

        spnFilterBy.setAdapter(adapterSpn);
        //thiết lập sự kiện chọn phần tử cho Spinner
        spnFilterBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idFilterBy = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = edtKeyword.getText().toString();
                String queryClause = generateQueryClause(keyword, idFilterBy);
                cursor = db.GetData(queryClause);

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
                            )
                    );
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        edtKeyword = findViewById(R.id.edtKeyword);
        spnFilterBy = findViewById(R.id.spnFilterBy);
        btnFilter = findViewById(R.id.btnFilter);
        gridView = findViewById(R.id.gridView);
    }

    private String generateQueryClause(String keyword, int idFilterBy) {
        String queryClause = "SELECT * FROM CongViec where TenCV LIKE '%" + keyword + "%'";
        switch (idFilterBy) {
            case 0:
                queryClause += " ORDER BY TenCV ASC";
                break;
            case 1:
                queryClause += " ORDER BY TenCV DESC";
                break;
            case 2:
                queryClause += " and ThoiGian >= " + System.currentTimeMillis();
                break;
            case 3:
                queryClause += " and ThoiGian < " + System.currentTimeMillis();
                break;
            default:
                break;
        }
        return queryClause;
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
