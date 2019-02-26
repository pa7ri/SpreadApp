package com.ucm.informatica.spread.View;

public interface MainTabView {

    void initView();

    void loadDataEventSmartContract(String title, String description, String latitude, String longitude, String dataTime);

    void loadDataPosterSmartContract(String title, String description, String latitude, String longitude, String dataTime, byte[] image);

    void showErrorTransition();

    void showLoading();

    void hideLoading();
}
