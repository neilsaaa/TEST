package com.example.quiz.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Thêm một dòng mới vào cơ sở dữ liệu
    public void addData(String content, String options, String answer) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CONTENT, content);
        values.put(DBHelper.COLUMN_OPTIONS, options);
        values.put(DBHelper.COLUMN_ANSWER, answer);
        database.insert(DBHelper.TABLE_NAME, null, values);
    }

    // Lấy tất cả dữ liệu từ cơ sở dữ liệu
    public List<Question> getAllData() {
        List<Question> dataList = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CONTENT));
            String options = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_OPTIONS));
            String answer = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ANSWER));
            dataList.add(new Question(id, content, options, answer));
            cursor.moveToNext();
        }
        cursor.close();
        return dataList;
    }
}
