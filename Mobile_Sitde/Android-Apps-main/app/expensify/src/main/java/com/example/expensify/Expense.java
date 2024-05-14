package com.example.expensify;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.RoomDatabase;

// 1. Tạo một lớp Entity
@Entity
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;
    public String name;
    public String address;
    public String amount;
    public String category;
    public boolean isPaid;
}

