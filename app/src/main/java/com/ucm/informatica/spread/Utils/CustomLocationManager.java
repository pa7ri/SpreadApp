package com.ucm.informatica.spread.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CustomLocationManager {
    private Context context;

    public CustomLocationManager(Context context){
        this.context = context;
    }

    private Address getAddress(Double latitude, Double longitude){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            Log.e("ADDRESS", e.getMessage());
        }
        return addresses.get(0);
    }

    public String getFormatAddress(Double latitude, Double longitude){
        Address address = getAddress(latitude, longitude);
        return address.getThoroughfare() + ", " + address.getSubThoroughfare() + "\n"
                + address.getLocality() + "\n"
                + address.getPostalCode() + " - " + address.getSubAdminArea() + "\n"
                + address.getCountryName();
    }


    public String getPostalCode(Location location){
        Address address = getAddress(location.getLatitude(), location.getLongitude());
        return address.getPostalCode();
    }

}
