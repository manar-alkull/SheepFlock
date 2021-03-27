package com.example.sheepflock;

import androidx.fragment.app.FragmentActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private static MapsActivity mapsActivity=null;
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
                preview_();
            }
        });

        mapsActivity=this;
        preview_();
    }

    private void preview(){
        if(mapsActivity!=null)
            mapsActivity.preview_();
    }
    private void preview_() {
        TextView txtId_preview=findViewById(R.id.txtId_preview);
        TextView txtNikName_preview=findViewById(R.id.txtNikName_preview);
        TextView txtWeight_preview=findViewById(R.id.txtWeight_preview);
        TextView txtLastFeedDate_preview=findViewById(R.id.txtLastFeedDate_preview);
        TextView txtIsHungry_preview=findViewById(R.id.txtIsHungry_preview);

        txtId_preview.setText(sheep.id+"");
        txtNikName_preview.setText(sheep.nikName);
        txtWeight_preview.setText(sheep.weight+"");

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        txtLastFeedDate_preview.setText(df.format("yyyy-MM-dd hh:mm:ss a", sheep.lastFeedDate));

        txtIsHungry_preview.setText(sheep.isHungry()?getResources().getString(R.string.yes):getResources().getString(R.string.no));
        if(sheep.isHungry()){
            txtIsHungry_preview.setText(getResources().getString(R.string.yes));
            txtIsHungry_preview.setTextColor(Color.RED);
        }else {
            txtIsHungry_preview.setText(getResources().getString(R.string.no));
            txtIsHungry_preview.setTextColor(Color.GREEN);
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
        LatLng sydney = new LatLng(sheep.latitude, sheep.longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(sheep.nikName));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomLevel));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }




    private BroadcastReceiver reciever = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            preview_();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter1 = new IntentFilter("com.example.sheepflock.alarm");
        registerReceiver(reciever,filter1);
    }
    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(reciever);
    }
}
