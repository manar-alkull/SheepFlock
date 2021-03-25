package com.example.sheepflock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocationManager {

    private LocationManager locationManager;
    //private Criteria criteria;
    //private String provider;


    public MyLocationManager(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //criteria = new Criteria();
        //criteria.setAccuracy(Criteria.ACCURACY_FINE);

    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates(){
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1,
                0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        onLocationReceived.sendToAll(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
    }

    public GenericListenersRegister<Location,OnLocationReceivedListener> onLocationReceived=new GenericListenersRegister<Location, OnLocationReceivedListener>() {
        @Override
        protected void doForEachListener(OnLocationReceivedListener onLocationReceivedListener, Location location) {
            onLocationReceivedListener.OnLocationReceived(location.getLatitude(),location.getLongitude());
        }
    };

    public interface OnLocationReceivedListener{
        void OnLocationReceived(double latitude, double longitude);
    }
}
