package com.ucm.informatica.spread.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.util.ArrayMap;
import android.util.Log;

import com.ucm.informatica.spread.Contracts.AlertContract;
import com.ucm.informatica.spread.Data.ApiFcmService;
import com.ucm.informatica.spread.Data.ApiTelegramService;
import com.ucm.informatica.spread.Data.ApiUtils;
import com.ucm.informatica.spread.Model.Colours;
import com.ucm.informatica.spread.Model.Notification;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.CustomLocationListener;
import com.ucm.informatica.spread.Utils.CustomLocationManager;
import com.ucm.informatica.spread.View.HomeFragmentView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.*;
import static com.ucm.informatica.spread.Utils.Constants.Notifications.*;


public class HomeFragmentPresenter {

    private AlertContract alertContract;
    private HomeFragmentView homeFragmentView;
    private ApiFcmService apiFcmService;
    private ApiTelegramService apiTelegramService;

    private CustomLocationManager locationManager;

    private int currentTopic;

    public HomeFragmentPresenter(Context context, HomeFragmentView homeFragmentView, AlertContract alertContract){
        this.homeFragmentView = homeFragmentView;
        this.alertContract = alertContract;

        apiFcmService = ApiUtils.getApiFcmService();
        apiTelegramService = ApiUtils.getApiTelegramService();
        locationManager = new CustomLocationManager(context);
    }

    public void start() {
        homeFragmentView.initView();
        homeFragmentView.setupListeners();
    }


    public void onHelpButtonPressed(Location location, Resources resources, SharedPreferences sharedPreferences) {
        if(location != null) {
            sendTelegramNotification(location, sharedPreferences);
            sendPushNotification(location, sharedPreferences);
            homeFragmentView.showSendConfirmation();
            saveData(resources.getString(R.string.button_help),
                    resources.getString(R.string.button_help_description),
                    Double.toString(location.getLongitude()),
                    Double.toString(location.getLatitude()));
        } else {
            homeFragmentView.showErrorGPS();
        }
    }

    private void sendPushNotification(Location location, SharedPreferences sharedPreferences) {
        currentTopic=0;
        sendBroadcastToTopics(location,sharedPreferences);
    }

    private void sendBroadcastToTopics(Location location,SharedPreferences sharedPreferences) {
        Map<String, String> data = new ArrayMap<>();

        CustomLocationListener locationListener = homeFragmentView.getCustomLocationListener();
        locationListener.unregisterLastNotificationTopic(location);
        data.put(NOTIFICATION_DATA, notificationToJson(location.getLatitude(),
                location.getLongitude(), sharedPreferences));
        data.put(NOTIFICATION_DATA_TITLE, NOTIFICATION_DATA_TITLE_CONTENT);
        data.put(NOTIFICATION_DATA_SUBTITLE, NOTIFICATION_DATA_SUBTITLE_CONTENT);

        final int radius = sharedPreferences.getInt(RADIUS_PREF,0);
        String postalCode = sharedPreferences.getString(NOTIFICATION_TOPIC_PREF,"");
        String condition = getNotificationTopic(radius,postalCode);

        apiFcmService.sendBroadcastNotification(new Notification(NOTIFICATION_DATA_TITLE_CONTENT,
                NOTIFICATION_DATA_SUBTITLE_CONTENT, condition, data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JSONObject>() {
                    @Override
                    public void onCompleted() {
                        if((radius==2 && currentTopic<10) || (radius==3 && currentTopic<100)){
                            sendBroadcastToTopics(location,sharedPreferences);
                        } else {
                            locationListener.registerNewNotificationTopic(location);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("SEND NOTIFICATION",e.getMessage());
                        homeFragmentView.showErrorTransaction();
                        locationListener.registerNewNotificationTopic(location);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {

                    }
                });
    }

    private void sendTelegramNotification(Location location, SharedPreferences sharedPreferences) {
        String telegramMessage = getTelegramMessage(location, sharedPreferences);
        int count = sharedPreferences.getInt(TELEGRAM_GROUPS_NUMBER_PREF, 0);
        for (int i = 0; i < count; i++) {
            apiTelegramService.sendTelegramMessage(
                    sharedPreferences.getString(TELEGRAM_GROUP_CHAT_ID_PREF+i,""),
                    telegramMessage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<JSONObject>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(JSONObject responseBody) {
                        }
                    });
        }
    }

    private String notificationToJson(Double latitude, Double longitude, SharedPreferences sharedPreferences) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(NOTIFICATION_DATA_NAME, sharedPreferences.getString(NAME_PREF,NOTIFICATION_DATA_UNKNOWN));
            jsonObject.put(NOTIFICATION_DATA_AGE, sharedPreferences.getString(AGE_PREF,NOTIFICATION_DATA_UNKNOWN));
            jsonObject.put(NOTIFICATION_DATA_LATITUDE, latitude);
            jsonObject.put(NOTIFICATION_DATA_LONGITUDE, longitude);
            jsonObject.put(NOTIFICATION_DATA_TSHIRT_COLOR, getColor(Colours.values()[sharedPreferences.getInt(TSHIRT_PREF,0)]));
            jsonObject.put(NOTIFICATION_DATA_PANTS_COLOR, getColor(Colours.values()[sharedPreferences.getInt(PANTS_PREF,0)]));
            jsonObject.put(NOTIFICATION_DATA_WATCHWORD_KEY, sharedPreferences.getString(KEY_PREF,NOTIFICATION_DATA_UNKNOWN));
            jsonObject.put(NOTIFICATION_DATA_WATCHWORD_RESPONSE, sharedPreferences.getString(RESPONSE_PREF,NOTIFICATION_DATA_UNKNOWN));
            return jsonObject.toString();
        } catch (JSONException e) {
            Log.e("JSON BUILDER", e.getMessage());
            return "";
        }
    }

