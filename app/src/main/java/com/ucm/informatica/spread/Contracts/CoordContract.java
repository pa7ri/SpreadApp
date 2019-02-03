package com.ucm.informatica.spread.Contracts;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int128;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple6;
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
public class CoordContract extends Contract implements Serializable {
    private static final String BINARY = "608060405234801561001057600080fd5b50610b5b806100206000396000f3fe608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806330366d5f14610067578063348e03ea146100925780639b6b6d3e14610225578063eaaa7cf014610379575b600080fd5b34801561007357600080fd5b5061007c6104cd565b6040518082815260200191505060405180910390f35b34801561009e57600080fd5b50610223600480360360c08110156100b557600080fd5b81019080803590602001906401000000008111156100d257600080fd5b8201836020820111156100e457600080fd5b8035906020019184600183028401116401000000008311171561010657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561016957600080fd5b82018360208201111561017b57600080fd5b8035906020019184600183028401116401000000008311171561019d57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035600f0b90602001909291908035600f0b90602001909291908035600f0b90602001909291908035600f0b90602001909291905050506104d9565b005b34801561023157600080fd5b5061025e6004803603602081101561024857600080fd5b810190808035906020019092919050505061068d565b60405180806020018060200187600f0b600f0b815260200186600f0b600f0b815260200185600f0b600f0b815260200184600f0b600f0b8152602001838103835289818151815260200191508051906020019080838360005b838110156102d25780820151818401526020810190506102b7565b50505050905090810190601f1680156102ff5780820380516001836020036101000a031916815260200191505b50838103825288818151815260200191508051906020019080838360005b8381101561033857808201518184015260208101905061031d565b50505050905090810190601f1680156103655780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390f35b34801561038557600080fd5b506103b26004803603602081101561039c57600080fd5b81019080803590602001909291905050506108db565b60405180806020018060200187600f0b600f0b815260200186600f0b600f0b815260200185600f0b600f0b815260200184600f0b600f0b8152602001838103835289818151815260200191508051906020019080838360005b8381101561042657808201518184015260208101905061040b565b50505050905090810190601f1680156104535780820380516001836020036101000a031916815260200191505b50838103825288818151815260200191508051906020019080838360005b8381101561048c578082015181840152602081019050610471565b50505050905090810190601f1680156104b95780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390f35b60008080549050905090565b600060c06040519081016040528088815260200187815260200186600f0b815260200185600f0b815260200184600f0b815260200183600f0b81525090806001815401808255809150509060018203906000526020600020906004020160009091929091909150600082015181600001908051906020019061055c929190610a8a565b506020820151816001019080519060200190610579929190610a8a565b5060408201518160020160006101000a8154816fffffffffffffffffffffffffffffffff0219169083600f0b6fffffffffffffffffffffffffffffffff16021790555060608201518160020160106101000a8154816fffffffffffffffffffffffffffffffff0219169083600f0b6fffffffffffffffffffffffffffffffff16021790555060808201518160030160006101000a8154816fffffffffffffffffffffffffffffffff0219169083600f0b6fffffffffffffffffffffffffffffffff16021790555060a08201518160030160106101000a8154816fffffffffffffffffffffffffffffffff0219169083600f0b6fffffffffffffffffffffffffffffffff160217905550505050505050505050565b6060806000806000806000878154811015156106a557fe5b90600052602060002090600402016000016000888154811015156106c557fe5b90600052602060002090600402016001016000898154811015156106e557fe5b906000526020600020906004020160020160009054906101000a9004600f0b60008a81548110151561071357fe5b906000526020600020906004020160020160109054906101000a9004600f0b60008b81548110151561074157fe5b906000526020600020906004020160030160009054906101000a9004600f0b60008c81548110151561076f57fe5b906000526020600020906004020160030160109054906101000a9004600f0b858054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108235780601f106107f857610100808354040283529160200191610823565b820191906000526020600020905b81548152906001019060200180831161080657829003601f168201915b50505050509550848054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108bf5780601f10610894576101008083540402835291602001916108bf565b820191906000526020600020905b8154815290600101906020018083116108a257829003601f168201915b5050505050945095509550955095509550955091939550919395565b6000818154811015156108ea57fe5b9060005260206000209060040201600091509050806000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109965780601f1061096b57610100808354040283529160200191610996565b820191906000526020600020905b81548152906001019060200180831161097957829003601f168201915b505050505090806001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a345780601f10610a0957610100808354040283529160200191610a34565b820191906000526020600020905b815481529060010190602001808311610a1757829003601f168201915b5050505050908060020160009054906101000a9004600f0b908060020160109054906101000a9004600f0b908060030160009054906101000a9004600f0b908060030160109054906101000a9004600f0b905086565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610acb57805160ff1916838001178555610af9565b82800160010185558215610af9579182015b82811115610af8578251825591602001919060010190610add565b5b509050610b069190610b0a565b5090565b610b2c91905b80821115610b28576000816000905550600101610b10565b5090565b9056fea165627a7a7230582063a83d6d85230978a7cab62fc0961a85589102ee573cfda7a70416245041b1cd0029";

    public static final String FUNC_GETEVENTSCOUNT = "getEventsCount";

    public static final String FUNC_ADDEVENT = "addEvent";

    public static final String FUNC_GETEVENTBYINDEX = "getEventByIndex";

    public static final String FUNC_LISTEVENTS = "listEvents";

    protected CoordContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CoordContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }


    public RemoteCall<BigInteger> getEventsCount() {
        final Function function = new Function(FUNC_GETEVENTSCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> addEvent(String t, String d, BigInteger latEnt, BigInteger latDec, BigInteger longEnt, BigInteger longDec) {
        final Function function = new Function(
                FUNC_ADDEVENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(t), 
                new org.web3j.abi.datatypes.Utf8String(d), 
                new org.web3j.abi.datatypes.generated.Int128(latEnt), 
                new org.web3j.abi.datatypes.generated.Int128(latDec), 
                new org.web3j.abi.datatypes.generated.Int128(longEnt), 
                new org.web3j.abi.datatypes.generated.Int128(longDec)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple6<String, String, BigInteger, BigInteger, BigInteger, BigInteger>> getEventByIndex(BigInteger index) {
        final Function function = new Function(FUNC_GETEVENTBYINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Int128>() {}, new TypeReference<Int128>() {}, new TypeReference<Int128>() {}, new TypeReference<Int128>() {}));
        return new RemoteCall<Tuple6<String, String, BigInteger, BigInteger, BigInteger, BigInteger>>(
                new Callable<Tuple6<String, String, BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple6<String, String, BigInteger, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, BigInteger, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public RemoteCall<Tuple6<String, String, BigInteger, BigInteger, BigInteger, BigInteger>> listEvents(BigInteger param0) {
        final Function function = new Function(FUNC_LISTEVENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Int128>() {}, new TypeReference<Int128>() {}, new TypeReference<Int128>() {}, new TypeReference<Int128>() {}));
        return new RemoteCall<Tuple6<String, String, BigInteger, BigInteger, BigInteger, BigInteger>>(
                new Callable<Tuple6<String, String, BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple6<String, String, BigInteger, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, BigInteger, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    public static RemoteCall<CoordContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CoordContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }


    public static RemoteCall<CoordContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CoordContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static CoordContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new CoordContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static CoordContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CoordContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

}
