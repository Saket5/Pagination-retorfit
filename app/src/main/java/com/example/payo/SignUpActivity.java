package com.example.payo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG ="SignUp" ;
    private TextInputLayout nameTextInput, emailTextInput, passwordTextInput, confirmPasswordTextInput,phoneNumberTextInput,AddressTextInput;
    private TextInputEditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText,phoneNumberEditText,AddressEditText;

    private String fullName, email;
    private int phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        phoneNumberTextInput=findViewById(R.id.phoneNumber);
        phoneNumberEditText=findViewById(R.id.phoneNumberEditText);
        AddressEditText=findViewById(R.id.AddressEditText);
        AddressTextInput=findViewById(R.id.AddressTextInput);
        nameTextInput = findViewById(R.id.createAccountScreenFullNameTextInput);
        nameEditText = findViewById(R.id.createAccountScreenFullNameEditText);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        emailTextInput = findViewById(R.id.createAccountScreenEmailTextInput);
        emailEditText = findViewById(R.id.createAccountScreenEmailEditText);

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

        passwordTextInput = findViewById(R.id.createAccountScreenPasswordTextInput);
        passwordEditText = findViewById(R.id.createAccountScreenPasswordEditText);

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

        confirmPasswordTextInput = findViewById(R.id.createAccountScreenConfirmPasswordTextInput);
        confirmPasswordEditText = findViewById(R.id.createAccountScreenConfirmPasswordEditText);

        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPasswordTextInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button createAccountButton = findViewById(R.id.createAccountScreenCreateAccountButton);
        findViewById(R.id.createAccountBackArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: back arrow is pressed");
                onBackPressed();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: create account button pressed");

                if (validateFields()) {
                    signupconfirm();
                    findViewById(R.id.createAccountScreenProgressBar).setVisibility(View.VISIBLE);
                }
            }
        });
        findViewById(R.id.createAccountScreenLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.createAccountBackArrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void signupconfirm()
    {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: account created");
                            storeUserDetails();
                        } else {
                            findViewById(R.id.createAccountScreenProgressBar).setVisibility(View.INVISIBLE);
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void storeUserDetails()
    {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final UserDetails user = new UserDetails(fullName,email,passwordEditText.getText().toString(),phoneNumberEditText.getText().toString(),AddressEditText.getText().toString());

        UserProfileChangeRequest.Builder profileUpdates = new UserProfileChangeRequest.Builder();
        profileUpdates.setDisplayName(fullName);

        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates.build())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

                            //Make users database
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(user);


                            Log.d(TAG, "onComplete: starting main activity");
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finishAffinity();
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        } else {
                            Log.d(TAG, "onComplete: Could not update display name");
                        }
                    }
                });
    }
    private boolean validateFields()
    {
        fullName = nameEditText.getText().toString();
        String nameValidation = validateName(fullName);

        email = emailEditText.getText().toString();
        String emailValidation = validateEmail(email);

        String passwordValidation = validatePassword(passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());

        String confirmPasswordValidation = validateConfirmPassword(confirmPasswordEditText.getText().toString(), passwordEditText.getText().toString());
        String phoneNumberValidation=validatePhoneNumber(phoneNumberEditText.getText().toString());
        String AddressValidation=validateAddress(AddressEditText.getText().toString());
        if (nameValidation == null && emailValidation == null && passwordValidation == null && confirmPasswordValidation == null && phoneNumberValidation==null && AddressValidation==null) {
            Log.d(TAG, "validateFields: all fields valid");
            return true;
        }

        if (nameValidation != null) {
            nameTextInput.setError(nameValidation);
        }

        if (emailValidation != null) {
            emailTextInput.setError(emailValidation);
        }

        if (passwordValidation != null) {
            passwordTextInput.setError(passwordValidation);
        }

       if (confirmPasswordValidation != null) {
           confirmPasswordTextInput.setError(confirmPasswordValidation);
        }
       if(phoneNumberValidation !=null)
       {
           phoneNumberTextInput.setError(phoneNumberValidation);
       }
       if(AddressValidation !=null)
       {
           AddressTextInput.setError(AddressValidation);
       }

        return false;
    }

    private String validateName(String name) {
        if (name.trim().length() == 0) {
            return "Name cannot be empty";
        } else if (name.trim().matches("^[0-9]+$")) {
            return "Name cannot have numbers in it";
        } else if (!name.trim().matches("^[a-zA-Z][a-zA-Z ]++$")) {
            return "Invalid Name";
        }
        return null;
    }



    private String validateEmail(String email) {
        if (email.trim().length() == 0) {
            return "Email cannot be empty";
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            return "Invalid Email";
        }
        return null;
    }

    private String validatePassword(String password, String confirmPassword) {
        if (password.trim().length() == 0) {
            return "Passwords cannot be empty";
        } else if (confirmPassword.trim().length() == 0) {
            return "Passwords cannot be empty";
        } else if (!confirmPassword.trim().matches(password.trim())) {
            return "Passwords do not match";
        } else if (password.trim().length() < 8) {
            return "Password too small. Minimum length is 8";
        } else if (password.trim().length() > 15) {
            return "Password too long. Maximum length is 15";
        } else if (password.trim().contains(" ")) {
            return "Password cannot contain spaces";
        }
        return null;
    }

    private String validateConfirmPassword(String confirmPassword, String password) {
        if (confirmPassword.trim().length() == 0) {
            return "Passwords cannot be empty";
        } else if (password.trim().length() == 0) {
            return "Passwords cannot be empty";
        } else if (!confirmPassword.trim().matches(password.trim())) {
            return "Passwords do not match";
        } else if (password.trim().length() < 8) {
            return "Password too small. Minimum length is 8";
        } else if (password.trim().length() > 15) {
            return "Password too long. Maximum length is 15";
        } else if (password.trim().contains(" ")) {
            return "Password cannot contain spaces";
        }
        return null;

    }
    private String validatePhoneNumber(String phoneNo)
    {
        if(phoneNo.length()<10||phoneNo.length()>10)
            return "Phone Number Invalid";
        return null;
    }
    private String validateAddress(String Address)
    {
        if(Address.length()>150)
            return"Address must not be more than 150 characters";
        return null;
    }
}