package org.web3j.contracts.gnosissafe.generated;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
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
public class SafeMigration extends Contract {
    public static final String BINARY = "610100604052348015610010575f5ffd5b5060405161067c38038061067c83398101604081905261002f91610166565b30608052823b6100865760405162461bcd60e51b815260206004820152601e60248201527f536166652053696e676c65746f6e206973206e6f74206465706c6f796564000060448201526064015b60405180910390fd5b813b6100e05760405162461bcd60e51b815260206004820152602360248201527f536166652053696e676c65746f6e20284c3229206973206e6f74206465706c6f6044820152621e595960ea1b606482015260840161007d565b803b61012e5760405162461bcd60e51b815260206004820181905260248201527f66616c6c6261636b2068616e646c6572206973206e6f74206465706c6f796564604482015260640161007d565b6001600160a01b0392831660a05290821660c0521660e0526101a6565b80516001600160a01b0381168114610161575f5ffd5b919050565b5f5f5f60608486031215610178575f5ffd5b6101818461014b565b925061018f6020850161014b565b915061019d6040850161014b565b90509250925092565b60805160a05160c05160e0516104746102085f395f81816098015261028e01525f818161010a01526101c301525f8181610131015261039001525f818160e30152818161016d01528181610231015281816102f7015261034301526104745ff3fe608060405234801561000f575f5ffd5b5060043610610085575f3560e01c80639bf47d6e116100585780639bf47d6e14610105578063caa12add1461012c578063ed007fc614610153578063f6682ab01461015b575f5ffd5b806307f464a4146100895780630d7101f71461009357806368cb3d94146100d657806372f7a956146100de575b5f5ffd5b610091610163565b005b6100ba7f000000000000000000000000000000000000000000000000000000000000000081565b6040516001600160a01b03909116815260200160405180910390f35b610091610227565b6100ba7f000000000000000000000000000000000000000000000000000000000000000081565b6100ba7f000000000000000000000000000000000000000000000000000000000000000081565b6100ba7f000000000000000000000000000000000000000000000000000000000000000081565b6100916102ed565b610091610339565b6001600160a01b037f00000000000000000000000000000000000000000000000000000000000000001630036101b45760405162461bcd60e51b81526004016101ab906103ee565b60405180910390fd5b5f80546001600160a01b0319167f00000000000000000000000000000000000000000000000000000000000000006001600160a01b03169081179091556040519081527f75e41bc35ff1bf14d81d1d2f649c0084a0f974f9289c803ec9898eeec4c8d0b8906020015b60405180910390a1565b6001600160a01b037f000000000000000000000000000000000000000000000000000000000000000016300361026f5760405162461bcd60e51b81526004016101ab906103ee565b610277610163565b60405163f08a032360e01b81526001600160a01b037f0000000000000000000000000000000000000000000000000000000000000000166004820152309063f08a0323906024015f604051808303815f87803b1580156102d5575f5ffd5b505af11580156102e7573d5f5f3e3d5ffd5b50505050565b6001600160a01b037f00000000000000000000000000000000000000000000000000000000000000001630036103355760405162461bcd60e51b81526004016101ab906103ee565b6102775b6001600160a01b037f00000000000000000000000000000000000000000000000000000000000000001630036103815760405162461bcd60e51b81526004016101ab906103ee565b5f80546001600160a01b0319167f00000000000000000000000000000000000000000000000000000000000000006001600160a01b03169081179091556040519081527f75e41bc35ff1bf14d81d1d2f649c0084a0f974f9289c803ec9898eeec4c8d0b89060200161021d565b60208082526030908201527f4d6967726174696f6e2073686f756c64206f6e6c792062652063616c6c65642060408201526f1d9a584819195b1959d85d1958d85b1b60821b60608201526080019056fea2646970667358221220ac77f206414777a58f7e6240f9f251c407b111536323664760f150d46fad22f264736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_MIGRATION_SINGLETON = "MIGRATION_SINGLETON";

    public static final String FUNC_SAFE_FALLBACK_HANDLER = "SAFE_FALLBACK_HANDLER";

    public static final String FUNC_SAFE_L2_SINGLETON = "SAFE_L2_SINGLETON";

    public static final String FUNC_SAFE_SINGLETON = "SAFE_SINGLETON";

    public static final String FUNC_MIGRATEL2SINGLETON = "migrateL2Singleton";

    public static final String FUNC_MIGRATEL2WITHFALLBACKHANDLER = "migrateL2WithFallbackHandler";

    public static final String FUNC_MIGRATESINGLETON = "migrateSingleton";

    public static final String FUNC_MIGRATEWITHFALLBACKHANDLER = "migrateWithFallbackHandler";

    public static final Event CHANGEDMASTERCOPY_EVENT = new Event("ChangedMasterCopy", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected SafeMigration(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SafeMigration(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SafeMigration(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SafeMigration(String contractAddress, Web3j web3j,
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

    public RemoteFunctionCall<String> MIGRATION_SINGLETON() {
        final Function function = new Function(FUNC_MIGRATION_SINGLETON, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> SAFE_FALLBACK_HANDLER() {
        final Function function = new Function(FUNC_SAFE_FALLBACK_HANDLER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> SAFE_L2_SINGLETON() {
        final Function function = new Function(FUNC_SAFE_L2_SINGLETON, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> SAFE_SINGLETON() {
        final Function function = new Function(FUNC_SAFE_SINGLETON, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> migrateL2Singleton() {
        final Function function = new Function(
                FUNC_MIGRATEL2SINGLETON, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> migrateL2WithFallbackHandler() {
        final Function function = new Function(
                FUNC_MIGRATEL2WITHFALLBACKHANDLER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> migrateSingleton() {
        final Function function = new Function(
                FUNC_MIGRATESINGLETON, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> migrateWithFallbackHandler() {
        final Function function = new Function(
                FUNC_MIGRATEWITHFALLBACKHANDLER, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SafeMigration load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeMigration(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SafeMigration load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeMigration(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SafeMigration load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new SafeMigration(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SafeMigration load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SafeMigration(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SafeMigration> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String safeSingleton, String safeL2Singleton,
            String fallbackHandler) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, safeSingleton), 
                new org.web3j.abi.datatypes.Address(160, safeL2Singleton), 
                new org.web3j.abi.datatypes.Address(160, fallbackHandler)));
        return deployRemoteCall(SafeMigration.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<SafeMigration> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            String safeSingleton, String safeL2Singleton, String fallbackHandler) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, safeSingleton), 
                new org.web3j.abi.datatypes.Address(160, safeL2Singleton), 
                new org.web3j.abi.datatypes.Address(160, fallbackHandler)));
        return deployRemoteCall(SafeMigration.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<SafeMigration> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String safeSingleton, String safeL2Singleton,
            String fallbackHandler) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, safeSingleton), 
                new org.web3j.abi.datatypes.Address(160, safeL2Singleton), 
                new org.web3j.abi.datatypes.Address(160, fallbackHandler)));
        return deployRemoteCall(SafeMigration.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<SafeMigration> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            String safeSingleton, String safeL2Singleton, String fallbackHandler) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, safeSingleton), 
                new org.web3j.abi.datatypes.Address(160, safeL2Singleton), 
                new org.web3j.abi.datatypes.Address(160, fallbackHandler)));
        return deployRemoteCall(SafeMigration.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
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
