package com.example.profattendaceemsi;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EmploiDuTempsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emploi_du_temps);

TableLayout timetableTable = findViewById(R.id.timetableTable);
    }
}