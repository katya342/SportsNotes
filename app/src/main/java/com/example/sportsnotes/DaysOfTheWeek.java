package com.example.sportsnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DaysOfTheWeek extends AppCompatActivity {

    private CheckBox twoWeekCheckBox, threeWeekCheckBox, moreThanThreeCheckBox, everydayCheckBox, startWeekCheckBox;
    private int selectedDaysPerWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_of_the_week);

        twoWeekCheckBox = findViewById(R.id.twoweek);
        threeWeekCheckBox = findViewById(R.id.threeweek);
        moreThanThreeCheckBox = findViewById(R.id.weeks);
        everydayCheckBox = findViewById(R.id.everyday);
        startWeekCheckBox = findViewById(R.id.week);

        Button weekButton = findViewById(R.id.buttonNext);
        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnyCheckBoxChecked()) {
                    Toast.makeText(DaysOfTheWeek.this, "Пожалуйста, выберите вариант", Toast.LENGTH_SHORT).show();
                } else {
                    selectedDaysPerWeek = getSelectedDaysPerWeek();
                    Intent intent = new Intent(DaysOfTheWeek.this, MarkSelection.class);
                    intent.putExtra("days_per_week", selectedDaysPerWeek);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean isAnyCheckBoxChecked() {
        return twoWeekCheckBox.isChecked() || threeWeekCheckBox.isChecked() ||
                moreThanThreeCheckBox.isChecked() || everydayCheckBox.isChecked() || startWeekCheckBox.isChecked();
    }

    private int getSelectedDaysPerWeek() {
        if (twoWeekCheckBox.isChecked()) return 2;
        if (threeWeekCheckBox.isChecked()) return 3;
        if (moreThanThreeCheckBox.isChecked()) return 4; // Предположим, больше 3 раз в неделю это 4
        if (everydayCheckBox.isChecked()) return 7;
        return 0; // Начать занятия
    }
}
