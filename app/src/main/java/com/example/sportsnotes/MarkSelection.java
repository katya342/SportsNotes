package com.example.sportsnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
        dbHelper = new Database(this);
// Запуск таймера для показа уведомления через 10 секунд
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showUserInputDialog();
            }
        }, 10000);
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
    private void showUserInputDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_user_input, null);

        Spinner activityLevelSpinner = dialogView.findViewById(R.id.spActivityLevel);
        ArrayAdapter<CharSequence> activityLevelAdapter = ArrayAdapter.createFromResource(this,
                R.array.activity_level_array, android.R.layout.simple_spinner_item);
        activityLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityLevelSpinner.setAdapter(activityLevelAdapter);

        Spinner dietTypeSpinner = dialogView.findViewById(R.id.spDietType);
        ArrayAdapter<CharSequence> dietTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.diet_type_array, android.R.layout.simple_spinner_item);
        dietTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dietTypeSpinner.setAdapter(dietTypeAdapter);

        Spinner goalSpinner = dialogView.findViewById(R.id.spGoal);
        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this,
                R.array.goal_array, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(goalAdapter);
        new AlertDialog.Builder(this)
                .setTitle("Введите свои данные")
                .setView(dialogView)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    EditText userIdEditText = dialogView.findViewById(R.id.etUserId);
                    EditText weightEditText = dialogView.findViewById(R.id.etWeight);
                    EditText heightEditText = dialogView.findViewById(R.id.etHeight);
                    EditText ageEditText = dialogView.findViewById(R.id.etAge);

                    int userId = Integer.parseInt(userIdEditText.getText().toString());
                    double weight = Double.parseDouble(weightEditText.getText().toString());
                    double height = Double.parseDouble(heightEditText.getText().toString());
                    int age = Integer.parseInt(ageEditText.getText().toString());
                    String activityLevel = activityLevelSpinner.getSelectedItem().toString().toLowerCase();
                    String dietType = dietTypeSpinner.getSelectedItem().toString().toLowerCase();
                    String goal = goalSpinner.getSelectedItem().toString().toLowerCase();
                    double recommendedCalories = calculateRecommendedCalories(weight, height, age, activityLevel, dietType, goal);

                    dbHelper.saveUserDetails(userId, weight, height, age, activityLevel, dietType, goal, recommendedCalories);

                })
                .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }
    private double calculateRecommendedCalories(double weight, double height, int age, String activityLevel, String dietType, String goal) {
        // Определение базального уровня метаболизма (BMR) с использованием формулы Харриса-Бенедикта
        // Формула для мужчин: BMR = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        // Формула для женщин: BMR = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        // Для примера предположим, что пол определен отдельно, и мы используем формулу для мужчин.

        double bmr;
        // Вы можете заменить эту логику на учет пола и соответствующую формулу
        bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);

        // Определение коэффициента активности
        double activityFactor;
        switch (activityLevel.toLowerCase()) {
            case "низкий":
                activityFactor = 1.2;
                break;
            case "средний":
                activityFactor = 1.55;
                break;
            case "высокий":
                activityFactor = 1.9;
                break;
            default:
                activityFactor = 1.2; // Значение по умолчанию
                break;
        }

        // Расчет общего количества калорий с учетом уровня активности
        double totalCalories = bmr * activityFactor;

        // Корректировка калорий в зависимости от цели
        switch (goal.toLowerCase()) {
            case "потеря веса":
                totalCalories -= 500; // Уменьшение на 500 калорий для потери веса
                break;
            case "набор массы":
                totalCalories += 500; // Увеличение на 500 калорий для набора массы
                break;
            case "поддержание веса":
                // Нет изменений
                break;
            default:
                break;
        }

        // Введение дополнительных корректировок в зависимости от типа диеты
        // Например, можно изменить общее количество калорий в зависимости от диеты
        // Здесь для простоты нет изменений

        return totalCalories;
    }

}
