package com.ucm.informatica.spread.View;

public interface MainTabView {

    void initView();

    void loadDataSmartContract(String title, String description, String latitude, String longitude, String dataTime);

    void showErrorTransition();

    void showLoading();

    void hideLoading();
}
