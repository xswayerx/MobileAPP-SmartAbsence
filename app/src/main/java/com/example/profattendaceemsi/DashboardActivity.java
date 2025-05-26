package com.example.profattendaceemsi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DashboardActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView welcomeText = findViewById(R.id.dashboard_adminName);
        String profName = getIntent().getStringExtra("profName");
        if (profName != null && !profName.isEmpty()) {
            welcomeText.setText("Welcome, Prof. " + profName + "!");
        } else {
            welcomeText.setText("Professor Name ");
        }


        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DashboardActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress", "WrongCast", "WrongViewCast"})
        LinearLayout mapSection = findViewById(R.id.mapSection);
        mapSection.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MapsActivity.class);
            startActivity(intent);
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout assistantTitle = findViewById(R.id.assistant_virtual);
        assistantTitle.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, Assistant_virtuelActivity.class);
            startActivity(intent);
        });
        CardView emploiSection = findViewById(R.id.history);
        emploiSection.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EmploiDuTempsActivity.class);
            startActivity(intent);
        });
        CardView absence = findViewById(R.id.card4);
        absence.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MarkAbsenceActivity.class);
            startActivity(intent);
        });
    }
}
