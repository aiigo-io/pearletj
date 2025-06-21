package org.web3j.contracts.gnosissafe.generated;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
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
public class SafeToL2Setup extends Contract {
    public static final String BINARY = "60a0604052348015600e575f5ffd5b503060805260805161024861002a5f395f604c01526102485ff3fe608060405234801561000f575f5ffd5b5060043610610029575f3560e01c8063fe51f6431461002d575b5f5ffd5b61004061003b3660046101e5565b610042565b005b6001600160a01b037f00000000000000000000000000000000000000000000000000000000000000001630036100dc5760405162461bcd60e51b815260206004820152603460248201527f53616665546f4c3253657475702073686f756c64206f6e6c792062652063616c6044820152731b1959081d9a584819195b1959d85d1958d85b1b60621b60648201526084015b60405180910390fd5b600554156101375760405162461bcd60e51b815260206004820152602260248201527f53616665206d7573742068617665206e6f7420657865637574656420616e79206044820152610e8f60f31b60648201526084016100d3565b80803b5f036101885760405162461bcd60e51b815260206004820152601c60248201527f4163636f756e7420646f65736e277420636f6e7461696e20636f64650000000060448201526064016100d3565b466001146101e1575f80546001600160a01b0319166001600160a01b0384169081179091556040519081527f75e41bc35ff1bf14d81d1d2f649c0084a0f974f9289c803ec9898eeec4c8d0b89060200160405180910390a15b5050565b5f602082840312156101f5575f5ffd5b81356001600160a01b038116811461020b575f5ffd5b939250505056fea2646970667358221220eb25bb6037eef8e1682b3a8de19f5fdff4aa552a67a67ea1da74986a2067ff8e64736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_SETUPTOL2 = "setupToL2";

    public static final Event CHANGEDMASTERCOPY_EVENT = new Event("ChangedMasterCopy", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected SafeToL2Setup(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SafeToL2Setup(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SafeToL2Setup(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SafeToL2Setup(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ChangedMasterCopyEventResponse> getChangedMasterCopyEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CHANGEDMASTERCOPY_EVENT, transactionReceipt);
        ArrayList<ChangedMasterCopyEventResponse> responses = new ArrayList<ChangedMasterCopyEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ChangedMasterCopyEventResponse typedResponse = new ChangedMasterCopyEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.singleton = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ChangedMasterCopyEventResponse getChangedMasterCopyEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CHANGEDMASTERCOPY_EVENT, log);
        ChangedMasterCopyEventResponse typedResponse = new ChangedMasterCopyEventResponse();
        typedResponse.log = log;
        typedResponse.singleton = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ChangedMasterCopyEventResponse> changedMasterCopyEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getChangedMasterCopyEventFromLog(log));
    }

    public Flowable<ChangedMasterCopyEventResponse> changedMasterCopyEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CHANGEDMASTERCOPY_EVENT));
        return changedMasterCopyEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> setupToL2(String l2Singleton) {
        final Function function = new Function(
                FUNC_SETUPTOL2, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, l2Singleton)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SafeToL2Setup load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeToL2Setup(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SafeToL2Setup load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeToL2Setup(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SafeToL2Setup load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new SafeToL2Setup(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SafeToL2Setup load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SafeToL2Setup(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SafeToL2Setup> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SafeToL2Setup.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<SafeToL2Setup> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SafeToL2Setup.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SafeToL2Setup> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SafeToL2Setup.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SafeToL2Setup> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SafeToL2Setup.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class ChangedMasterCopyEventResponse extends BaseEventResponse {
        public String singleton;
    }
}
