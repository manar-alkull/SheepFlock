package com.example.sheepflock;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sheepflock.alarm.AlarmHandler;
import com.example.sheepflock.system.SheepContentManager;
import com.example.sheepflock.ui.home.HomeFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Sheep sheep;
    SheepContentManager sheepContentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        int itemId = getIntent().getIntExtra("itemId",-1);

        sheepContentManager=new SheepContentManager(this);

        sheep= sheepContentManager.getSheeps().get(itemId);

        Button feedBtn= findViewById(R.id.feedBtn);
        final Context currentContext=this;
        feedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheep.lastFeedDate= Calendar.getInstance();
                //AlarmHandler.cancelAlarm(currentContext,sheep);
                AlarmHandler.setAlarm(MainActivity.mainActivityContext,sheep);
                sheepContentManager.saveSheep(sheep);
                HomeFragment.refresh();
            }
        });
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
        LatLng sydney = new LatLng(sheep.latitude, sheep.longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(sheep.nikName));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomLevel));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
