package com.example.sportsnotes;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Gym extends AppCompatActivity {

    private Database dbHelper;
    private LinearLayout gymsContainer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gym);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gymsContainer = findViewById(R.id.gymsContainer);
        dbHelper = new Database(this);

        displayGyms();
    }

    private void displayGyms() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Database.COLUMN_GYM_NAME,
                Database.COLUMN_GYM_DESCRIPTION,
                Database.COLUMN_GYM_ADDRESS,
                Database.COLUMN_GYM_CONTACT,
                Database.COLUMN_GYM_WEBSITE
        };

        Cursor cursor = db.query(
                Database.TABLE_GYMS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String gymName = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GYM_NAME));
            String gymDescription = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GYM_DESCRIPTION));
            String gymAddress = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GYM_ADDRESS));
            String gymContact = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GYM_CONTACT));
            String gymWebsite = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GYM_WEBSITE));

            addGymToLayout(gymName, gymDescription, gymAddress, gymContact, gymWebsite);
        }

        cursor.close();
    }

    private void addGymToLayout(String name, String description, String address, String contact, String website) {
        TextView gymNameTextView = new TextView(this);
        gymNameTextView.setText("Название: " + name);
        gymNameTextView.setTextSize(18);
        gymNameTextView.setTextColor(getResources().getColor(android.R.color.white));
        gymsContainer.addView(gymNameTextView);

        TextView gymDescriptionTextView = new TextView(this);
        gymDescriptionTextView.setText("Описание: " + description);
        gymDescriptionTextView.setTextSize(16);
        gymDescriptionTextView.setTextColor(getResources().getColor(android.R.color.white));
        gymsContainer.addView(gymDescriptionTextView);

        TextView gymAddressTextView = new TextView(this);
        gymAddressTextView.setText("Адрес: " + address);
        gymAddressTextView.setTextSize(16);
        gymAddressTextView.setTextColor(getResources().getColor(android.R.color.white));
        gymsContainer.addView(gymAddressTextView);

        TextView gymContactTextView = new TextView(this);
        gymContactTextView.setText("Контакт: " + contact);
        gymContactTextView.setTextSize(16);
        gymContactTextView.setTextColor(getResources().getColor(android.R.color.white));
        gymsContainer.addView(gymContactTextView);

        TextView gymWebsiteTextView = new TextView(this);
        gymWebsiteTextView.setText("Веб-сайт: " + website);
        gymWebsiteTextView.setTextSize(16);
        gymWebsiteTextView.setTextColor(getResources().getColor(android.R.color.white));
        gymsContainer.addView(gymWebsiteTextView);

        // Добавляем разделитель между записями
        TextView separator = new TextView(this);
        separator.setText("------------------------------");
        separator.setTextSize(16);
        separator.setTextColor(getResources().getColor(android.R.color.black));
        gymsContainer.addView(separator);
    }
}