package com.example.sportsnotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Profile extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1;

    private EditText editTextBreakfastCalories, editTextLunchCalories, editTextDinnerCalories;
    private TextView textViewTotalCalories;
    private Button buttonCalculateCalories, buttonSave, buttonUploadPhoto;
    private EditText editTextTrainingCount, editTextTrainingTime, editTextWeight, editTextDate;
    private Bitmap selectedImage;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = new Database(this);

        editTextBreakfastCalories = findViewById(R.id.editTextBreakfastCalories);
        editTextLunchCalories = findViewById(R.id.editTextLunchCalories);
        editTextDinnerCalories = findViewById(R.id.editTextDinnerCalories);
        textViewTotalCalories = findViewById(R.id.textViewTotalCalories);
        buttonCalculateCalories = findViewById(R.id.buttonCalculateCalories);
        buttonSave = findViewById(R.id.buttonSave);
        buttonUploadPhoto = findViewById(R.id.buttonUploadPhoto);
        editTextTrainingCount = findViewById(R.id.editTextTrainingCount);
        editTextTrainingTime = findViewById(R.id.editTextTrainingTime);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextDate = findViewById(R.id.editTextDate);

        buttonCalculateCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTotalCalories();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        buttonUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });
    }

    private void calculateTotalCalories() {
        try {
            float breakfastCalories = Float.parseFloat(editTextBreakfastCalories.getText().toString());
            float lunchCalories = Float.parseFloat(editTextLunchCalories.getText().toString());
            float dinnerCalories = Float.parseFloat(editTextDinnerCalories.getText().toString());

            // Общее количество калорий
            float totalCalories = breakfastCalories + lunchCalories + dinnerCalories;
            textViewTotalCalories.setText("Общее количество калорий: " + totalCalories);
        } catch (NumberFormatException e) {
            textViewTotalCalories.setText("Ошибка ввода данных");
        }
    }

    private void saveProfileData() {
        try {
            int trainingCount = Integer.parseInt(editTextTrainingCount.getText().toString());
            double trainingTime = Double.parseDouble(editTextTrainingTime.getText().toString());
            double breakfastCalories = Double.parseDouble(editTextBreakfastCalories.getText().toString());
            double lunchCalories = Double.parseDouble(editTextLunchCalories.getText().toString());
            double dinnerCalories = Double.parseDouble(editTextDinnerCalories.getText().toString());
            double weight = Double.parseDouble(editTextWeight.getText().toString());
            String date = editTextDate.getText().toString();

            byte[] image = null;
            if (selectedImage != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                image = baos.toByteArray();
            }

            long id = db.insertProfileData(trainingCount, trainingTime, breakfastCalories, lunchCalories, dinnerCalories, weight, date, image);
            if (id != -1) {
                Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
                // Переход к History активности с передачей ID пользователя
                Intent intent = new Intent(Profile.this, History.class);
                intent.putExtra("userId", id); // Передаем ID пользователя
                startActivity(intent);
            } else {
                Toast.makeText(this, "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ошибка ввода данных", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                selectedImage = BitmapFactory.decodeStream(inputStream);
                Toast.makeText(this, "Фото загружено", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка загрузки фото", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
