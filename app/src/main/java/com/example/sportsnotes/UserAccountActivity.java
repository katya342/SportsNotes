package com.example.sportsnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UserAccountActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView user_name;
    private TextView user_email;
    private ImageView profilePictureEdit;
    private EditText editUserName;
    private EditText editUserEmail;
    private Uri profilePictureUri;
    private Database dbHelper;
    private ImageView profile_picture;
    private AlertDialog editProfileDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        dbHelper = new Database(this);
        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        profile_picture = findViewById(R.id.profile_picture);
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> onBackPressed());

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = (int) sharedPreferences.getLong("user_id", -1);
        if (userId != -1) {
            loadUserData(userId);
        } else {
            user_name.setText("Invalid user ID");
            user_email.setText("Please login again");
        }

        findViewById(R.id.edit_profile_button).setOnClickListener(v -> showEditProfileDialog());

        String profilePictureUriString = sharedPreferences.getString("profile_picture_uri", null);
        if (profilePictureUriString != null) {
            profilePictureUri = Uri.parse(profilePictureUriString);
            profile_picture.setImageURI(profilePictureUri);
        }
    }

    private void loadUserData(int userId) {
        Cursor currentUser = null;
        try {
            currentUser = dbHelper.getUserById(userId);
            if (currentUser != null && currentUser.moveToFirst()) {
                String name = currentUser.getString(currentUser.getColumnIndexOrThrow(Database.COLUMN_LOGIN));
                String email = currentUser.getString(currentUser.getColumnIndexOrThrow(Database.COLUMN_EMAIL));
                user_name.setText(name);
                user_email.setText(email);
            } else {
                user_name.setText("User not found");
                user_email.setText("Please check your user ID");
            }
        } finally {
            if (currentUser != null) {
                currentUser.close();
            }
        }
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);
        builder.setView(dialogView);

        editUserName = dialogView.findViewById(R.id.edit_user_name);
        editUserEmail = dialogView.findViewById(R.id.edit_user_email);
        profilePictureEdit = dialogView.findViewById(R.id.profile_picture_edit);
        profilePictureEdit.setImageURI(profilePictureUri);

        Button uploadPictureButton = dialogView.findViewById(R.id.upload_picture_button);
        uploadPictureButton.setOnClickListener(v -> openGallery());

        Button saveChangesButton = dialogView.findViewById(R.id.save_changes_button);
        saveChangesButton.setOnClickListener(v -> saveProfileChanges());

        editProfileDialog = builder.create();
        editProfileDialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profilePictureUri = data.getData();
            profilePictureEdit.setImageURI(profilePictureUri);
        }
    }

    private void saveProfileChanges() {
        String newName = editUserName.getText().toString().trim();
        String newEmail = editUserEmail.getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = (int) sharedPreferences.getLong("user_id", -1);

        if (userId != -1 && !newName.isEmpty() && !newEmail.isEmpty() && profilePictureUri != null) {
            dbHelper.updateUser(userId, newName, newEmail);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("profile_picture_uri", profilePictureUri.toString());
            editor.apply();

            loadUserData(userId);
            profile_picture.setImageURI(profilePictureUri);

            editProfileDialog.dismiss();
        }
    }
}
