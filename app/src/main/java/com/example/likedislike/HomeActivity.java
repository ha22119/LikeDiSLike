package com.example.likedislike;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

public class HomeActivity extends AppCompatActivity {
    Spinner subejctSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        subejctSpinner = findViewById(R.id.subjectSpinner);
        subejctSpinner.setSelection(0);
    }
}
