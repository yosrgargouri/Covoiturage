package com.example.cov;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cov.model.Offre;
import com.example.cov.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    TextView profileFullName, profileEmail, profilePhone;
    Button editProfil;
    private FirebaseUser firebaseUser;
    private DatabaseReference dbUser;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbUser = FirebaseDatabase.getInstance().getReference("Users");

        setContentView(R.layout.profile);
        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");

        profileFullName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhone = findViewById(R.id.profilePhone);
        editProfil = findViewById(R.id.editProfile);

        profileEmail.setText(firebaseUser.getEmail());
        profileFullName.setText(firebaseUser.getDisplayName());

        Log.d(TAG, "onCreate: " + fullName + " " + email + " " + phone);


        dbUser.child(firebaseUser.getUid()).getRef().addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshotx) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshotx != null && dataSnapshotx.exists() && dataSnapshotx.getChildrenCount() > 0) {
                    user = dataSnapshotx.getValue(User.class);
                    profilePhone.setText(user.getPhoneNumber());
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void editProfile(View view) {
        startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
        finish();
    }

}
