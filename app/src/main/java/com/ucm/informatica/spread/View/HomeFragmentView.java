package com.ucm.informatica.spread.View;

import com.ucm.informatica.spread.Utils.CustomLocationListener;

public interface HomeFragmentView {

    void initView();

    CustomLocationListener getCustomLocationListener();

    void setupListeners();

    void showConfirmationTransaction();

    void showErrorTransaction();

    void showErrorGPS();

    void showSendConfirmation();
}
