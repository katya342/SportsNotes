package com.example.sportsnotes;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class UsersList extends AppCompatActivity {

    private Database databaseHelper;
    private ListView usersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        databaseHelper = new Database(this);
        usersListView = findViewById(R.id.users_list_view);

        loadUsers();
    }

    private void loadUsers() {
        Cursor cursor = databaseHelper.getAllUsers();
        String[] from = new String[]{Database.COLUMN_LOGIN, Database.COLUMN_EMAIL};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                from,
                to,
                0);

        usersListView.setAdapter(adapter);
    }
}
