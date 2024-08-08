package com.example.sportsnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyWorkout extends AppCompatActivity {

    EditText etMonday, etTuesday, etWednesday, etThursday, etFriday, etSaturday, etSunday;
    Button btnSave;
    Database dbHelper;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workout);

        dbHelper = new Database(this);

        etMonday = findViewById(R.id.et_monday);
        etTuesday = findViewById(R.id.et_tuesday);
        etWednesday = findViewById(R.id.et_wednesday);
        etThursday = findViewById(R.id.et_thursday);
        etFriday = findViewById(R.id.et_friday);
        etSaturday = findViewById(R.id.et_saturday);
        etSunday = findViewById(R.id.et_sunday);

        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWorkoutNotes();
            }
        });
    }

    private void saveWorkoutNotes() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = (int) sharedPreferences.getLong("user_id", -1);

        String mondayNotes = etMonday.getText().toString().trim();
        String tuesdayNotes = etTuesday.getText().toString().trim();
        String wednesdayNotes = etWednesday.getText().toString().trim();
        String thursdayNotes = etThursday.getText().toString().trim();
        String fridayNotes = etFriday.getText().toString().trim();
        String saturdayNotes = etSaturday.getText().toString().trim();
        String sundayNotes = etSunday.getText().toString().trim();

        // Проверка, что хотя бы одно поле не пустое
        if (mondayNotes.isEmpty() && tuesdayNotes.isEmpty() && wednesdayNotes.isEmpty() &&
                thursdayNotes.isEmpty() && fridayNotes.isEmpty() && saturdayNotes.isEmpty() &&
                sundayNotes.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните хотя бы одно поле", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.addWorkout(userId, mondayNotes, tuesdayNotes, wednesdayNotes,
                thursdayNotes, fridayNotes, saturdayNotes, sundayNotes);

        if (result != -1) {
            Toast.makeText(this, "Запись сохранена", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyWorkout.this, HistoryWorkout.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Не удалось сохранить запись", Toast.LENGTH_SHORT).show();
        }
    }

}
