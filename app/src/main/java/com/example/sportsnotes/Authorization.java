package com.example.sportsnotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Authorization extends AppCompatActivity {

    private EditText loginEditText, emailEditText, passwordEditText;
    private Database databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authorization);

        loginEditText = findViewById(R.id.login);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.register);

        databaseHelper = new Database(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                long userId = validateUser(login, email, password);
                if (userId != -1) {
                    // Save user ID to shared preferences
                    saveUserIdToPreferences(userId);

                    Toast.makeText(Authorization.this, "Добро пожаловать", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Authorization.this, MarkSelection.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Authorization.this, "Неверно введены данные, попробуйте еще раз!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private long validateUser(String login, String email, String password) {
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + Database.TABLE_USERS + " WHERE " + Database.COLUMN_LOGIN + "=? AND " + Database.COLUMN_EMAIL + "=? AND " + Database.COLUMN_PASSWORD + "=?",
                new String[]{login, email, password});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") long userId = cursor.getLong(cursor.getColumnIndex(Database.COLUMN_ID));
            cursor.close();
            return userId;
        } else {
            cursor.close();
            return -1;
        }
    }

    private void saveUserIdToPreferences(long userId) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("user_id", userId);
        editor.apply();
    }
}
