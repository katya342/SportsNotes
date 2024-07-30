package com.example.sportsnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChoiceOfSport extends AppCompatActivity {

    private EditText editTextSportType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_of_sport);

        editTextSportType = findViewById(R.id.editTextSportType);

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sportType = editTextSportType.getText().toString().trim();
                if (sportType.isEmpty()) {
                    Toast.makeText(ChoiceOfSport.this, "Пожалуйста, укажите вид спорта", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ChoiceOfSport.this, DaysOfTheWeek.class);
                    intent.putExtra("sport_type", sportType);
                    intent.putExtra("days_per_week", getIntent().getIntExtra("days_per_week", 0));
                    startActivity(intent);
                }
            }
        });
    }
}
