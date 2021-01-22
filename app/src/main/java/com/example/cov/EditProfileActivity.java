package com.example.cov;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cov.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText profileFullName, profilePhone;
    TextView profileEmail;
    private FirebaseUser firebaseUser;
    Button btnSaveProfileInfo;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbUser;
    User userC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        dbUser = FirebaseDatabase.getInstance().getReference("Users");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(R.layout.edit_profile);
        Intent data = getIntent();
        String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");

        profileFullName = findViewById(R.id.profileFullName);
        profileEmail = findViewById(R.id.profileEmailAddress);
        profilePhone = findViewById(R.id.profilePhoneNo);

        profileEmail.setText(firebaseUser.getEmail());
        profileFullName.setText(firebaseUser.getDisplayName());
        profilePhone.setText(firebaseUser.getPhoneNumber());

        Log.d(TAG, "onCreate: " + fullName + " " + email + " " + phone);

        btnSaveProfileInfo = (Button) findViewById(R.id.saveProfileInfo);
        btnSaveProfileInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullName = profileFullName.getText().toString().trim();
                String phoneNumber = profilePhone.getText().toString().trim();

                if (TextUtils.isEmpty(fullName)) {
                    profileFullName.setError("fullName is Required.");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    profilePhone.setError("phone number is Required.");
                    return;
                }

                updateProfile(profileFullName.getText().toString(), profileEmail.getText().toString(), profilePhone.getText().toString());
            }
        });



        dbUser.child(firebaseUser.getUid()).getRef().addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshotx) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshotx != null && dataSnapshotx.exists() && dataSnapshotx.getChildrenCount() > 0) {
                    userC = dataSnapshotx.getValue(User.class);
                    profilePhone.setText(userC.getPhoneNumber());
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void updateProfile(String fullName, String email, String phoneNumber) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build();


        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this, "User profile updated.", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "User profile updated.");
                            firebaseUser.reload();

                            User user = new User();
                            user.setEmail(email);
                            user.setDisplayName(fullName);
                            user.setPhoneNumber(phoneNumber);

                            dbUser.child(firebaseUser.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }
                });

    }
}
