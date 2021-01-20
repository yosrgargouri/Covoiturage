package com.example.cov;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cov.model.Offre;
import com.example.cov.model.OffreDetail;
import com.example.cov.model.Request;
import com.example.cov.model.StatusEnum;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {


    private final static String TAG = "MainActivity";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbOffre;
    private DatabaseReference dbRequest;
    private ListView mListData;
    private SimpleDateFormat ISO_FORMAT = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm'Z'");
    private EditText mHeureDepart;
    private EditText mAdresseDestination;
    private EditText mAdresseDepart;
    private EditText mNombrePlace;
    private EditText mPrix;
    private EditText mTelephone;
    private EditText departureCitySh;
    private EditText destinationCitySh;
    private EditText nombrePlaceSh;
    private Button saveBtn;
    private Button btnSearch;
    private FirebaseUser firebaseUser;
    private ArrayList<OffreDetail> offres = new ArrayList<>();


//    BtnClickListener btnClickListener = new BtnClickListener() {

    //      @Override
    //    public void onBtnClick(int position) {
    // TODO Auto-generated method stub
    // Call your function which creates and shows the dialog here
    //  }
    //};

    BtnClickListener btnRequestListener = new BtnClickListener() {

        @Override
        public void onBtnClick(Offre offre, String key, EditText numberPlaceToRequest, Dialog dialog) {
            // TODO Auto-generated method stub
            // Call your function which creates and shows the dialog here
//            changeMoneda(position);
            //    offre.setNombre_place(offre.getNombre_place() - 1);
            //    updateOffre(key,offre);
            Request request = new Request();
            request.setEmail_request(firebaseAuth.getCurrentUser().getEmail());
            request.setNombre_place(!numberPlaceToRequest.getText().toString().isEmpty() ? Integer.parseInt(numberPlaceToRequest.getText().toString()) : 1);
            request.setOffre_key(key);
            request.setStatus(StatusEnum.PENDING);

            dbRequest.push().setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MainActivity.this, "The request has been sent with success", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error ", e);
                    Toast.makeText(MainActivity.this, "request failed.", Toast.LENGTH_LONG).show();

                }
            });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListData = findViewById(R.id.listData);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DrawerLayout relativeLayout2 = findViewById(R.id.relativeLayout2);
        NavigationView navigationview = findViewById(R.id.navigationview);

        dbOffre = FirebaseDatabase.getInstance().getReference("offres");
        dbRequest = FirebaseDatabase.getInstance().getReference("requests");

        // Read from the database
        dbOffre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot != null && dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    List<Object> objectList = new ArrayList<>();
                    offres.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Offre offre = ds.getValue(Offre.class);
                        OffreDetail offreDetail = new OffreDetail(offre);
                        offreDetail.setKey(ds.getKey());
                        if (offreDetail.getNombre_place() > 0)
                            offres.add(offreDetail);
                        mListData.setAdapter(new OffreListAdapter(MainActivity.this, R.layout.list_detail, offres, btnRequestListener));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        mListData.setAdapter(new OffreListAdapter(MainActivity.this, R.layout.list_detail, offres, btnRequestListener));

        mListData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        Button mButtonPlus = (Button) findViewById(R.id.buttonPlus);
        mButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogAdd();
            }
        });
        Button mButtonFilter = (Button) findViewById(R.id.buttonFilter);
        mButtonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialogFilter();
            }
        });

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!relativeLayout2.isDrawerOpen(GravityCompat.START))
                    relativeLayout2.openDrawer(GravityCompat.START);
                else relativeLayout2.closeDrawer(GravityCompat.END);
            }
        });
        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuNotifications:
                        startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                        return true;
                    case R.id.menuLogout:
                        logout();
                    case R.id.menuRequest:
                        startActivity(new Intent(getApplicationContext(), RequestActivity.class));
//                    case R.id.menuProfile:
//                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    default:
                        return true;
                }
            }
        });

    }


    public void displayDialogFilter() {

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("ADD REQUEST");
        dialog.setContentView(R.layout.activity_find);
        dialog.show();

        //Find widgets
        departureCitySh = dialog.findViewById(R.id.departureCitySh);
        destinationCitySh = dialog.findViewById(R.id.destinationCitySh);
        nombrePlaceSh = dialog.findViewById(R.id.nombrePlaceSh);


        btnSearch = dialog.findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                ArrayList<OffreDetail> offresFilter = (ArrayList<OffreDetail>)
                        offres.stream().filter(elt -> !departureCitySh.getText().toString().isEmpty() ? elt.getAdresse_depart().equalsIgnoreCase(departureCitySh.getText().toString()) : true
                                && !destinationCitySh.getText().toString().isEmpty() ? elt.getAdresse_destination().equalsIgnoreCase(destinationCitySh.getText().toString()) : true
                                && elt.getNombre_place() >= (!nombrePlaceSh.getText().toString().isEmpty() ? Integer.parseInt(nombrePlaceSh.getText().toString()) : 0)
                        ).collect(Collectors.toList());
                mListData.setAdapter(new OffreListAdapter(MainActivity.this, R.layout.list_detail, offresFilter, btnRequestListener));
                dialog.cancel();
            }
        });
    }

    public void displayDialogAdd() {

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("ADD REQUEST");
        dialog.setContentView(R.layout.add_dialog);
        dialog.show();


        //Find widgets
        mHeureDepart = dialog.findViewById(R.id.heureDepart);
        mAdresseDestination = dialog.findViewById(R.id.adresseDestination);
        mAdresseDepart = dialog.findViewById(R.id.adresseDepart);
        mNombrePlace = dialog.findViewById(R.id.destinationCitySh);
        mPrix = dialog.findViewById(R.id.prix);
        mTelephone = dialog.findViewById(R.id.telephone);

        saveBtn = dialog.findViewById(R.id.btnSearch);

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

                String key = dbOffre.push().getKey();
                dbOffre.child(key).setValue(offre).addOnSuccessListener(new OnSuccessListener<Void>() {
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

//    public void logout(View view) {
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//        finish();
//    }

    public void clearFilter(View view) {
        mListData.setAdapter(new OffreListAdapter(MainActivity.this, R.layout.list_detail, offres, btnRequestListener));
    }


    public void logout(View view) {
        logout();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}