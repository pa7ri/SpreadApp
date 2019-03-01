package com.ucm.informatica.spread.Data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ucm.informatica.spread.Contracts.AlertContract;
import com.ucm.informatica.spread.Contracts.PosterContract;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static com.ucm.informatica.spread.Utils.Constants.Contract.CONTRACT_ADDRESS_ALERT;
import static com.ucm.informatica.spread.Utils.Constants.Contract.CONTRACT_ADDRESS_POSTER;
import static com.ucm.informatica.spread.Utils.Constants.Wallet.WALLET_FILE;

public class LocalWallet {
    private String filenameWallet;
    private String passwordWallet;
    private Credentials walletCredentials;

    private SmartContract smartContract;
    private AlertContract alertContract;
    private PosterContract posterContract;

    private Activity view;

    public LocalWallet(Activity mainActivity){
        view = mainActivity;
        passwordWallet = "password";
    }

    public LocalWallet(Activity mainActivity, String password){
        view = mainActivity;
        passwordWallet = password;
    }

    public void createWallet(String password, String filePath) {
        filenameWallet="";
        try {
            filenameWallet = WalletUtils.generateLightNewWalletFile(password, new File(filePath));
            updateWalletStored(filenameWallet);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException | IOException | CipherException e) {
            Log.e("TAG", e.getMessage());
        }
    }

    public Credentials loadWallet(String password, String filePath) {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(password, filePath + "/" + filenameWallet);
        }catch (IOException | CipherException e) {
            Log.e("TAG", e.getMessage());
        }
        return credentials;
    }

    public void loadContract(Web3j web3j){
        smartContract = new SmartContract(web3j, walletCredentials);
        alertContract = smartContract.loadAlertSmartContract(CONTRACT_ADDRESS_ALERT);
        posterContract = smartContract.loadPosterSmartContract(CONTRACT_ADDRESS_POSTER);
    }

    public boolean existWallet(){
        filenameWallet = readWalletStored();
        return !filenameWallet.isEmpty();
    }

    public String getPasswordWallet() {
        return "password"; //TODO : read local storage
    }

    public String getFilenameWallet() {
        return filenameWallet;
    }


    public void setCredentials(Credentials credentials) {
        walletCredentials = credentials;
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
