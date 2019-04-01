package com.ucm.informatica.spread.Data;

import static com.ucm.informatica.spread.Utils.Constants.Notifications.API_FCM_URL;
import static com.ucm.informatica.spread.Utils.Constants.API_TELEGRAM_URL;

public class ApiUtils {
    private ApiUtils() {}

    public static ApiFcmService getApiFcmService() {
        return RetrofitClient.getClient(API_FCM_URL).create(ApiFcmService.class);
    }

    public static ApiTelegramService getApiTelegramService() {
        return RetrofitClient.getClient(API_TELEGRAM_URL).create(ApiTelegramService.class);
    }
}
