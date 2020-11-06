package com.example.payo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class BoardingScreen extends AppCompatActivity {
    public Button signup;
    public Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding_screen);
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.login);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            //Log.d(TAG, "onCreate: User already logged in");

            //findViewById(R.id.loginScreenProgressBar).setVisibility(View.VISIBLE);
            startActivity(new Intent(BoardingScreen.this, MainActivity.class));
            finishAffinity();
            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}