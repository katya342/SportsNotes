package com.example.sportsnotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class History extends AppCompatActivity {

    private LinearLayout workoutContainer;
    private Database db;
    private long userId; // Получаем ID пользователя из Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        workoutContainer = findViewById(R.id.workoutContainer);
        db = new Database(this);

        // Получаем ID пользователя из Intent
        Intent intent = getIntent();
        userId = intent.getLongExtra("userId", -1);

        if (userId != -1) {
            loadWorkoutData();
        } else {
            Toast.makeText(this, "Ошибка ID пользователя", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadWorkoutData() {
        Cursor cursor = db.getWorkoutDataForUser(userId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Пример получения данных из курсора с проверкой наличия столбцов
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                int trainingCount = cursor.getInt(cursor.getColumnIndexOrThrow("training_count"));
                double trainingTime = cursor.getDouble(cursor.getColumnIndexOrThrow("training_time"));
                double breakfastCalories = cursor.getDouble(cursor.getColumnIndexOrThrow("breakfast_calories"));
                double lunchCalories = cursor.getDouble(cursor.getColumnIndexOrThrow("lunch_calories"));
                double dinnerCalories = cursor.getDouble(cursor.getColumnIndexOrThrow("dinner_calories"));
                double weight = cursor.getDouble(cursor.getColumnIndexOrThrow("weight"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

                // Создаем и добавляем TextView для отображения данных
                TextView workoutEntry = new TextView(this);
                workoutEntry.setText("Дата: " + date + "\n" +
                        "Кол-во тренировок: " + trainingCount + "\n" +
                        "Время тренировки: " + trainingTime + "\n" +
                        "Калории (завтрак): " + breakfastCalories + "\n" +
                        "Калории (обед): " + lunchCalories + "\n" +
                        "Калории (ужин): " + dinnerCalories + "\n" +
                        "Вес: " + weight);

                workoutEntry.setPadding(16, 16, 16, 16);
                workoutEntry.setBackgroundColor(Color.WHITE);
                workoutContainer.addView(workoutEntry);

                // При необходимости добавьте код для отображения изображения
                if (image != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(bitmap);
                    workoutContainer.addView(imageView);
                }

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            TextView noDataText = new TextView(this);
            noDataText.setText("Нет данных для отображения");
            //noDataText.setPadding(16, 16, 16, 16);
            workoutContainer.addView(noDataText);
        }
    }
}
