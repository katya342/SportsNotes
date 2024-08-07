package com.example.sportsnotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextTrainingCount, editTextTrainingTime, editTextBreakfastCalories, editTextLunchCalories, editTextDinnerCalories, editTextWeight, editTextDate;
    private TextView textViewTotalCalories;
    private Button buttonCalculateCalories, buttonUploadPhoto, buttonSavePhoto;
    private ImageView imageViewPhoto;

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Найти поля ввода и текстовые поля
        editTextTrainingCount = findViewById(R.id.editTextTrainingCount);
        editTextTrainingTime = findViewById(R.id.editTextTrainingTime);
        editTextBreakfastCalories = findViewById(R.id.editTextBreakfastCalories);
        editTextLunchCalories = findViewById(R.id.editTextLunchCalories);
        editTextDinnerCalories = findViewById(R.id.editTextDinnerCalories);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextDate = findViewById(R.id.editTextDate);
        textViewTotalCalories = findViewById(R.id.textViewTotalCalories);
        buttonCalculateCalories = findViewById(R.id.buttonCalculateCalories);
        buttonUploadPhoto = findViewById(R.id.buttonUploadPhoto);
        buttonSavePhoto = findViewById(R.id.buttonSavePhoto);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);

        db = new Database(this);

        // Установить слушатель на кнопку расчета калорий
        buttonCalculateCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateTotalCalories();
            }
        });

        // Установить слушатель на кнопку выбора фото
        buttonUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // Установить слушатель на кнопку сохранения данных
        buttonSavePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });
    }

    private void calculateTotalCalories() {
        double breakfastCalories = getCaloriesFromEditText(editTextBreakfastCalories);
        double lunchCalories = getCaloriesFromEditText(editTextLunchCalories);
        double dinnerCalories = getCaloriesFromEditText(editTextDinnerCalories);

        double totalCalories = breakfastCalories + lunchCalories + dinnerCalories;

        textViewTotalCalories.setText("Общее количество калорий: " + totalCalories);
    }

    private double getCaloriesFromEditText(EditText editText) {
        String caloriesText = editText.getText().toString();
        if (caloriesText.isEmpty()) {
            return 0;
        } else {
            return Double.parseDouble(caloriesText);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите фото"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageViewPhoto.setImageURI(imageUri);
        }
    }

    private void saveProfileData() {
        int trainingCount = Integer.parseInt(editTextTrainingCount.getText().toString());
        double trainingTime = Double.parseDouble(editTextTrainingTime.getText().toString());
        double breakfastCalories = Double.parseDouble(editTextBreakfastCalories.getText().toString());
        double lunchCalories = Double.parseDouble(editTextLunchCalories.getText().toString());
        double dinnerCalories = Double.parseDouble(editTextDinnerCalories.getText().toString());
        double weight = Double.parseDouble(editTextWeight.getText().toString());
        String date = editTextDate.getText().toString();

        // Получить изображение из ImageView
        byte[] image = getImageBytes(imageViewPhoto);

        // Вставить данные в базу данных
        db.insertUserProfile(trainingCount, trainingTime, breakfastCalories, lunchCalories, dinnerCalories, weight, date, image);

        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
    }

    private byte[] getImageBytes(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
