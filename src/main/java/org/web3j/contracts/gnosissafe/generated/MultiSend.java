package org.web3j.contracts.gnosissafe.generated;

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
public class MultiSend extends Contract {
    public static final String BINARY = "60a0604052348015600e575f5ffd5b503060805260805161024b61002a5f395f6040015261024b5ff3fe60806040526004361061001d575f3560e01c80638d80ff0a14610021575b5f5ffd5b61003461002f366004610162565b610036565b005b6001600160a01b037f00000000000000000000000000000000000000000000000000000000000000001630036100cb5760405162461bcd60e51b815260206004820152603060248201527f4d756c746953656e642073686f756c64206f6e6c792062652063616c6c65642060448201526f1d9a584819195b1959d85d1958d85b1b60821b606482015260840160405180910390fd5b805160205b81811015610149578083015160f81c6001820184015160601c60158301850151603584018601516055850187015f855f811461011357600181146101225761012c565b5f5f8585888a5af1915061012c565b5f5f8585895af491505b5080610136575f5ffd5b50508060550185019450505050506100d0565b505050565b634e487b7160e01b5f52604160045260245ffd5b5f60208284031215610172575f5ffd5b813567ffffffffffffffff811115610188575f5ffd5b8201601f81018413610198575f5ffd5b803567ffffffffffffffff8111156101b2576101b261014e565b604051601f8201601f19908116603f0116810167ffffffffffffffff811182821017156101e1576101e161014e565b6040528181528282016020018610156101f8575f5ffd5b816020840160208301375f9181016020019190915294935050505056fea2646970667358221220151bc92d8379c6f5739c86e722ac4aa488cf4af23c3d910bc5842a0fc0435db864736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_MULTISEND = "multiSend";

    @Deprecated
    protected MultiSend(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MultiSend(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected MultiSend(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected MultiSend(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
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
    public static MultiSend load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new MultiSend(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static MultiSend load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MultiSend(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static MultiSend load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new MultiSend(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static MultiSend load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new MultiSend(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<MultiSend> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MultiSend.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<MultiSend> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(MultiSend.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<MultiSend> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MultiSend.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<MultiSend> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MultiSend.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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
