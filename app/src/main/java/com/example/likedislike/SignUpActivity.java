package com.example.likedislike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth auth;
//    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");


    private EditText emailet, pwet, confpwet;
    private String email = "";
    private String password = "";
    private String confpassword = "";

    Spinner spinner;
    Button codebtn, signupbtn;
    RadioButton womanrbtn, manrbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        user = auth.getCurrentUser();
        auth = FirebaseAuth.getInstance();

        spinner = findViewById(R.id.sp);
        emailet = findViewById(R.id.emailet);
        codebtn = findViewById(R.id.codebtn);
        signupbtn = findViewById(R.id.signupbtn);
        pwet = findViewById((R.id.pwet));
        confpwet = findViewById((R.id.confpwet));
        womanrbtn = findViewById(R.id.womanrbtn);
        manrbtn = findViewById(R.id.manrbtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.grade_arr, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        codebtn.setOnClickListener(listener);
        signupbtn.setOnClickListener(listener);
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.codebtn:
                    sendEmail();
//                    email = emailet.getText().toString().trim();
//                    auth.sendSignInLinkToEmail(email, actionCodeSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Toast.makeText(SignUpActivity.this, "1", Toast.LENGTH_SHORT).show();
////              if (task.isSuccessful()) {
////              Toast.makeText(SignUpActivity.this, "이메일 발송 성공", Toast.LENGTH_SHORT).show();
////              Log.d(TAG, "Email sent.");
////              } else{
////              Toast.makeText(SignUpActivity.this, "이메일 발송 실패", Toast.LENGTH_SHORT).show();
////              }
//                        }
//                    });
//                    sendSignInLink(emailet.getText().toString().trim(), actionCodeSettings);
                case R.id.signupbtn:
                    signUp();
            }
        }
    };

    private void sendEmail() {
        email = emailet.getText().toString().trim();
        if(isValidEmail()){
            sendSignInLink(email);
        }
    }


    public void sendSignInLink(String email) {
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder().setUrl("https://likedislike.page.link").setHandleCodeInApp(true).setAndroidPackageName("com.example.likedislike", true, "26").build();
        auth.sendSignInLinkToEmail(email, actionCodeSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SignUpActivity.this, "1", Toast.LENGTH_SHORT).show();
//              if (task.isSuccessful()) {
//              Toast.makeText(SignUpActivity.this, "이메일 발송 성공", Toast.LENGTH_SHORT).show();
//              Log.d(TAG, "Email sent.");
//              } else{
//              Toast.makeText(SignUpActivity.this, "이메일 발송 실패", Toast.LENGTH_SHORT).show();
//              }
            }
        });
    }

    private void signUp() {
        email = emailet.getText().toString().trim();
        password = pwet.getText().toString().trim();
        confpassword = confpwet.getText().toString().trim();

        if(isValidEmail() && isValidPasswd()) {
            createUser(email, password);
        }
    }

    private boolean isValidEmail() {
        if (email.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            // 이메일 형식 불일치
//            return false;
        } else {
            return true;
        }
    }

    private boolean isValidPasswd() {
        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
//        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
//            // 비밀번호 형식 불일치
//            return false;
        }else if (!password.equals(confpassword)){
            Toast.makeText(this, "비밀번호가 동일하지 않습니다 다시 확인해주세요", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void createUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = auth.getCurrentUser();
                    updateUI(user);
                } else {
                    Toast.makeText(SignUpActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                }
//                if (password.equals(confpw)) {
//                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                FirebaseUser user = auth.getCurrentUser();
//                                assert user != null;
//                                String email = user.getEmail();
//                                String uid = user.getUid();
//
//                                HashMap<Object, String> hashMap = new HashMap<>();
//
//                                hashMap.put("uid", uid);
//                                hashMap.put("email", email);
//
//                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                DatabaseReference reference = database.getReference("Users");
//                                reference.child(uid).setValue(hashMap);
//
//                                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
//                                startActivity(intent);
//                                finish();
//                                Toast.makeText(SignUpActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(SignUpActivity.this, "이메일을 다시 확인해주세요", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
//                    });
//                } else{
//                    Toast.makeText(SignUpActivity.this, "비밀번호가 동일하지 않습니다. 다시 확인해주세요", Toast.LENGTH_SHORT).show();
//                    return;
//                }
            }

            private void updateUI(FirebaseUser user) {

            }
        });
    }

}

//    public void login(Context context, EditText email, EditText password){
//        final String email = emailet.getText().toString();
//        final String password = pwet.getText().toString();
//
//        if (validationCheck(email, password)){
//            try {
//
//            }
//        }
//    }

//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            mBinding.status.setText(getString(R.string.emailpassword_status_fmt,
//                    user.getEmail(), user.isEmailVerified()));
//            mBinding.detail.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
//            mBinding.emailPasswordButtons.setVisibility(View.GONE);
//            mBinding.emailPasswordFields.setVisibility(View.GONE);
//            mBinding.signedInButtons.setVisibility(View.VISIBLE);
//
//            if (user.isEmailVerified()) {
//                mBinding.verifyEmailButton.setVisibility(View.GONE);
//            } else {
//                mBinding.verifyEmailButton.setVisibility(View.VISIBLE);
//            }
//        } else {
//            mBinding.status.setText(R.string.signed_out);
//            mBinding.detail.setText(null);
//
//            mBinding.emailPasswordButtons.setVisibility(View.VISIBLE);
//            mBinding.emailPasswordFields.setVisibility(View.VISIBLE);
//            mBinding.signedInButtons.setVisibility(View.GONE);
//        }


//    public void verifySignInLink() {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        Intent intent = getIntent();
//        String emailLink = emailet.getData().toString();
//
//        if (auth.isSignInWithEmailLink(emailLink)) {
//            String email = "someemail@domain.com";
//
//            auth.signInWithEmailLink(email, emailLink).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (task.isSuccessful()) {
//                        Log.d(TAG, "Successfully signed in with email link!");
//                        AuthResult result = task.getResult();
//                        // You can access the new user via result.getUser()
//                        // Additional user info profile *not* available via:
//                        // result.getAdditionalUserInfo().getProfile() == null
//                        // You can check if the user is new or existing:
//                        // result.getAdditionalUserInfo().isNewUser()
//                    } else {
//                        Log.e(TAG, "Error signing in with email link", task.getException());
//                    }
//                }
//            });
//        }
//    }

