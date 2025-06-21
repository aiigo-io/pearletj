package org.web3j.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/LFDT-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.14.0.
 */
@SuppressWarnings("rawtypes")
public class MultiSendCallOnly extends Contract {
    public static final String BINARY = "6080604052348015600e575f5ffd5b506101a88061001c5f395ff3fe60806040526004361061001d575f3560e01c80638d80ff0a14610021575b5f5ffd5b61003461002f3660046100bf565b610036565b005b805160205b818110156100a6578083015160f81c6001820184015160601c60158301850151603584018601516055850187015f855f811461007e576001811461001d57610089565b5f5f8585888a5af191505b5080610093575f5ffd5b505080605501850194505050505061003b565b505050565b634e487b7160e01b5f52604160045260245ffd5b5f602082840312156100cf575f5ffd5b813567ffffffffffffffff8111156100e5575f5ffd5b8201601f810184136100f5575f5ffd5b803567ffffffffffffffff81111561010f5761010f6100ab565b604051601f8201601f19908116603f0116810167ffffffffffffffff8111828210171561013e5761013e6100ab565b604052818152828201602001861015610155575f5ffd5b816020840160208301375f9181016020019190915294935050505056fea2646970667358221220479c9d0f565e1999894a37de0f0f879becec2583793f15d6b00fe4f63ab4303464736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_MULTISEND = "multiSend";

    @Deprecated
    protected MultiSendCallOnly(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MultiSendCallOnly(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MultiSendCallOnly(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MultiSendCallOnly(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> multiSend(byte[] transactions,
            BigInteger weiValue) {
        final Function function = new Function(
                FUNC_MULTISEND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(transactions)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    @Deprecated
    public static MultiSendCallOnly load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MultiSendCallOnly(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MultiSendCallOnly load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MultiSendCallOnly(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MultiSendCallOnly load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new MultiSendCallOnly(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MultiSendCallOnly load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MultiSendCallOnly(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MultiSendCallOnly> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MultiSendCallOnly.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<MultiSendCallOnly> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MultiSendCallOnly.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<MultiSendCallOnly> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MultiSendCallOnly.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<MultiSendCallOnly> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MultiSendCallOnly.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }
}
