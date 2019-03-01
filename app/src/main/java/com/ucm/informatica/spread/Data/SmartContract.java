package com.ucm.informatica.spread.Utils;

import com.ucm.informatica.spread.Contracts.AlertContract;
import com.ucm.informatica.spread.Contracts.PosterContract;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.io.Serializable;


public class SmartContract implements Serializable {
    private Web3j web3j;
    private Credentials credentials;

    public SmartContract(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public AlertContract loadAlertSmartContract(String addressContract) {
        return AlertContract.load(
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
