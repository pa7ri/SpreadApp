package com.ucm.informatica.spread.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import static com.ucm.informatica.spread.Utils.Constants.Notifications.TOPICS;

public class Notification {

    @SerializedName("to")
    private String to;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    @SerializedName("data")
    private Map<String, String> data;

    public Notification(String title, String body, String to, Map<String, String> data){
        this.to = TOPICS+to;
        this.title = title;
        this.body = body;
        this.data = data;
    }
}
