package com.example.sportsnotes;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout workoutContainer = findViewById(R.id.workoutContainer);

        ArrayList<String> workoutList = getIntent().getStringArrayListExtra("workoutList");

        if (workoutList != null) {
            for (String workout : workoutList) {
                TextView textView = new TextView(this);
                textView.setText(workout);
                textView.setTextSize(16);
                textView.setTextColor(getResources().getColor(android.R.color.black));
                workoutContainer.addView(textView);
            }
        }
    }
}
