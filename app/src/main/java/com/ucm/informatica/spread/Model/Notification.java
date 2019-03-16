package com.ucm.informatica.spread.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import static com.ucm.informatica.spread.Utils.Constants.Notifications.TOPICS;
import static com.ucm.informatica.spread.Utils.Constants.Notifications.TOPIC_ALL_DEVICES;

public class Notification {

    @SerializedName("to")
    private String to=TOPICS+TOPIC_ALL_DEVICES;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    @SerializedName("data")
    private Map<String, String> data;

    public Notification(String title, String body, Map<String, String> data){
        this.title = title;
        this.body = body;
        this.data = data;
    }
}
