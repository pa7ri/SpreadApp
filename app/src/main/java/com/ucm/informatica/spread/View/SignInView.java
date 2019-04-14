package com.ucm.informatica.spread.View;

public interface SignInView {

    void initView();

    void setUpListeners();

    void showPasswordError();

    void showLoading();

    void hideLoading();

    void initMainActivity();

    void initTutorialActivity();

    void storeWalletFileLocally(String walletFilename);

    void storePasswordLocally(String password);

    void storeOnBoardingFinishedLocally();

    String getWalletFilePath();

}
