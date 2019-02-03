package com.ucm.informatica.spread.Contracts;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.6.0.
 */
public class NContract extends Contract implements Serializable{
    private static final String BINARY = "608060405234801561001057600080fd5b506102d1806100206000396000f3fe60806040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806317d7de7c14610051578063c47f0027146100e1575b600080fd5b34801561005d57600080fd5b506100666101a9565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100a657808201518184015260208101905061008b565b50505050905090810190601f1680156100d35780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156100ed57600080fd5b506101a76004803603602081101561010457600080fd5b810190808035906020019064010000000081111561012157600080fd5b82018360208201111561013357600080fd5b8035906020019184600183028401116401000000008311171561015557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506101e6565b005b60606040805190810160405280600881526020017f726563696269646f000000000000000000000000000000000000000000000000815250905090565b80600090805190602001906101fc929190610200565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061024157805160ff191683800117855561026f565b8280016001018555821561026f579182015b8281111561026e578251825591602001919060010190610253565b5b50905061027c9190610280565b5090565b6102a291905b8082111561029e576000816000905550600101610286565b5090565b9056fea165627a7a72305820c85bb8a63893531a6194f9029bcfc39bd965b50bcc354ae58332202c3de355600029";

    public static final String FUNC_GETNAME = "getName";

    public static final String FUNC_SETNAME = "setName";

    protected NContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> getName() {
        final Function function = new Function(FUNC_GETNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> setName(String n) {
        final Function function = new Function(
                FUNC_SETNAME, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(n)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<NContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(NContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<NContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(NContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static NContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static NContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }
}
