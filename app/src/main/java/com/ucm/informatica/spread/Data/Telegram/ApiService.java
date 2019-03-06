package com.ucm.informatica.spread.Data.Telegram;

import org.json.JSONObject;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiService {
    @POST("sendMessage")
    @FormUrlEncoded
    Observable<JSONObject> sendTelegramMessage(@Field("chat_id") String chatId,
                                    @Field("text") String bodyText);
}
