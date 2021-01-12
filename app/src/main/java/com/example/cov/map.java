package com.example.cov;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;


public class map extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Button btn_pick;
    private List<Place.Field> fields;
    final int place_Piker_req_code = 1;
    String name;
    LatLng latLng;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_pick = findViewById(R.id.btn_pick);
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyDNysXu_ZDqzfaVXpitFOlPvqKulQ3cUws");

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(map.this);
                startActivityForResult(intent, place_Piker_req_code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case place_Piker_req_code:
                if (resultCode == RESULT_OK) {

                    Place place = Autocomplete.getPlaceFromIntent(data);
                    name = place.getName();
                    latLng = place.getLatLng();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    mMap.animateCamera(update);
                }
                break;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng tunis = new LatLng(36, 10);
        mMap.addMarker(new MarkerOptions().position(tunis).title("Marker in tunis"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tunis));
    }

}
