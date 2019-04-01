package com.ucm.informatica.spread.Data;

import android.util.Log;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class LocalWallet {
    private String passwordWallet;
    private Credentials walletCredentials;


    public LocalWallet(String password){
        passwordWallet = password;
    }

    public String createWallet(String filePath) {
        String filenameWallet = "";
        try {
            filenameWallet = WalletUtils.generateLightNewWalletFile(passwordWallet, new File(filePath));
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException | IOException | CipherException e) {
            Log.e("CREATE WALLET", e.getMessage());
        }
        return filenameWallet;
    }

    public Credentials loadWallet(String filePath, String filenameWallet) {
        Credentials credentials = null;
        try {
            credentials = WalletUtils.loadCredentials(passwordWallet, filePath + "/" + filenameWallet);
        }catch (IOException | CipherException e) {
            Log.e("LOAD WALLET", e.getMessage());
        }
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        walletCredentials = credentials;
    }

    public Credentials getCredentials() {
        return walletCredentials;
    }


}
