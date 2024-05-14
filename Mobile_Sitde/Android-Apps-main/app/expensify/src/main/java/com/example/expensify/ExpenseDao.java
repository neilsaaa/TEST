package com.example.expensify;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface ExpenseDao {
    @Insert
    void insert(Expense expense);
}
