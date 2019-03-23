package com.ucm.informatica.spread.Data;

import com.ucm.informatica.spread.Model.Notification;

import org.json.JSONObject;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiFcmService {
    @Headers({"Authorization: key=AAAAUJykIF8:APA91bF_NooIH7FSxUHZVFoG5ZjKEM15yggCG9rRWcoHOhgNX8nQhdIHkz6zUsXvhN8nr4sBPwIwwHIVHcsCSB9308pfgpGcGxkD6B5j8Z1fQQ0zrV7S1ANBQs3FwKANnmZ5o8KVGLVA",
            "Content-Type:application/json"})
    @POST("fcm/send")
    Observable<JSONObject> sendBroadcastNotification(@Body Notification notification);
}
