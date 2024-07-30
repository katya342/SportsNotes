// Eat.java
package com.example.sportsnotes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Random;

public class Eat extends AppCompatActivity {

    private TextView recommendationTextView1;
    private TextView recommendationTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat);

        recommendationTextView1 = findViewById(R.id.recommendation_text);
        recommendationTextView2 = findViewById(R.id.recommendation_text2);

        // Извлекаем рекомендации из базы данных
        Database dbHelper = new Database(this);
        List<String> recommendations = dbHelper.getAllRecommendations();

        // Получаем два случайных индекса
        Random random = new Random();
        int randomIndex1 = random.nextInt(recommendations.size());
        int randomIndex2 = random.nextInt(recommendations.size());

        // Получаем случайные рекомендации
        String randomRecommendation1 = recommendations.get(randomIndex1);
        String randomRecommendation2 = recommendations.get(randomIndex2);

        // Отображаем рекомендации
        recommendationTextView1.setText(randomRecommendation1);
        recommendationTextView2.setText(randomRecommendation2);
    }
}
