package com.example.sportsnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Registration extends AppCompatActivity {

    private EditText loginEditText, emailEditText, passwordEditText;
    private Database databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginEditText = findViewById(R.id.login);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.register);

        databaseHelper = new Database(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String sportType = getIntent().getStringExtra("sport_type");
                int daysPerWeek = getIntent().getIntExtra("days_per_week", -1); // Используйте -1 для проверки

                // Если sportType и daysPerWeek не были переданы, установите их по умолчанию
                if (sportType == null) sportType = "";
                if (daysPerWeek == -1) daysPerWeek = 0;

                // Проверка полей
                if (!login.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    long id = databaseHelper.addUser(login, email, password, sportType, daysPerWeek);
                    if (id > 0) {
                        Toast.makeText(Registration.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registration.this, ChoiceOfSport.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Registration.this, "Ошибка при регистрации", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Registration.this, "Пожалуйста, заполните все данные", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}


