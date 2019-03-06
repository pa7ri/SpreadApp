package com.ucm.informatica.spread.Data.Telegram;

import static com.ucm.informatica.spread.Utils.Constants.TELEGRAM_URL;

public class ApiUtils {

    private ApiUtils() {}

    public static ApiService getAPIService() {
        return RetrofitClient.getClient(TELEGRAM_URL).create(ApiService.class);
    }
}
