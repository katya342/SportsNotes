package com.example.sportsnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MarkSelection extends AppCompatActivity {
    private Database dbHelper;
    private ImageView user_icon;
    private String[] recommendations = {
            // Рекомендации по питанию и весу
            "Попробуйте добавить больше овощей в ваш рацион.",
            "Употребляйте достаточное количество белка для восстановления мышц после тренировок.",
            "Выбирайте полезные углеводы, такие как овес и киноа, для долгосрочной энергии.",
            "Не забывайте пить достаточное количество воды в течение дня.",
            "Избегайте употребления большого количества сахара и сладких напитков.",
            "Добавляйте орехи и семена в ваш рацион для получения полезных жиров.",
            "Питайтесь разнообразно, чтобы получить все необходимые витамины и минералы.",
            "Избегайте фастфуда и готовьте пищу дома, чтобы контролировать ингредиенты.",
            "Уменьшите потребление соли, чтобы снизить риск высокого давления.",
            "Регулярно ешьте рыбу для получения омега-3 жирных кислот.",
            "Следите за размером порций, чтобы избежать переедания.",
            "Ешьте медленно и осознанно, чтобы лучше контролировать аппетит.",
            "Пейте зелёный чай для улучшения метаболизма.",
            "Старайтесь избегать переработанных продуктов.",
            "Планируйте своё питание заранее, чтобы избежать нездоровых перекусов.",
            "Регулярно взвешивайтесь и записывайте результаты.",
            "Поддерживайте дефицит калорий для похудения.",
            "Увеличьте количество белка в рационе для поддержания мышечной массы.",
            "Избегайте обработанных продуктов и сахара.",
            "Пейте больше воды, чтобы поддерживать метаболизм.",
            "Регулярно занимайтесь физическими упражнениями.",
            "Сократите потребление алкоголя.",
            "Старайтесь есть меньшими порциями, но чаще.",
            "Не пропускайте завтраки, они запускают метаболизм.",
            "Следите за качеством и количеством сна.",
            "Используйте приложения для отслеживания питания и активности.",
            "Найдите поддержку в лице друзей или семьи.",
            "Старайтесь уменьшить стресс, так как он может влиять на вес.",
            "Избегайте поздних ужинов.",
            "Готовьте пищу самостоятельно, чтобы контролировать ингредиенты."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mark_selection);

//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        int userId = (int) sharedPreferences.getLong("user_id", -1);
//        dbHelper = new Database(this);
//
//        user_icon = findViewById(R.id.user_icon);
//
//        String profilePictureUriString = sharedPreferences.getString("profile_picture_uri", null);
//        if (profilePictureUriString != null) {
//            Uri profilePictureUri = Uri.parse(profilePictureUriString);
//            user_icon.setImageURI(profilePictureUri);
//        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button otButton = findViewById(R.id.worcout_ot);
        otButton.setOnClickListener(v -> {
            Intent newIntent = new Intent(MarkSelection.this, Profile.class);
            startActivity(newIntent);
        });

        Button myButton = findViewById(R.id.worcout_my);
        myButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences2 = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userIds = (int) sharedPreferences2.getLong("user_id", -1);
            if (userIds == -1) {
                // Handle the case where the user ID is not found
                Toast.makeText(MarkSelection.this, "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent newIntent = new Intent(MarkSelection.this, HistoryWorkout.class);
            newIntent.putExtra("USER_ID", userIds);
            startActivity(newIntent);
        });

        Button crButton = findViewById(R.id.worcout_create);
        crButton.setOnClickListener(v -> {
            Intent newIntent = new Intent(MarkSelection.this, MyWorkout.class);
            startActivity(newIntent);
        });

        Button hisButton = findViewById(R.id.history);
        hisButton.setOnClickListener(v -> {
            Intent newIntent = new Intent(MarkSelection.this, History.class);
            startActivity(newIntent);
        });

        Button eatButton = findViewById(R.id.show_recommendation_button);
        eatButton.setOnClickListener(v -> {
            // Получаем два случайных индекса
            Random random = new Random();
            int randomIndex1 = random.nextInt(recommendations.length);
            int randomIndex2 = random.nextInt(recommendations.length);

            // Получаем случайные рекомендации
            String randomRecommendation1 = recommendations[randomIndex1];
            String randomRecommendation2 = recommendations[randomIndex2];

            // Создаем Intent для перехода на Eat activity и передаем рекомендации
            Intent newIntent = new Intent(MarkSelection.this, Eat.class);
            newIntent.putExtra("RECOMMENDATION1", randomRecommendation1);
            newIntent.putExtra("RECOMMENDATION2", randomRecommendation2);
            startActivity(newIntent);
        });

        Button gymButton = findViewById(R.id.gym);
        gymButton.setOnClickListener(v -> {
            Intent newIntent = new Intent(MarkSelection.this, Gym.class);
            startActivity(newIntent);
        });
        ImageView userIcon = findViewById(R.id.user_icon);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MarkSelection.this, UserAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
