package com.example.sportsnotes;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryWorkout extends AppCompatActivity {

    private Database dbHelper;
    private ListView workoutsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_workout);

        dbHelper = new Database(this);
        workoutsListView = findViewById(R.id.workouts_list_view);

        int userId = getIntent().getIntExtra("USER_ID", -1);

        if (userId != -1) {
            Cursor cursor = dbHelper.getAllWorkoutsCursor(userId);
            if (cursor != null && cursor.moveToFirst()) {
                String[] fromColumns = {
                        Database.COLUMN_MONDAY_NOTES,
                        Database.COLUMN_TUESDAY_NOTES,
                        Database.COLUMN_WEDNESDAY_NOTES,
                        Database.COLUMN_THURSDAY_NOTES,
                        Database.COLUMN_FRIDAY_NOTES,
                        Database.COLUMN_SATURDAY_NOTES,
                        Database.COLUMN_SUNDAY_NOTES
                };

                int[] toViews = {
                        android.R.id.text1,
                        android.R.id.text1,
                        android.R.id.text1,
                        android.R.id.text1,
                        android.R.id.text1,
                        android.R.id.text1,
                        android.R.id.text1
                };

                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                        this,
                        android.R.layout.simple_list_item_1,
                        cursor,
                        fromColumns,
                        toViews,
                        0
                );
                adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                    @Override
                    public boolean setViewValue(android.view.View view, Cursor cursor, int columnIndex) {
                        if (view.getId() == android.R.id.text1) {
                            String mondayNotes = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_MONDAY_NOTES));
                            String tuesdayNotes = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_TUESDAY_NOTES));
                            String wednesdayNotes = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_WEDNESDAY_NOTES));
                            String thursdayNotes = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_THURSDAY_NOTES));
                            String fridayNotes = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_FRIDAY_NOTES));
                            String saturdayNotes = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_SATURDAY_NOTES));
                            String sundayNotes = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_SUNDAY_NOTES));

                            String allNotes = "Понедельник: " + mondayNotes + "\n"
                                    + "Вторник: " + tuesdayNotes + "\n"
                                    + "Среда: " + wednesdayNotes + "\n"
                                    + "Четверг: " + thursdayNotes + "\n"
                                    + "Пятница: " + fridayNotes + "\n"
                                    + "Суббота: " + saturdayNotes + "\n"
                                    + "Воскресенье: " + sundayNotes;

                            ((android.widget.TextView) view).setText(allNotes);
                            return true;
                        }
                        return false;
                    }
                });
                workoutsListView.setAdapter(adapter);
            }
        }
    }
}
