package com.ucm.informatica.spread.Model;

import android.content.Context;

public class Poster extends Event {

    private byte[] image;

    public Poster(Context context, String title, String description, String latitude, String longitude, String dateTime, byte[] image) {
        super(context, title, description, latitude, longitude, dateTime);
        this.image = image;
    }

    public byte[] getImage(){
        return image;
    }

}
