package com.example.sportsnotes;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
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

    private void addGymToLayout(String name, String description, String address, String contact, String website, int imageResId) {

        addSpace();

        // Добавляем изображение спортзала перед названием
        ImageView gymImageView = new ImageView(this);
        gymImageView.setImageResource(imageResId);  // Используем переданный ресурс изображения
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                600 // Увеличиваем размер изображения
        );
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(0, 24, 0, 24);  // Добавляем отступы сверху и снизу
        gymImageView.setLayoutParams(layoutParams);
        gymsContainer.addView(gymImageView);

        addSpace();

        TextView gymNameTextView = new TextView(this);
        gymNameTextView.setText(name);
        gymNameTextView.setTextSize(25);
        gymNameTextView.setTextColor(getResources().getColor(android.R.color.white));
        gymNameTextView.setGravity(Gravity.CENTER);
        gymsContainer.addView(gymNameTextView);

        addSpace();

        TextView gymDescriptionTextView = new TextView(this);
        gymDescriptionTextView.setText(description);
        gymDescriptionTextView.setTextSize(20);
        gymDescriptionTextView.setTextColor(getResources().getColor(android.R.color.white));
        gymsContainer.addView(gymDescriptionTextView);

        addSpace();

        TextView gymAddressTextView = new TextView(this);
        gymAddressTextView.setText("Адрес: " + address);
        gymAddressTextView.setTextSize(18);
        gymAddressTextView.setTextColor(getResources().getColor(android.R.color.white));
        gymsContainer.addView(gymAddressTextView);

        addSpace();

        TextView gymContactTextView = new TextView(this);
        gymContactTextView.setText("Контакт: " + contact);
        gymContactTextView.setTextSize(18);
        gymContactTextView.setTextColor(getResources().getColor(android.R.color.white));
        gymsContainer.addView(gymContactTextView);

        addSpace();

        TextView gymWebsiteTextView = new TextView(this);
        gymWebsiteTextView.setText("Веб-сайт: " + website);
        gymWebsiteTextView.setTextSize(18);
        gymWebsiteTextView.setTextColor(getResources().getColor(android.R.color.white));
        gymsContainer.addView(gymWebsiteTextView);

        addSpace();
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

        int[] imageResources = {
                R.drawable.images1,
                R.drawable.images2,
                R.drawable.images3,
                R.drawable.images4,
                R.drawable.images5,
        };

        int i = 0;
        while (cursor.moveToNext()) {
            String gymName = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GYM_NAME));
            String gymDescription = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GYM_DESCRIPTION));
            String gymAddress = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GYM_ADDRESS));
            String gymContact = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GYM_CONTACT));
            String gymWebsite = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_GYM_WEBSITE));

            // Передаем соответствующее изображение для каждого спортзала
            addGymToLayout(gymName, gymDescription, gymAddress, gymContact, gymWebsite, imageResources[i % imageResources.length]);
            i++;
        }

        cursor.close();
    }

    private void addSpace() {
        Space space = new Space(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 16);
        space.setLayoutParams(params);
        gymsContainer.addView(space);
    }
}
