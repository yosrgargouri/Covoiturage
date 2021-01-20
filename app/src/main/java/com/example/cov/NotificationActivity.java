package com.example.cov;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cov.model.Offre;
import com.example.cov.model.Request;
import com.example.cov.model.RequestDetail;
import com.example.cov.model.StatusEnum;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private final static String TAG = "NotificationActivity";
    private DatabaseReference dbRequest;
    private DatabaseReference dbOffre;

    private ListView mListData;
    private final ArrayList<RequestDetail> requestDetails = new ArrayList<>();

    BtnRequestClickListener btnAcceptListener = new BtnRequestClickListener() {
        @Override
        public void onBtnClick(RequestDetail requestDetail, StatusEnum status) {
            updateStatusRequestAndUpdateOffre(requestDetail.getRequest_key(), status, requestDetail);
        }
    };
    BtnRequestClickListener btnCancelListener = new BtnRequestClickListener() {
        @Override
        public void onBtnClick(RequestDetail requestDetail, StatusEnum status) {
            cancelRequest(requestDetail.getRequest_key(), status);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_request);
        mListData = findViewById(R.id.listDataOffre);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        dbRequest = FirebaseDatabase.getInstance().getReference("requests");
        dbOffre = FirebaseDatabase.getInstance().getReference("offres");

        // Read from the database
        dbRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot != null && dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    requestDetails.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Request request = ds.getValue(Request.class);
                            RequestDetail requestDetail = new RequestDetail(request);
                            requestDetail.setRequest_key(ds.getKey());

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
                                            requestDetail.setOffre(offre);
                                            if (StatusEnum.PENDING.equals(request.getStatus()) && !firebaseUser.getEmail().equals(request.getEmail_request())) {
                                                requestDetails.add(requestDetail);
                                                mListData.setAdapter(new RequestListAdapter(NotificationActivity.this, R.layout.request_detail, requestDetails, btnAcceptListener, btnCancelListener));
                                            }
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
                    if (requestDetails.isEmpty()) {

                        mListData.setAdapter(null);
                    }
                } else {
                    mListData.setAdapter(null);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void updateNumberPlaceOffre(String key, int numberPlace) {
        dbOffre.child(key).child("nombre_place").setValue(numberPlace).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(NotificationActivity.this, "offre updated.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelRequest(String key, StatusEnum status) {
        dbRequest.child(key).child("status").setValue(status.name()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(NotificationActivity.this, "Request updated.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateStatusRequestAndUpdateOffre(String keyRequest, StatusEnum status, RequestDetail requestDetail) {
        if (requestDetail.getNombre_place() > requestDetail.getOffre().getNombre_place()) {
            Toast.makeText(NotificationActivity.this, "The number of places requested is greater than the number of free places", Toast.LENGTH_SHORT).show();
        } else {
            dbRequest.child(keyRequest).child("status").setValue(status.name()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(NotificationActivity.this, "Request updated.", Toast.LENGTH_SHORT).show();
                    updateNumberPlaceOffre(requestDetail.getOffre_key(), requestDetail.getOffre().getNombre_place() - requestDetail.getNombre_place());

                }
            });
        }
    }

}
