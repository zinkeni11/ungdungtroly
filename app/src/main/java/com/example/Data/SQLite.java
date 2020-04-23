package com.example.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {

    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    // khong tra ve du lieu
    public void QueryData(String sql) { // insert into // update tbl set
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    // tra ve du lieu
    public Cursor GetData(String sql) { // sql = select * from tbl...
        SQLiteDatabase database = getWritableDatabase();
        return database.rawQuery(sql, null);
    }

    // return id
    public long Insert(String table, ContentValues values) {
        SQLiteDatabase database = getWritableDatabase();
        return database.insert(table, null, values);
    }

    // return the number of rows affected
    public int Update(String table, ContentValues values, long id) {
        SQLiteDatabase database = getWritableDatabase();
        return database.update(table, values, "id = " + id, null);
    }
}