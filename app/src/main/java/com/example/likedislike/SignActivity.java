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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorResolver;

public class SignActivity extends AppCompatActivity {

    EditText editEmail, editPwd,reEditPwd;
    Button verifyEmailButton, signUpBtn;

    final static String TAG = "Yahoo";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        editEmail = findViewById(R.id.editEmail);
        editPwd = findViewById(R.id.editPwd);
        reEditPwd = findViewById(R.id.reEditPwd);

        verifyEmailButton = findViewById(R.id.verifyEmailButton);
        signUpBtn =findViewById(R.id.signUpBtn);

        mAuth = FirebaseAuth.getInstance();

        verifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(editEmail.getText().toString(), editPwd.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignActivity.this, "회원가입 성공",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignActivity.this, "회원가입 실패",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void sendEmailVerification() { // 이메일 인증 보내기
        verifyEmailButton.setEnabled(false);

        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        verifyEmailButton.setEnabled(true);
                        Log.d(TAG,"메일 보낼까 말까");

                        if (task.isSuccessful()) {
                            Toast.makeText(SignActivity.this,
                                    "LiKEDiSLiKE 에서 \n"+ user.getEmail()+" 로 이메일을 발송하였습니다.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"메일 보냄");
                            finish();


                        } else {
                            Log.d(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(SignActivity.this,
                                    "이메일을 보내는데 실패하였습니다.\n이메일이 정확한지 확인해주세요.",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"메일 안 보냄");

                        }
                    }
                });
    }

    private boolean validateForm() { // 이게 형식이 맞는지 확인하기
        boolean valid = true;

        String email = editEmail.getText().toString();
        if (TextUtils.isEmpty(email)) { // 이메일이 비어있으면 false 반환
            valid = false;
        } else {
        }

        String password = editPwd.getText().toString();
        String rePassword = reEditPwd.getText().toString();
        if (TextUtils.isEmpty(password)) {
            valid = false;
            Toast.makeText(getApplicationContext(), "비밀번호가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(rePassword)){
            valid = false;
            Toast.makeText(getApplicationContext(), "비밀번호 확인을 위해 비밀번호를 \n한 번 더 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else if(!(password.equals(rePassword))) {
            Toast.makeText(getApplicationContext(), "재입력된 비밀번호와 \n비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }else {
        }
        return valid;
    }
}
