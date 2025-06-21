package org.web3j.model;

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
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
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
public class ModuleManager extends Contract {
    public static final String BINARY = "";

    private static String librariesLinkedBinary;

    public static final String FUNC_DISABLEMODULE = "disableModule";

    public static final String FUNC_ENABLEMODULE = "enableModule";

    public static final String FUNC_EXECTRANSACTIONFROMMODULE = "execTransactionFromModule";

    public static final String FUNC_EXECTRANSACTIONFROMMODULERETURNDATA = "execTransactionFromModuleReturnData";

    public static final String FUNC_GETMODULESPAGINATED = "getModulesPaginated";

    public static final String FUNC_ISMODULEENABLED = "isModuleEnabled";

    public static final Event DISABLEDMODULE_EVENT = new Event("DisabledModule", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event ENABLEDMODULE_EVENT = new Event("EnabledModule", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event EXECUTIONFROMMODULEFAILURE_EVENT = new Event("ExecutionFromModuleFailure", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event EXECUTIONFROMMODULESUCCESS_EVENT = new Event("ExecutionFromModuleSuccess", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected ModuleManager(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ModuleManager(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ModuleManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ModuleManager(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<DisabledModuleEventResponse> getDisabledModuleEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(DISABLEDMODULE_EVENT, transactionReceipt);
        ArrayList<DisabledModuleEventResponse> responses = new ArrayList<DisabledModuleEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DisabledModuleEventResponse typedResponse = new DisabledModuleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.module = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static DisabledModuleEventResponse getDisabledModuleEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(DISABLEDMODULE_EVENT, log);
        DisabledModuleEventResponse typedResponse = new DisabledModuleEventResponse();
        typedResponse.log = log;
        typedResponse.module = (String) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<DisabledModuleEventResponse> disabledModuleEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getDisabledModuleEventFromLog(log));
    }

    public Flowable<DisabledModuleEventResponse> disabledModuleEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DISABLEDMODULE_EVENT));
        return disabledModuleEventFlowable(filter);
    }

    public static List<EnabledModuleEventResponse> getEnabledModuleEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ENABLEDMODULE_EVENT, transactionReceipt);
        ArrayList<EnabledModuleEventResponse> responses = new ArrayList<EnabledModuleEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EnabledModuleEventResponse typedResponse = new EnabledModuleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.module = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static EnabledModuleEventResponse getEnabledModuleEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ENABLEDMODULE_EVENT, log);
        EnabledModuleEventResponse typedResponse = new EnabledModuleEventResponse();
        typedResponse.log = log;
        typedResponse.module = (String) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<EnabledModuleEventResponse> enabledModuleEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getEnabledModuleEventFromLog(log));
    }

    public Flowable<EnabledModuleEventResponse> enabledModuleEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ENABLEDMODULE_EVENT));
        return enabledModuleEventFlowable(filter);
    }

    public static List<ExecutionFromModuleFailureEventResponse> getExecutionFromModuleFailureEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(EXECUTIONFROMMODULEFAILURE_EVENT, transactionReceipt);
        ArrayList<ExecutionFromModuleFailureEventResponse> responses = new ArrayList<ExecutionFromModuleFailureEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ExecutionFromModuleFailureEventResponse typedResponse = new ExecutionFromModuleFailureEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.module = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ExecutionFromModuleFailureEventResponse getExecutionFromModuleFailureEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(EXECUTIONFROMMODULEFAILURE_EVENT, log);
        ExecutionFromModuleFailureEventResponse typedResponse = new ExecutionFromModuleFailureEventResponse();
        typedResponse.log = log;
        typedResponse.module = (String) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ExecutionFromModuleFailureEventResponse> executionFromModuleFailureEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getExecutionFromModuleFailureEventFromLog(log));
    }

    public Flowable<ExecutionFromModuleFailureEventResponse> executionFromModuleFailureEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(EXECUTIONFROMMODULEFAILURE_EVENT));
        return executionFromModuleFailureEventFlowable(filter);
    }

    public static List<ExecutionFromModuleSuccessEventResponse> getExecutionFromModuleSuccessEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(EXECUTIONFROMMODULESUCCESS_EVENT, transactionReceipt);
        ArrayList<ExecutionFromModuleSuccessEventResponse> responses = new ArrayList<ExecutionFromModuleSuccessEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ExecutionFromModuleSuccessEventResponse typedResponse = new ExecutionFromModuleSuccessEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.module = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ExecutionFromModuleSuccessEventResponse getExecutionFromModuleSuccessEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(EXECUTIONFROMMODULESUCCESS_EVENT, log);
        ExecutionFromModuleSuccessEventResponse typedResponse = new ExecutionFromModuleSuccessEventResponse();
        typedResponse.log = log;
        typedResponse.module = (String) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ExecutionFromModuleSuccessEventResponse> executionFromModuleSuccessEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getExecutionFromModuleSuccessEventFromLog(log));
    }

    public Flowable<ExecutionFromModuleSuccessEventResponse> executionFromModuleSuccessEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(EXECUTIONFROMMODULESUCCESS_EVENT));
        return executionFromModuleSuccessEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> disableModule(String prevModule, String module) {
        final Function function = new Function(
                FUNC_DISABLEMODULE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, prevModule), 
                new org.web3j.abi.datatypes.Address(160, module)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> enableModule(String module) {
        final Function function = new Function(
                FUNC_ENABLEMODULE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, module)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> execTransactionFromModule(String to,
            BigInteger value, byte[] data, BigInteger operation) {
        final Function function = new Function(
                FUNC_EXECTRANSACTIONFROMMODULE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(value), 
                new org.web3j.abi.datatypes.DynamicBytes(data), 
                new org.web3j.abi.datatypes.generated.Uint8(operation)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> execTransactionFromModuleReturnData(String to,
            BigInteger value, byte[] data, BigInteger operation) {
        final Function function = new Function(
                FUNC_EXECTRANSACTIONFROMMODULERETURNDATA, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(value), 
                new org.web3j.abi.datatypes.DynamicBytes(data), 
                new org.web3j.abi.datatypes.generated.Uint8(operation)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<List<String>, String>> getModulesPaginated(String start,
            BigInteger pageSize) {
        final Function function = new Function(FUNC_GETMODULESPAGINATED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, start), 
                new org.web3j.abi.datatypes.generated.Uint256(pageSize)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}, new TypeReference<Address>() {}));
        return new RemoteFunctionCall<Tuple2<List<String>, String>>(function,
                new Callable<Tuple2<List<String>, String>>() {
                    @Override
                    public Tuple2<List<String>, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<List<String>, String>(
                                convertToNative((List<Address>) results.get(0).getValue()), 
                                (String) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Boolean> isModuleEnabled(String module) {
        final Function function = new Function(FUNC_ISMODULEENABLED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, module)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static ModuleManager load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new ModuleManager(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ModuleManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ModuleManager(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ModuleManager load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new ModuleManager(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ModuleManager load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ModuleManager(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ModuleManager> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ModuleManager.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<ModuleManager> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ModuleManager.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<ModuleManager> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ModuleManager.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<ModuleManager> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ModuleManager.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class DisabledModuleEventResponse extends BaseEventResponse {
        public String module;
    }

    public static class EnabledModuleEventResponse extends BaseEventResponse {
        public String module;
    }

    public static class ExecutionFromModuleFailureEventResponse extends BaseEventResponse {
        public String module;
    }

    public static class ExecutionFromModuleSuccessEventResponse extends BaseEventResponse {
        public String module;
    }
}
