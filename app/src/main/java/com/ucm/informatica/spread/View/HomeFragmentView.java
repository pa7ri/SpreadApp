package com.ucm.informatica.spread.View;

public interface HomeFragmentView {

    void initView();

    void setupListeners();

    void showConfirmationTransaction(String result);

    void showErrorTransaction();

    void showErrorGPS();

}
