package com.example.likedislike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText editId, editPwd;
    TextView signUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.LoginButton);
        editId = findViewById(R.id.EditID);
        editPwd = findViewById(R.id.EditPwd);
        signUpLink = findViewById(R.id.signUpLink);

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(signUpIntent);
            }
        });

    }
}

