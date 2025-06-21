package org.web3j.contracts.gnosissafe.generated;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
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
public class OwnerManager extends Contract {
    public static final String BINARY = "";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDOWNERWITHTHRESHOLD = "addOwnerWithThreshold";

    public static final String FUNC_CHANGETHRESHOLD = "changeThreshold";

    public static final String FUNC_GETOWNERS = "getOwners";

    public static final String FUNC_GETTHRESHOLD = "getThreshold";

    public static final String FUNC_ISOWNER = "isOwner";

    public static final String FUNC_REMOVEOWNER = "removeOwner";

    public static final String FUNC_SWAPOWNER = "swapOwner";

    public static final Event ADDEDOWNER_EVENT = new Event("AddedOwner", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event CHANGEDTHRESHOLD_EVENT = new Event("ChangedThreshold", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event REMOVEDOWNER_EVENT = new Event("RemovedOwner", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected OwnerManager(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected OwnerManager(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected OwnerManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected OwnerManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<AddedOwnerEventResponse> getAddedOwnerEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ADDEDOWNER_EVENT, transactionReceipt);
        ArrayList<AddedOwnerEventResponse> responses = new ArrayList<AddedOwnerEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddedOwnerEventResponse typedResponse = new AddedOwnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static AddedOwnerEventResponse getAddedOwnerEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ADDEDOWNER_EVENT, log);
        AddedOwnerEventResponse typedResponse = new AddedOwnerEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<AddedOwnerEventResponse> addedOwnerEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getAddedOwnerEventFromLog(log));
    }

    public Flowable<AddedOwnerEventResponse> addedOwnerEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDEDOWNER_EVENT));
        return addedOwnerEventFlowable(filter);
    }

    public static List<ChangedThresholdEventResponse> getChangedThresholdEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CHANGEDTHRESHOLD_EVENT, transactionReceipt);
        ArrayList<ChangedThresholdEventResponse> responses = new ArrayList<ChangedThresholdEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ChangedThresholdEventResponse typedResponse = new ChangedThresholdEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.threshold = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ChangedThresholdEventResponse getChangedThresholdEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CHANGEDTHRESHOLD_EVENT, log);
        ChangedThresholdEventResponse typedResponse = new ChangedThresholdEventResponse();
        typedResponse.log = log;
        typedResponse.threshold = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ChangedThresholdEventResponse> changedThresholdEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getChangedThresholdEventFromLog(log));
    }

    public Flowable<ChangedThresholdEventResponse> changedThresholdEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CHANGEDTHRESHOLD_EVENT));
        return changedThresholdEventFlowable(filter);
    }

    public static List<RemovedOwnerEventResponse> getRemovedOwnerEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(REMOVEDOWNER_EVENT, transactionReceipt);
        ArrayList<RemovedOwnerEventResponse> responses = new ArrayList<RemovedOwnerEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RemovedOwnerEventResponse typedResponse = new RemovedOwnerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static RemovedOwnerEventResponse getRemovedOwnerEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(REMOVEDOWNER_EVENT, log);
        RemovedOwnerEventResponse typedResponse = new RemovedOwnerEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<RemovedOwnerEventResponse> removedOwnerEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getRemovedOwnerEventFromLog(log));
    }

    public Flowable<RemovedOwnerEventResponse> removedOwnerEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REMOVEDOWNER_EVENT));
        return removedOwnerEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addOwnerWithThreshold(String owner,
            BigInteger _threshold) {
        final Function function = new Function(
                FUNC_ADDOWNERWITHTHRESHOLD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner), 
                new org.web3j.abi.datatypes.generated.Uint256(_threshold)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> changeThreshold(BigInteger _threshold) {
        final Function function = new Function(
                FUNC_CHANGETHRESHOLD, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_threshold)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<List> getOwners() {
        final Function function = new Function(FUNC_GETOWNERS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getThreshold() {
        final Function function = new Function(FUNC_GETTHRESHOLD, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> isOwner(String owner) {
        final Function function = new Function(FUNC_ISOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeOwner(String prevOwner, String owner,
            BigInteger _threshold) {
        final Function function = new Function(
                FUNC_REMOVEOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, prevOwner), 
                new org.web3j.abi.datatypes.Address(160, owner), 
                new org.web3j.abi.datatypes.generated.Uint256(_threshold)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> swapOwner(String prevOwner, String oldOwner,
            String newOwner) {
        final Function function = new Function(
                FUNC_SWAPOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, prevOwner), 
                new org.web3j.abi.datatypes.Address(160, oldOwner), 
                new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static OwnerManager load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new OwnerManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static OwnerManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new OwnerManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static OwnerManager load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new OwnerManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static OwnerManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new OwnerManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<OwnerManager> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(OwnerManager.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<OwnerManager> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(OwnerManager.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<OwnerManager> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(OwnerManager.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<OwnerManager> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(OwnerManager.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class AddedOwnerEventResponse extends BaseEventResponse {
        public String owner;
    }

    public static class ChangedThresholdEventResponse extends BaseEventResponse {
        public BigInteger threshold;
    }

    public static class RemovedOwnerEventResponse extends BaseEventResponse {
        public String owner;
    }
}
