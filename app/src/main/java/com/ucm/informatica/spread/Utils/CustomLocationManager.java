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
        assert addresses != null;
        return addresses.get(0);
    }

    public String getFormatAddress(Double latitude, Double longitude){
        Address address = getAddress(latitude, longitude);
        String addressString;
        if(address.getThoroughfare() != null && address.getSubThoroughfare() != null) {
            addressString = address.getThoroughfare() + ", " + address.getSubThoroughfare() + "\n";
        } else {
            addressString = getLineAddress(latitude,longitude).split(",")[0] + "\n";
        }
        return addressString + address.getLocality() + "\n"
                + address.getPostalCode() + " - " + address.getSubAdminArea() + "\n"
                + address.getCountryName();
    }

    public String getLineAddress(Double latitude, Double longitude){
        Address address = getAddress(latitude, longitude);
        return address.getAddressLine(0);
    }

    public String getPostalCode(Location location){
        Address address = getAddress(location.getLatitude(), location.getLongitude());
        return address.getPostalCode();
    }

}
