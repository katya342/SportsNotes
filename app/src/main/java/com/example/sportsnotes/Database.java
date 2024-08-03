package com.example.sportsnotes;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "sportsnotes.db";
    private static final int DATABASE_VERSION = 10;

    // Таблица users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_SPORT_TYPE = "sport_type"; // Новое поле
    public static final String COLUMN_DAYS_PER_WEEK = "days_per_week"; // Новое поле
    public static final String COLUMN_IMAGE_PATH = "image_path";


    // Таблица workouts
    public static final String TABLE_WORKOUTS = "workouts";
    public static final String COLUMN_WORKOUT_USER_ID = "user_id"; // Переименованный идентификатор пользователя
    public static final String COLUMN_MONDAY_NOTES = "monday_notes";
    public static final String COLUMN_TUESDAY_NOTES = "tuesday_notes";
    public static final String COLUMN_WEDNESDAY_NOTES = "wednesday_notes";
    public static final String COLUMN_THURSDAY_NOTES = "thursday_notes";
    public static final String COLUMN_FRIDAY_NOTES = "friday_notes";
    public static final String COLUMN_SATURDAY_NOTES = "saturday_notes";
    public static final String COLUMN_SUNDAY_NOTES = "sunday_notes";

    // Таблица recommendations
    public static final String TABLE_RECOMMENDATIONS = "recommendations";
    public static final String COLUMN_RECOMMENDATION_ID = "id";
    public static final String COLUMN_RECOMMENDATION_TEXT = "text";
    // Таблица user details

    public static final String TABLE_USER_DETAILS = "user_details";
    public static final String COLUMN_USER_ID = "user_id"; // Идентификатор пользователя
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_ACTIVITY_LEVEL = "activity_level";
    public static final String COLUMN_DIET_TYPE = "diet_type";
    public static final String COLUMN_GOAL = "goal";
    public static final String COLUMN_RECOMMENDED_CALORIES = "recommended_calories";

    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LOGIN + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_SPORT_TYPE + " TEXT, " + // Новое поле
                    COLUMN_DAYS_PER_WEEK + " TEXT, " + // Новое поле
                    COLUMN_IMAGE_PATH + " TEXT" +
                    ");";


    private static final String SQL_CREATE_WORKOUTS_TABLE =
            "CREATE TABLE " + TABLE_WORKOUTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_WORKOUT_USER_ID + " INTEGER, " +
                    COLUMN_MONDAY_NOTES + " TEXT, " +
                    COLUMN_TUESDAY_NOTES + " TEXT, " +
                    COLUMN_WEDNESDAY_NOTES + " TEXT, " +
                    COLUMN_THURSDAY_NOTES + " TEXT, " +
                    COLUMN_FRIDAY_NOTES + " TEXT, " +
                    COLUMN_SATURDAY_NOTES + " TEXT, " +
                    COLUMN_SUNDAY_NOTES + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_WORKOUT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")" +
                    ");";

    // Запрос на создание таблицы recommendations
    private static final String SQL_CREATE_RECOMMENDATIONS_TABLE =
            "CREATE TABLE " + TABLE_RECOMMENDATIONS + " (" +
                    COLUMN_RECOMMENDATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECOMMENDATION_TEXT + " TEXT" +
                    ");";


    private static final String SQL_CREATE_USER_DETAILS_TABLE =
            "CREATE TABLE " + TABLE_USER_DETAILS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_WEIGHT + " REAL, " +
                    COLUMN_HEIGHT + " REAL, " +
                    COLUMN_AGE + " INTEGER, " +
                    COLUMN_ACTIVITY_LEVEL + " TEXT, " +
                    COLUMN_DIET_TYPE + " TEXT, " +
                    COLUMN_GOAL + " TEXT, " +
                    COLUMN_RECOMMENDED_CALORIES + " REAL, " +
                    "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + " (" + COLUMN_ID + ") ON DELETE CASCADE" +
                    ");";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // Инициализация контекста
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_WORKOUTS_TABLE);
        db.execSQL(SQL_CREATE_RECOMMENDATIONS_TABLE);
        db.execSQL(SQL_CREATE_USER_DETAILS_TABLE);
        // Вставка рекомендаций в таблицу
        insertRecommendations(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOMMENDATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);

        onCreate(db);
    }

    // Метод для вставки рекомендаций в таблицу
    private void insertRecommendations(SQLiteDatabase db) {
        String[] recommendations = {
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

        for (String recommendation : recommendations) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_RECOMMENDATION_TEXT, recommendation);
            db.insert(TABLE_RECOMMENDATIONS, null, values);
        }
    }

    public void saveUserDetails(int userId, double weight, double height, int age, String activityLevel, String dietType, String goal, double recommendedCalories) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_HEIGHT, height);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_ACTIVITY_LEVEL, activityLevel);
        values.put(COLUMN_DIET_TYPE, dietType);
        values.put(COLUMN_GOAL, goal);
        values.put(COLUMN_RECOMMENDED_CALORIES, recommendedCalories);

        // Проверка, существует ли уже запись для данного пользователя
        int rowsAffected = db.update(TABLE_USER_DETAILS, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        // Если запись не обновилась, то создаем новую запись
        if (rowsAffected == 0) {
            db.insert(TABLE_USER_DETAILS, null, values);
        }

        db.close();
    }

    // Методы для работы с таблицей users
    public long addUser(String login, String email, String password, String sportType, int daysPerWeek) {
        Log.d("Database", "Values being inserted: " +
                "Login: " + login + ", " +
                "Email: " + email + ", " +
                "Password: " + password + ", " +
                "Sport Type: " + sportType + ", " +
                "Days per Week: " + daysPerWeek);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        // Установка значений sportType и daysPerWeek только если они не пустые
        values.put(COLUMN_SPORT_TYPE, sportType != null && !sportType.isEmpty() ? sportType : (String) null);
        values.put(COLUMN_DAYS_PER_WEEK, daysPerWeek > 0 ? String.valueOf(daysPerWeek) : null);

        long id = db.insert(TABLE_USERS, null, values);
        if (id == -1) {
            Log.e("Database", "Ошибка при вставке данных в таблицу users");
        } else {
            saveUserIdToPreferences(id);
        }
        db.close();
        return id;
    }


    private void saveUserIdToPreferences(long userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("user_id", userId);
        editor.apply();
    }

    private boolean isLoginExists(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_LOGIN + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{login});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, new String[]{COLUMN_ID + " AS _id", COLUMN_LOGIN, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_SPORT_TYPE, COLUMN_DAYS_PER_WEEK}, null, null, null, null, null);
    }

    public Cursor getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS,
                new String[]{COLUMN_ID, COLUMN_LOGIN, COLUMN_EMAIL, COLUMN_IMAGE_PATH},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);
    }

    public boolean loginUser(String login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_LOGIN + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{login, password});
        boolean loginSuccessful = cursor.moveToFirst();
        cursor.close();
        return loginSuccessful;
    }

    public int getUserId(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_LOGIN + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{login});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        return userId;
    }

    // Метод для обновления информации о пользователе
    public void updateUser(int userId, String sportType, String daysPerWeek) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SPORT_TYPE, sportType);
        values.put(COLUMN_DAYS_PER_WEEK, daysPerWeek);
        db.update(TABLE_USERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
    public void updateUserViaImage(int userId, String login, String email, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGIN, login);
        values.put(COLUMN_EMAIL, email);
        if (imagePath != null) {
            values.put(COLUMN_IMAGE_PATH, imagePath);
        }

        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(userId)};

        db.update(TABLE_USERS, values, whereClause, whereArgs);
    }
    public long addWorkout(int userId, String mondayNotes, String tuesdayNotes, String wednesdayNotes,
                           String thursdayNotes, String fridayNotes, String saturdayNotes, String sundayNotes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKOUT_USER_ID, userId);
        values.put(COLUMN_MONDAY_NOTES, mondayNotes);
        values.put(COLUMN_TUESDAY_NOTES, tuesdayNotes);
        values.put(COLUMN_WEDNESDAY_NOTES, wednesdayNotes);
        values.put(COLUMN_THURSDAY_NOTES, thursdayNotes);
        values.put(COLUMN_FRIDAY_NOTES, fridayNotes);
        values.put(COLUMN_SATURDAY_NOTES, saturdayNotes);
        values.put(COLUMN_SUNDAY_NOTES, sundayNotes);

        long id = db.insert(TABLE_WORKOUTS, null, values);
        db.close();
        return id;
    }


    public Cursor getAllWorkoutsCursor(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                COLUMN_ID + " AS _id, " +
                COLUMN_WORKOUT_USER_ID + ", " +
                COLUMN_MONDAY_NOTES + ", " +
                COLUMN_TUESDAY_NOTES + ", " +
                COLUMN_WEDNESDAY_NOTES + ", " +
                COLUMN_THURSDAY_NOTES + ", " +
                COLUMN_FRIDAY_NOTES + ", " +
                COLUMN_SATURDAY_NOTES + ", " +
                COLUMN_SUNDAY_NOTES +
                " FROM " + TABLE_WORKOUTS +
                " WHERE " + COLUMN_WORKOUT_USER_ID + "=?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }


    // Методы для работы с таблицей recommendations
    public List<String> getAllRecommendations() {
        List<String> recommendations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECOMMENDATIONS, new String[]{COLUMN_RECOMMENDATION_TEXT},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                recommendations.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECOMMENDATION_TEXT)));
            }
            cursor.close();
        }
        return recommendations;
    }
}
