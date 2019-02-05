package com.ucm.informatica.spread;

import com.ucm.informatica.spread.Contracts.CoordContract;
import com.ucm.informatica.spread.Contracts.NameContract;

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

    public CoordContract loadSmartContract(String addressContract) {
        return CoordContract.load(
                addressContract,
                web3j,
                credentials,
                Constants.Contract.GAS_PRICE,
                Constants.Contract.GAS_LIMIT);
    }

    //TODO : remove not used method
    public String writeNameToSmartContract(NameContract nameContract, String data) {
        nameContract.setName(data).observable()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    (result) -> {
                        String s = result.getBlockNumber().toString();
                        String s1 = result.getGasUsed().toString();
                    } ,
                    (error) -> {
                        int a =4*2;
                    }
            );
        return "Guardado";
    }

    public String readNameFromSmartContract(NameContract nameContract) {
         nameContract.getName().observable()
             .subscribeOn(Schedulers.newThread())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(
                 (result) -> {
                     String s = result;
                 } ,
                 (error) -> {
                    int a =4*2;
                 }
             );
        return "Algo ha leido";
    }
}
