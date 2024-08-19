package com.example.sportsnotes;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private Database db;
    private LinearLayout workoutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        workoutContainer = findViewById(R.id.workoutContainer);

        db = new Database(this);

        ArrayList<String> profiles = db.getAllProfiles();

        if (profiles != null) {
            for (String profile : profiles) {
                // Добавляем тренировочные данные
                String[] profileLines = profile.split("\n"); // Разделяем данные по строкам

                for (String line : profileLines) {
                    TextView textView = new TextView(this);
                    textView.setText(line);
                    textView.setTextSize(16);
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                    textView.setPadding(16, 16, 16, 16); // Паддинг для данных

                    // Устанавливаем маргин для отступов между строками
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, 16); // Отступ снизу
                    textView.setLayoutParams(params);

                    workoutContainer.addView(textView);

                    // Добавляем отступ после каждого блока данных
                    LinearLayout.LayoutParams spaceParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 16); // Высота Space
                    Space space = new Space(this);
                    space.setLayoutParams(spaceParams);
                    workoutContainer.addView(space);
                }
            }
        }
    }
}
