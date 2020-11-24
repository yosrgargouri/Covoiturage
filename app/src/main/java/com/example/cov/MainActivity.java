package com.example.cov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cov.model.Offre;
import com.google.firebase.auth.FirebaseAuth;
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

//    public class FirebaseHelper {
//        DatabaseReference db;
//        boolean saved;
//        ArrayList<Offre> offres = new ArrayList<>();
//        ListView mListView;
//        Context context;
//
//        //ref firebase
//        public FirebaseHelper(DatabaseReference db, Context context, ListView mListView) {
//            this.db = db;
//            this.context = context;
//            this.mListView = mListView;
//            this.getData();
//        }
//
//        //get data from firebase
//        public ArrayList<Offre> getData() {
//            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//            DatabaseReference offreRef = rootRef.child("offres");
//            ValueEventListener valueEventListener = new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
////                    List<Offre> offreList = new ArrayList<>();
//                    offres.clear();
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        Offre offre = ds.getValue(Offre.class);
//                        offres.add(offre);
//                    }
//
//                    //Do what you need to do with your list
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
////                    Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
//                }
//            };
//            offreRef.addListenerForSingleValueEvent(valueEventListener);
//            return offres;
//        }
//
//
//    }
    private final static String TAG = "MainActivity";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference db;
//    private FirebaseHelper helper;
    private ListView mListData;
    private SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm'Z'");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListData = findViewById(R.id.listData);
        firebaseAuth = FirebaseAuth.getInstance();
        ArrayList<Offre> offres = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("offres");

// Read from the database
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot !=null && dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    List<Object> objectList = new ArrayList<>();
                    offres.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Offre offre = ds.getValue(Offre.class);
                        offres.add(offre);
                        mListData.setAdapter(new OffreListAdapter(MainActivity.this, R.layout.list_detail, offres));
                    }
                }
                Log.d(TAG, "Value is: " + offres);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        mListData.setAdapter(new OffreListAdapter(MainActivity.this, R.layout.list_detail, offres));
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}