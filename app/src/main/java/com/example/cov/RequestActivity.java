package com.example.cov;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cov.model.Offre;
import com.example.cov.model.OffreDetail;
import com.example.cov.model.Request;
import com.example.cov.model.RequestDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends AppCompatActivity {

    private final static String TAG = "RequestActivity";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRequest;
    private DatabaseReference dbOffre;

    private ListView mListData;
    private ArrayList<RequestDetail> requestDetails = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_request);
        mListData = findViewById(R.id.listDataOffre);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DrawerLayout relativeLayout2 = findViewById(R.id.relativeLayout2);

        dbRequest = FirebaseDatabase.getInstance().getReference("requests");
        dbOffre = FirebaseDatabase.getInstance().getReference("offres");

        // Read from the database
        dbRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot != null && dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    List<Object> objectList = new ArrayList<>();
                    requestDetails.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Request request = ds.getValue(Request.class);
                        RequestDetail requestDetail = new RequestDetail(request);
                        requestDetail.setRequest_key(ds.getKey());
                        //TODO: add title : get data from offre database

                        dbOffre.child(request.getOffre_key()).getRef().addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshotx) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                if (dataSnapshotx != null && dataSnapshotx.exists() && dataSnapshotx.getChildrenCount() > 0) {
                                    Offre offre = dataSnapshotx.getValue(Offre.class);
                                    if (offre.getEmail().equals(firebaseUser.getEmail())) {
                                        requestDetail.setTitleOffre(offre.getAdresse_depart() + " --> " + offre.getAdresse_destination() + " : " + offre.getHeure_depart());
                                        requestDetails.stream().filter(elt -> elt.getRequest_key().equals(requestDetail.getRequest_key())).findFirst().ifPresent(elt -> requestDetails.remove(elt));
                                        requestDetails.add(requestDetail);
                                        //TODO Delete
                                        requestDetails.stream().filter(elt->elt.getEmail_request().equals(firebaseUser.getEmail())).findFirst().ifPresent(elt-> requestDetails.remove(elt));

                                        mListData.setAdapter(new RequestListAdapter(RequestActivity.this, R.layout.request_detail, requestDetails));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w(TAG, "Failed to read value.", error.toException());
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
//        mListData.setAdapter(new RequestListAdapter(RequestActivity.this, R.layout.request_detail, requestDetails));
    }

}
