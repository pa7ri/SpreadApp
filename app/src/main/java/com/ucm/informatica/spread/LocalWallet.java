package com.ucm.informatica.spread;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.ucm.informatica.spread.Activities.MainTabActivity;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.ucm.informatica.spread.Constants.Wallet.WALLET_FILE;

public class LocalWallet {
    private Web3j web3j;
    private String filenameWallet;
    private String passwordWallet;
    private Credentials walletCredentials;
    private Activity view;

    public LocalWallet(Activity mainActivity){
        view = mainActivity;
        passwordWallet = "password";
    }

    public LocalWallet(Activity mainActivity, String password){
        view = mainActivity;
        passwordWallet = password;
    }

    public Web3j initWeb3j(String filepath) {
        if(web3j == null) {
            web3j = Web3jFactory.build(new HttpService(Constants.INFURA_PATH + Constants.INFURA_PUBLIC_PROYECT_ADDRESS));
        }
        web3j.web3ClientVersion().observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Web3ClientVersion>() {
                    @Override
                    public void onCompleted() {
                        Timber.e("Conexión completada");
                        if(!existWallet()){
                            createWallet(passwordWallet, filepath);
                        }
                        if(filenameWallet!= null && !filenameWallet.isEmpty()) {
                            walletCredentials = loadWallet(passwordWallet, filepath);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("Ha habido un error");
                    }

                    @Override
                    public void onNext(Web3ClientVersion web3ClientVersion) {
                        Timber.e("Conectado a %s", web3ClientVersion.getWeb3ClientVersion());
                    }
                });

        return web3j;
    }

    private void createWallet(String password, String filePath) {
        filenameWallet="";
        try {
            filenameWallet = WalletUtils.generateLightNewWalletFile(password, new File(filePath));
            updateWalletStored(filenameWallet);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException | IOException | CipherException e) {
            Timber.e(e);
        }
    }

    private Credentials loadWallet(String password, String filePath) {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(password, filePath + "/" + filenameWallet);
        }catch (IOException | CipherException e) {
            Timber.e(e);
        }
        return credentials;
    }

    private boolean existWallet(){
        filenameWallet = readWalletStored();
        return !filenameWallet.isEmpty();
    }

    public Credentials getCredentials() {
        return walletCredentials;
    }

    //manage local storage
    private void updateWalletStored(String walletFilename){
        SharedPreferences sharedPref = view.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(WALLET_FILE, walletFilename);
        editor.apply();
    }

    private String readWalletStored(){
        SharedPreferences sharedPref = view.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(WALLET_FILE, "");
    }

}
