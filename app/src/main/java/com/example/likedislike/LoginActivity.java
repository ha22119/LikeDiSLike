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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText editEmail, editPwd;
    TextView signUpLink;

    final static String TAG = "Yahoo";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.LoginButton);
        editEmail = findViewById(R.id.editEmail);
        editPwd = findViewById(R.id.EditPwd);
        signUpLink = findViewById(R.id.signUpLink);

        mAuth = FirebaseAuth.getInstance();

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(signUpIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAccount(editEmail.getText().toString(), editPwd.getText().toString());
            }
        });

//        signOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//            }
//        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void loginAccount(String email, String password) { // 로그인
        Log.d(TAG, "loginAccount:" + email);
        if (!validateForm()) {
            Toast.makeText(this, "올바르지 않은 형식", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:성공");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, ""+user.getEmail().toString()+" 으로 로그인",
                                    Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
                            homeIntent.putExtra("userInfo",user);
                            startActivity(homeIntent);
                        } else {
                            Log.w(TAG, "signInWithEmail:실패", task.getException());
                            Toast.makeText(LoginActivity.this, "로그인 실패",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private boolean validateForm() { // 이게 형식이 맞는지 확인하기
        boolean valid = true;

        String email_before = editEmail.getText().toString();
        String email = email_before.replace(" ","");

        if (TextUtils.isEmpty(email)) { // 이메일이 비어있으면 false 반환
            valid = false;
        } else {
        }

        String password = editPwd.getText().toString();
        if (TextUtils.isEmpty(password)) {
            valid = false;
            Toast.makeText(getApplicationContext(), "비밀번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }else {
        }
        return valid;
    }
//    private void signOut() {
////        mAuth.signOut();
////        Log.d(TAG,"로그아웃 함");
////        Toast.makeText(this, "로그아웃 됨",
////                Toast.LENGTH_SHORT).show();
////        Intent LogOutIntent = new Intent(getApplicationContext(),LoginActivity.class);
////        startActivity(LogOutIntent);
////    }
}

