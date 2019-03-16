package com.ucm.informatica.spread.Data;

import com.ucm.informatica.spread.Model.Notification;

import org.json.JSONObject;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiFcmService {
    @Headers({"Authorization: key=API_KEYTOKEN",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Observable<JSONObject> sendBroadcastNotification(@Body Notification notification);
}
