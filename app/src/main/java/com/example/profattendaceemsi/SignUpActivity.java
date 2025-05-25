package com.example.profattendaceemsi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    private Uri selectedImageUri = null;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");

        EditText nameEditText = findViewById(R.id.editTextTextName);
        EditText phoneEditText = findViewById(R.id.editTextTextPhone);
        EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
        EditText passwordEditText = findViewById(R.id.editTextTextPassword);
        Button signUpButton = findViewById(R.id.button);
        TextView signInTextView = findViewById(R.id.sign_in_text);
        profileImageView = findViewById(R.id.profile_image);

        // Image picker launcher
        ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        profileImageView.setImageURI(uri);
                    }
                });

        profileImageView.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        signInTextView.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        });

        signUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(SignUpActivity.this, "Creating account...", Toast.LENGTH_SHORT).show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                uploadProfileImageAndSaveUser(user.getUid(), name, phone, email);
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "Register failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void uploadProfileImageAndSaveUser(String userId, String name, String phone, String email) {
        if (selectedImageUri != null) {
            StorageReference fileRef = storageRef.child(userId + ".jpg");

            fileRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot ->
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String photoUrl = uri.toString();
                        saveUserToFirestore(userId, name, phone, email, photoUrl);
                    }).addOnFailureListener(e ->
                            Toast.makeText(SignUpActivity.this, "Failed to get photo URL", Toast.LENGTH_SHORT).show()
                    )
            ).addOnFailureListener(e ->
                    Toast.makeText(SignUpActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        } else {
            // No image selected
            saveUserToFirestore(userId, name, phone, email, null);
        }
    }

    private void saveUserToFirestore(String userId, String name, String phone, String email, String photoUrl) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("phone", phone);
        user.put("email", email);
        user.put("createdAt", System.currentTimeMillis());
        if (photoUrl != null) {
            user.put("photoUrl", photoUrl);
        }

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(SignUpActivity.this, "Created user", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                    intent.putExtra("PROF_NAME", name);
                    intent.putExtra("PHOTO_URL", photoUrl);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(SignUpActivity.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
