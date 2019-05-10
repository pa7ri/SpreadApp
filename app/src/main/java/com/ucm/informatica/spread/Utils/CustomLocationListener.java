package com.ucm.informatica.spread.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;

import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.NAME_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.NOTIFICATION_TOPIC_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.PROFILE_PREF;


public class CustomLocationListener implements LocationListener {

    private Location latestLocation;
    private CustomLocationManager locationManager;
    private SharedPreferences sharedPreferences;

    public CustomLocationListener(Context context){
        locationManager = new CustomLocationManager(context);
        sharedPreferences = context.getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE);

        latestLocation = new Location("");
        //Colegio : 40.449010, -3.720407
        // FDI : 40.452687,-3.733738
        latestLocation.setLatitude(40.452687);
        latestLocation.setLongitude(-3.733738);
    }

    @Override
    public void onLocationChanged(Location location) {
        unregisterLastNotificationTopic(latestLocation);
        //latestLocation = location;
        registerNewNotificationTopic(latestLocation);
        storeNotificationTopicLocally(latestLocation);
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

    public Location getLatestLocation() {
        return latestLocation;
    }

    public void unregisterLastNotificationTopic(Location location) {
        if(location != null) {
            String postalCode = locationManager.getPostalCode(location);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(postalCode);
        }
    }

    public void registerNewNotificationTopic(Location location) {
        if(location != null) {
            String postalCode = locationManager.getPostalCode(location);
            FirebaseMessaging.getInstance().subscribeToTopic(postalCode);
        }
    }

    private void storeNotificationTopicLocally(Location location) {
        if (location != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String postalCode = locationManager.getPostalCode(location);
            editor.putString(NOTIFICATION_TOPIC_PREF, postalCode);
            editor.apply();
        }
    }
}