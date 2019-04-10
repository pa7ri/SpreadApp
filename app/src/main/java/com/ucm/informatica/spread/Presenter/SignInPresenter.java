package com.ucm.informatica.spread.Presenter;

import android.util.Log;

import com.ucm.informatica.spread.Data.LocalWallet;
import com.ucm.informatica.spread.Utils.Constants;
import com.ucm.informatica.spread.View.SignInView;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInPresenter {

    private SignInView signInView;
    private boolean isOnBoardingComplete;

    public SignInPresenter(SignInView signInView, boolean isOnBoardingComplete){
        this.signInView = signInView;
        this.isOnBoardingComplete = isOnBoardingComplete;
    }

    public void start() {
        if(isOnBoardingComplete) {
            signInView.initMainActivity();
        } else {
            signInView.initView();
            signInView.setUpListeners();
        }
    }

    public void onContinuePressed(String password) {
        if (password.isEmpty() || password.length() < 8 || password.length() > 20) {
            signInView.showPasswordError();
        } else {
            signInView.showLoading();
            signInView.storePasswordLocally(password);
            createWallet(password);
        }
    }

    private void createWallet(String password) {
        LocalWallet localWallet = new LocalWallet(password);
        Web3j web3j = Web3jFactory.build(new HttpService(Constants.INFURA_PATH + Constants.INFURA_PUBLIC_PROYECT_ADDRESS));
        web3j.web3ClientVersion().observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Web3ClientVersion>() {
                    @Override
                    public void onCompleted() {
                        String walletFile = localWallet.createWallet(signInView.getWalletFilePath());
                        signInView.hideLoading();
                        signInView.storeWalletFileLocally(walletFile);
                        signInView.storeOnBoardingFinishedLocally();
                        signInView.initMainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ERROR WEB3 CONNECTION",e.getMessage());
                    }

                    @Override
                    public void onNext(Web3ClientVersion web3ClientVersion) {
                        Log.i("CONNECTED TO %s", web3ClientVersion.getWeb3ClientVersion());
                    }
                });
    }


}
