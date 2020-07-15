package com.example.likedislike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteActivity extends AppCompatActivity {
    ImageButton backButton,plusButton;
    TextView nameView;
    EditText textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        backButton = findViewById(R.id.backButton);
        plusButton = findViewById(R.id.plusButton);
        nameView = findViewById(R.id.nameView);
        textView = findViewById(R.id.textView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancleIntent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(cancleIntent);
                finish();
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView.getText().toString();
                String title = nameView.getText().toString();

                Intent writeIntentBack = new Intent();
                writeIntentBack.putExtra("text",text);
                writeIntentBack.putExtra("title",title);
                setResult(RESULT_OK,writeIntentBack);
                finish();
            }
        });

    }
}
