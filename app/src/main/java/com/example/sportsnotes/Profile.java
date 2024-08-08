package com.example.sportsnotes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity {

    private EditText editTextTrainingCount, editTextTrainingTime, editTextBreakfastCalories, editTextLunchCalories, editTextDinnerCalories, editTextWeight, editTextDate;
    private TextView textViewTotalCalories;
    private Button buttonCalculateCalories, buttonSaveProfile;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Найти поля ввода и текстовые поля
        editTextTrainingCount = findViewById(R.id.editTextTrainingCount);
        editTextTrainingTime = findViewById(R.id.editTextTrainingTime);
        editTextBreakfastCalories = findViewById(R.id.editTextBreakfastCalories);
        editTextLunchCalories = findViewById(R.id.editTextLunchCalories);
        editTextDinnerCalories = findViewById(R.id.editTextDinnerCalories);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextDate = findViewById(R.id.editTextDate);
        textViewTotalCalories = findViewById(R.id.textViewTotalCalories);
        buttonCalculateCalories = findViewById(R.id.buttonCalculateCalories);
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile);

        db = new Database(this);

        // Установить слушатель на кнопку расчета калорий
        buttonCalculateCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTotalCalories();
            }
        });

        // Установить слушатель на кнопку сохранения данных
        buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });
    }

    private void calculateTotalCalories() {
        double breakfastCalories = getCaloriesFromEditText(editTextBreakfastCalories);
        double lunchCalories = getCaloriesFromEditText(editTextLunchCalories);
        double dinnerCalories = getCaloriesFromEditText(editTextDinnerCalories);

        double totalCalories = breakfastCalories + lunchCalories + dinnerCalories;

        textViewTotalCalories.setText("Общее количество калорий: " + totalCalories);
    }

    private double getCaloriesFromEditText(EditText editText) {
        String caloriesText = editText.getText().toString();
        if (caloriesText.isEmpty()) {
            return 0;
        } else {
            return Double.parseDouble(caloriesText);
        }
    }

    private void saveProfileData() {
        int trainingCount = Integer.parseInt(editTextTrainingCount.getText().toString());
        double trainingTime = Double.parseDouble(editTextTrainingTime.getText().toString());
        double breakfastCalories = Double.parseDouble(editTextBreakfastCalories.getText().toString());
        double lunchCalories = Double.parseDouble(editTextLunchCalories.getText().toString());
        double dinnerCalories = Double.parseDouble(editTextDinnerCalories.getText().toString());
        double weights = Double.parseDouble(editTextWeight.getText().toString());
        String date = editTextDate.getText().toString();

        // Вставить данные в базу данных без изображения
        db.insertUserProfile(trainingCount, trainingTime, breakfastCalories, lunchCalories, dinnerCalories, weights, date);

        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
    }
}
