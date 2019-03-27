package com.ucm.informatica.spread.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import static com.ucm.informatica.spread.Utils.Constants.Notifications.TOPICS;

public class Notification {

    @SerializedName("condition")
    private String condition;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    @SerializedName("data")
    private Map<String, String> data;

    public Notification(String title, String body, String condition, Map<String, String> data){
        this.condition = condition;
        this.title = title;
        this.body = body;
        this.data = data;
    }
}
