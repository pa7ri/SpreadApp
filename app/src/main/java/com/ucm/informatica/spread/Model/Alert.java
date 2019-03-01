package com.ucm.informatica.spread.Model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Event {
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private Long dateTime;
    private String place;

    private Context context;

    public Event(Context context){
        this.context = context;
    }

    public Event(Context context, String title, String description, String latitude, String longitude, String dateTime) {
        this.context = context;
        this.title = title;
        this.description = description;
        this.latitude = Double.valueOf(latitude);
        this.longitude = Double.valueOf(longitude);
        this.dateTime = Long.valueOf(dateTime);
        this.place = getLocation();
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setLatitudeLongitude(String latitude,String longitude){
        this.latitude = Double.valueOf(latitude);
        this.longitude = Double.valueOf(longitude);
        this.place = getLocation();
    }

    public void setDateTime(String dateTime){
        this.dateTime = Long.valueOf(dateTime);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public String getDateTimeFormat() {
        return formatDate(dateTime);
    }

    public String getPlace() {
        return place;
    }

    private String getLocation(){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1);
        } catch (IOException e) {
            Log.e("TAG", e.getMessage());
        }
        return addresses.get(0).getLocality();
    }

    private String formatDate(Long data){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy \n HH:mm:ss");
        return df.format(new Date(data));
    }
}
