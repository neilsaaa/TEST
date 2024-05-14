package com.example.expensify;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.widget.ImageButton;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText editTextDate;
    EditText editTextExpenseName;
    EditText editTextExpenseAmount;

    EditText editTextAddress;
    Spinner spinnerExpenseCategory;
    SwitchCompat switchExpensePaid;
    Button buttonAddExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Ánh xạ các thành phần trong layout
        editTextDate = findViewById(R.id.editTextDate);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextExpenseName = findViewById(R.id.editTextExpenseName);
        editTextExpenseAmount = findViewById(R.id.editTextExpenseAmount);
        spinnerExpenseCategory = findViewById(R.id.spinnerExpenseCategory);
        switchExpensePaid = findViewById(R.id.switchExpensePaid);
        buttonAddExpense = findViewById(R.id.buttonAddExpense);
        // Đặt sự kiện click cho nút "Thêm"
        // 4. Kiểm tra thông tin và lưu chi tiêu
        buttonAddExpense.setOnClickListener(view -> {
            String date = editTextDate.getText().toString();
            String expenseName = editTextExpenseName.getText().toString();
            String address = editTextAddress.getText().toString();
            String expenseAmount = editTextExpenseAmount.getText().toString();
            String expenseCategory = spinnerExpenseCategory.getSelectedItem().toString();
            boolean expensePaid = switchExpensePaid.isChecked();

            if (date.isEmpty() || expenseName.isEmpty() || address.isEmpty() || expenseAmount.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                Expense expense = new Expense();
                expense.date = date;
                expense.name = expenseName;
                expense.address = address;
                expense.amount = expenseAmount;
                expense.category = expenseCategory;
                expense.isPaid = expensePaid;

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "expense").build();
                new Thread(() -> {
                    db.expenseDao().insert(expense);
                   }).start();


                Toast.makeText(MainActivity.this, "Đã thêm khoản chi phí: " + expenseName + " tại " + address, Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton calendarButton = findViewById(R.id.imageButtonDate);
        calendarButton.setOnClickListener(view -> {
            // Lấy ngày hiện tại
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            // Tạo một DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        // Đặt ngày được chọn vào EditText
                        Toast.makeText(MainActivity.this,"Đã chọn ngày được", Toast.LENGTH_SHORT).show();

                        editTextDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }
}
