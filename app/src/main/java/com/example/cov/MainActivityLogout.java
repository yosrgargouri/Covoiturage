package com.example.cov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityLogout extends AppCompatActivity {

    private EditText mFullname, mEmail, mPassword, mPhone;
    private Button mRegisterBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private TextView mSignInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logout);


        mFullname = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.passwordRegister);
        mPhone = findViewById(R.id.phoneNumber);
        mRegisterBtn = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progressBarRegister);
        mSignInBtn = findViewById(R.id.textLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        //TODO : delete comment
//        if(firebaseAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//        finish();
//        }
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mEmail.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be >= 6 Characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                //firebase
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivityLogout.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign up success,
                                    Toast.makeText(MainActivityLogout.this, "User Created.", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    //updateUI(user);
                                } else {
                                    // If sign up fails, display a message to the user.
                                    progressBar.setVisibility(View.INVISIBLE);
                                    if (task.getException() != null && task.getException().getMessage() != null) {
                                        Toast.makeText(MainActivityLogout.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivityLogout.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }

                                    //updateUI(null);
                                }

                                // ...
                            }
                        });

            }
        });
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}