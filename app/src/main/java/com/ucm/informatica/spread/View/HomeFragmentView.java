package com.ucm.informatica.spread.View;

import com.ucm.informatica.spread.Utils.CustomLocationListener;

public interface HomeFragmentView {

    void initView();

    void setupListeners();

    void showErrorTransaction();

    void showErrorGPS();

    void showSendConfirmation();

    void saveData(String title, String description, String latitude, String longitude);

    CustomLocationListener getCustomLocationListener();
}
