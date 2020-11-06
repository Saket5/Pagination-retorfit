package com.example.payo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG ="Login Screen";
    private TextInputLayout emailTextInput, passwordTextInput;
    private TextInputEditText emailEditText, passwordEditText;
    private ProgressBar progressBar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar=findViewById(R.id.loginProgressBar);
        emailTextInput = findViewById(R.id.loginEmailTextInput);
        emailEditText = findViewById(R.id.loginEmailEditText);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordTextInput = findViewById(R.id.loginPasswordTextInput);
        passwordEditText = findViewById(R.id.loginPasswordEditText);

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.loginSignUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.loginLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    login();
                }
            }
        });

        findViewById(R.id.loginBackArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: starts");
        super.onPause();

        if (emailTextInput.isErrorEnabled()) {
            emailTextInput.setError(null);
        }

        if (passwordTextInput.isErrorEnabled()) {
            passwordTextInput.setError(null);
        }
    }

    public void login()
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finishAffinity();
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                        }
                    }
                });

    }
    private boolean validateFields() {
        Log.d(TAG, "validateFields: starts");
        String emailValidation = validateEmail(emailEditText.getText().toString());
        String passwordValidation = validatePassword(passwordEditText.getText().toString());

        if (emailValidation == null && passwordValidation == null) {
            Log.d(TAG, "validateFields() returned: " + true);
            return true;
        }

        if (emailValidation != null) {
            emailTextInput.setError(emailValidation);
        }

        if (passwordValidation != null) {
            passwordTextInput.setError(passwordValidation);
        }

        Log.d(TAG, "validateFields() returned: " + false);
        return false;
    }

    private String validateEmail(String email) {
        if (email.trim().length() == 0) {
            return "Email cannot be empty";
        }
        return null;
    }

    private String validatePassword(String password) {
        if (password.trim().length() == 0) {
            return "Password cannot be empty";
        }
        return null;
    }
}