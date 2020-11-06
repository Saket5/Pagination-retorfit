package com.example.payo;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {
    TextView profileFullName,profileEmail,profilePhone,profileAddress;
    ProgressBar progressBar;
    Button Logout;
    UserDetails user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);



        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar=view.findViewById(R.id.profileProgressBar);
        profileFullName=view.findViewById(R.id.nameTextView);
        profileAddress=view.findViewById(R.id.addressTextView);
        profileEmail=view.findViewById(R.id.emailTextView);
        profilePhone=view.findViewById(R.id.phoneTextView);
        Logout=view.findViewById(R.id.profileLogoutButton);
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("Profile", "handleOnBackPressed: starts");
                MainActivity activity = (MainActivity) requireActivity();
                activity.mNavController.navigate(R.id.action_nav_profile_to_nav_home);
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        Logout.setClickable(false);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user=snapshot.getValue(UserDetails.class);
                if(user != null)
                {
                    profileFullName.setText(user.getFullName());
                    profileEmail.setText(user.getEmail());
                    profilePhone.setText(user.getPhoneNo());
                    profileAddress.setText(user.getAddress());
                    progressBar.setVisibility(View.GONE);
                    Logout.setClickable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                MainActivity activity = (MainActivity) requireActivity();
                activity.mNavController.navigate(R.id.nav_logout);
                activity.finishAffinity();
            }
        });
    }
}