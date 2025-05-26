package com.example.profattendaceemsi;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MarkAbsenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_absence);

        TextView group = findViewById(R.id.group);
        TextView site = findViewById(R.id.site);
//        Button dateButton = findViewById(R.id.dateButton);
        TextView studentListView = findViewById(R.id.studentNameEditText);
        EditText remarksEditText = findViewById(R.id.remarksEditText);
        Button saveButton = findViewById(R.id.saveButton);

        Button dateButton = findViewById(R.id.dateButton);
        TextView selectedDateTextView = findViewById(R.id.selectedDateTextView);

        dateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        selectedDateTextView.setText("📅 " + date);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });


        // Populate spinners and listview as needed
        // Add DatePickerDialog for dateButton
        // Add logic to save attendance and remarks
    }
}