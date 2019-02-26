package com.ucm.informatica.spread.Utils;

import com.ucm.informatica.spread.Contracts.CoordContract;
import com.ucm.informatica.spread.Contracts.PosterContract;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.io.Serializable;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SmartContract implements Serializable {
    private Web3j web3j;
    private Credentials credentials;

    public SmartContract(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public CoordContract loadCoordSmartContract(String addressContract) {
        return CoordContract.load(
                addressContract,
                web3j,
                credentials,
                Constants.Contract.GAS_PRICE,
                Constants.Contract.GAS_LIMIT);
    }

    public PosterContract loadPosterSmartContract(String addressContract) {
        return PosterContract.load(
                addressContract,
                web3j,
                credentials,
                Constants.Contract.GAS_PRICE,
                Constants.Contract.GAS_LIMIT);
    }
}
