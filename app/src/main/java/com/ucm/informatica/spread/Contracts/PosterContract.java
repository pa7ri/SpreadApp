package com.ucm.informatica.spread.Contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
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
public class PosterContract extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5061057a806100206000396000f3fe608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680635c0c041514610067578063824ee4891461011b578063d57bcaa0146101e3578063dc913a8b1461020e575b600080fd5b34801561007357600080fd5b506100a06004803603602081101561008a57600080fd5b81019080803590602001909291905050506102c2565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100e05780820151818401526020810190506100c5565b50505050905090810190601f16801561010d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561012757600080fd5b506101e16004803603602081101561013e57600080fd5b810190808035906020019064010000000081111561015b57600080fd5b82018360208201111561016d57600080fd5b8035906020019184600183028401116401000000008311171561018f57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610383565b005b3480156101ef57600080fd5b506101f86103df565b6040518082815260200191505060405180910390f35b34801561021a57600080fd5b506102476004803603602081101561023157600080fd5b81019080803590602001909291905050506103eb565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561028757808201518184015260208101905061026c565b50505050905090810190601f1680156102b45780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6000818154811015156102d157fe5b90600052602060002001600091509050806000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103795780601f1061034e57610100808354040283529160200191610379565b820191906000526020600020905b81548152906001019060200180831161035c57829003601f168201915b5050505050905081565b60006020604051908101604052808381525090806001815401808255809150509060018203906000526020600020016000909192909190915060008201518160000190805190602001906103d89291906104a9565b5050505050565b60008080549050905090565b60606000828154811015156103fc57fe5b906000526020600020016000018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561049d5780601f106104725761010080835404028352916020019161049d565b820191906000526020600020905b81548152906001019060200180831161048057829003601f168201915b50505050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106104ea57805160ff1916838001178555610518565b82800160010185558215610518579182015b828111156105175782518255916020019190600101906104fc565b5b5090506105259190610529565b5090565b61054b91905b8082111561054757600081600090555060010161052f565b5090565b9056fea165627a7a723058200e353d292f68f6e366b707493ad5771a6b40a5741e23823284cffd8f131be43b0029";

    public static final String FUNC_LISTPOSTERS = "listPosters";

    public static final String FUNC_ADDPOSTER = "addPoster";

    public static final String FUNC_GETPOSTERSCOUNT = "getPostersCount";

    public static final String FUNC_GETPOSTERBYINDEX = "getPosterByIndex";

    protected PosterContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected PosterContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> listPosters(BigInteger param0) {
        final Function function = new Function(FUNC_LISTPOSTERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> addPoster(String posterHash) {
        final Function function = new Function(
                FUNC_ADDPOSTER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(posterHash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getPostersCount() {
        final Function function = new Function(FUNC_GETPOSTERSCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> getPosterByIndex(BigInteger index) {
        final Function function = new Function(FUNC_GETPOSTERBYINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }


    public static RemoteCall<PosterContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PosterContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }


    public static RemoteCall<PosterContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(PosterContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static PosterContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new PosterContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static PosterContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new PosterContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

}
