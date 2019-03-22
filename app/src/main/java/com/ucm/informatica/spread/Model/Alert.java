package com.ucm.informatica.spread.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Alert {
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private Long dateTime;

    public Alert(){}

    public Alert(String title, String description, String latitude, String longitude, String dateTime) {
        this.title = title;
        this.description = description;
        this.latitude = Double.valueOf(latitude);
        this.longitude = Double.valueOf(longitude);
        this.dateTime = Long.valueOf(dateTime);
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

    private String formatDate(Long data){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy           HH:mm:ss");
        return df.format(new Date(data));
    }
}
