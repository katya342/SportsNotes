package com.example.sportsnotes;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    private Database db;
    private LinearLayout workoutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        workoutContainer = findViewById(R.id.workoutContainer);

        db = new Database(this);

        ArrayList<String> profiles = db.getAllProfiles();

        if (profiles != null) {
            for (String profile : profiles) {
                TextView textView = new TextView(this);
                textView.setText(profile);
                textView.setTextSize(16);
                textView.setTextColor(getResources().getColor(android.R.color.black));
                textView.setPadding(16, 16, 16, 16); // Optional: Add padding for better readability
                workoutContainer.addView(textView);
            }
        }
    }
}