    private String getNotificationTopic(int radius, String postalCode){
        String condition="";
        //there is a limit of 5 conditions for each notification
        switch (radius) {
            case 1 :
                condition = "'" + postalCode + "' in topics";
                break;
            case 2 :
                for (int i=currentTopic; i<currentTopic+5; i++) {
                    postalCode = postalCode.substring(0, postalCode.length() - 1) + i;
                    if(i==currentTopic) {
                        condition = "'" + postalCode + "' in topics";
                    } else {
                        condition = condition + " || '" + postalCode + "' in topics";
                    }
                }
                currentTopic+=5;
                break;
            case 3 :
                postalCode = postalCode.substring(0,postalCode.length() - 2) + "00";
                condition = "'" + postalCode + "' in topics";
                for (int i=currentTopic; i<currentTopic+5; i++) {
                    if(i<10) {
                        postalCode = postalCode.substring(0,postalCode.length() - 2) + "0"+ i;
                        if(i==currentTopic) {
                            condition = "'" + postalCode + "' in topics";
                        } else {
                            condition = condition + " || '" + postalCode + "' in topics";
                        }
                    } else {
                        postalCode = postalCode.substring(0, postalCode.length() - 2) + i;
                        if(i==currentTopic) {
                            condition = "'" + postalCode + "' in topics";
                        } else {
                            condition = condition + " || '" + postalCode + "' in topics";
                        }
                    }
                }
                currentTopic+=5;
                break;
            default : condition = "";
        }
        return condition;
    }

    private String getColor(Colours color) {
        switch (color) {
            case Red:
                return "Rojo";
            case Blue:
                return "Azul";
            case Gray:
                return "Gris";
            case Black:
                return "Negro";
            case Green:
                return "Verde";
            case Yellow:
                return "Amarillo";
            case White:
                return "Blanco";
            default:
                return NOTIFICATION_DATA_UNKNOWN;
        }
    }

    private String getTelegramMessage(Location location, SharedPreferences sharedPreferences) {
        String name = sharedPreferences.getString(NAME_PREF,NOTIFICATION_DATA_UNKNOWN);
        return name + " necesita ayuda, está en " +
                locationManager.getLineAddress(location.getLatitude(), location.getLongitude())
                + ". \n" + "https://maps.google.com/?q=" + Double.toString(location.getLatitude())
                + "," + Double.toString(location.getLongitude());
    }

    private void saveData(String title, String description, String longitude, String latitude) {
        if(alertContract != null) {
            alertContract.addAlert(title, description, latitude, longitude, String.valueOf(System.currentTimeMillis())).observable()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (result) -> homeFragmentView.showConfirmationTransaction(),
                            (error) -> homeFragmentView.showErrorTransaction()
                    );
        } else {
            homeFragmentView.showErrorTransaction();
        }

    }
}
