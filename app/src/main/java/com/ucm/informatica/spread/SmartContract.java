package com.ucm.informatica.spread;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SmartContract {
    private Web3j web3j;
    private Credentials credentials;

    public SmartContract(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public NameContract loadSmartContract(String addressContract) {
        return NameContract.load(
                addressContract,
                web3j,
                credentials,
                Constants.Contract.GAS_PRICE,
                Constants.Contract.GAS_LIMIT);
    }

    public String writeNameToSmartContract(NameContract nameContract, String data)
            throws ExecutionException, InterruptedException {
        Future<TransactionReceipt> tReceipt = nameContract.setName(data).sendAsync();
        return "BlockNum : " + tReceipt.get().getBlockNumber()
                + " , GasUsed : "+ tReceipt.get().getBlockNumber();
    }

    public String readNameFromSmartContract(NameContract nameContract)
            throws ExecutionException, InterruptedException {
        Future<String> gettingName = nameContract.getName().sendAsync();
        return gettingName.get();
    }
}
