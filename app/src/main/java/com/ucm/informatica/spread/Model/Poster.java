package com.ucm.informatica.spread.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.spongycastle.util.encoders.Base64;

public class Poster extends Alert {

    private byte[] image;

    public Poster(String title, String description, String latitude,
                  String longitude, String dateTime, byte[] image) {
        super(title, description, latitude, longitude, dateTime);
        this.image = image;
    }

    public Poster(String jsonObject) {
        super();
        try {
            JSONObject posterJSONObject = new JSONObject(jsonObject);
            setTitle(posterJSONObject.getString("title"));
            setDescription(posterJSONObject.getString("description"));
            setLatitudeLongitude(posterJSONObject.getString("latitude"),
                                        posterJSONObject.getString("longitude"));
            setDateTime(posterJSONObject.getString("datetime"));
            this.image = Base64.decode(posterJSONObject.getString("image"));
        } catch (JSONException e) {
            Log.e("JSON parser", e.getMessage());
        }
    }

    public byte[] getImage(){
        return image;
    }

    public String toJson(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("title", getTitle());
            jsonObject.put("description", getDescription());
            jsonObject.put("latitude", getLatitude());
            jsonObject.put("longitude", getLongitude());
            jsonObject.put("datetime", getDateTime());
            jsonObject.put("image", Base64.toBase64String(getImage()));

            return jsonObject.toString();
        } catch (JSONException e) {
            Log.e("JSON builder", e.getMessage());
            return "";
        }
    }

}
