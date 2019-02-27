package com.ucm.informatica.spread.View;

public interface HomeFragmentView {

    void initView();

    void setupListeners();

    void showSuccessfulStoredTransition(String result);

    void showErrorTransition();

    void showErrorGPS();

}
