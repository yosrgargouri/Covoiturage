package com.example.cov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cov.model.Offre;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private final static String TAG = "MainActivity";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference db;
    private ListView mListData;
    private SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm'Z'");
    private EditText mHeureDepart;
    private EditText mAdresseDestination;
    private EditText mAdresseDepart;
    private EditText mNombrePlace;
    private EditText mPrix;
    private EditText mTelephone;
    private Button saveBtn;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListData = findViewById(R.id.listData);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ArrayList<Offre> offres = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("offres");

        // Read from the database
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot != null && dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    List<Object> objectList = new ArrayList<>();
                    offres.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Offre offre = ds.getValue(Offre.class);
                        offres.add(offre);
                        mListData.setAdapter(new OffreListAdapter(MainActivity.this, R.layout.list_detail, offres));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        mListData.setAdapter(new OffreListAdapter(MainActivity.this, R.layout.list_detail, offres));

        Button mButtonPlus = (Button) findViewById(R.id.buttonPlus);
        mButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogAdd();
            }
        });
    }

    //TODO
    public void displayDialogAdd() {

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("ADD REQUEST");
        dialog.setContentView(R.layout.add_dialog);
        dialog.show();


        //Find widgets
        mHeureDepart = dialog.findViewById(R.id.heureDepart);
        mAdresseDestination = dialog.findViewById(R.id.adresseDestination);
        mAdresseDepart = dialog.findViewById(R.id.adresseDepart);
        mNombrePlace = dialog.findViewById(R.id.nombrePlace);
        mPrix = dialog.findViewById(R.id.prix);
        mTelephone = dialog.findViewById(R.id.telephone);

        saveBtn = dialog.findViewById(R.id.btnSave);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data form edit text
                String heureDepart = mHeureDepart.getText().toString();
                String adresseDestination = mAdresseDestination.getText().toString();
                String adresseDepart = mAdresseDepart.getText().toString();
                Integer nombrePlace = Integer.parseInt(mNombrePlace.getText().toString());
                Integer prix = Integer.parseInt(mPrix.getText().toString());
                Integer telephone = Integer.parseInt(mTelephone.getText().toString());

                Offre offre = new Offre();
                if (firebaseUser != null) {
                    // Name, email
                    offre.setFull_name(firebaseUser.getDisplayName());
                    offre.setEmail(firebaseUser.getEmail());
                }
                offre.setAdresse_depart(adresseDepart);
                offre.setAdresse_depart(adresseDepart);
                offre.setAdresse_depart(adresseDepart);
                offre.setAdresse_destination(adresseDestination);
                offre.setHeure_depart(heureDepart);
                offre.setNombre_place(nombrePlace);
                offre.setPrix(prix);
                offre.setTelephone(telephone);

                db.push().setValue(offre).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.cancel();
                        Toast.makeText(MainActivity.this, "add with success.", Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(MainActivity.this, "add failed.", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}