package org.web3j.model;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
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
public class SafeToL2Migration extends Contract {
    public static final String BINARY = "60a0604052348015600e575f5ffd5b5030608052608051610d836100455f395f818160480152818160b8015281816103cb015281816104a001526109210152610d835ff3fe608060405234801561000f575f5ffd5b506004361061003f575f3560e01c806372f7a95614610043578063d9a2081214610086578063ef2624ae1461009b575b5f5ffd5b61006a7f000000000000000000000000000000000000000000000000000000000000000081565b6040516001600160a01b03909116815260200160405180910390f35b6100996100943660046109bb565b6100ae565b005b6100996100a93660046109ec565b610496565b6001600160a01b037f00000000000000000000000000000000000000000000000000000000000000001630036100ff5760405162461bcd60e51b81526004016100f690610a0c565b60405180910390fd5b6005546001146101215760405162461bcd60e51b81526004016100f690610a5c565b803b6101795760405162461bcd60e51b815260206004820152602160248201527f66616c6c6261636b48616e646c6572206973206e6f74206120636f6e747261636044820152601d60fa1b60648201526084016100f6565b5f5f5f9054906101000a90046001600160a01b03166001600160a01b031663ffa1ad746040518163ffffffff1660e01b81526004015f60405180830381865afa1580156101c8573d5f5f3e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526101ef9190810190610ab2565b6040516020016101ff9190610b65565b6040516020818303038152906040528051906020012090506040516020016102329064312e312e3160d81b815260050190565b6040516020818303038152906040528051906020012081146102665760405162461bcd60e51b81526004016100f690610b7b565b5f836001600160a01b031663ffa1ad746040518163ffffffff1660e01b81526004015f60405180830381865afa1580156102a2573d5f5f3e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526102c99190810190610ab2565b6040516020016102d99190610b65565b60405160208183030381529060405280519060200120905060405160200161030c90640312e332e360dc1b815260050190565b60405160208183030381529060405280519060200120811480610357575060405164312e342e3160d81b60208201526025016040516020818303038152906040528051906020012081145b6103735760405162461bcd60e51b81526004016100f690610b7b565b60405163f08a032360e01b81526001600160a01b03841660048201523090819063f08a0323906024015f604051808303815f87803b1580156103b3575f5ffd5b505af11580156103c5573d5f5f3e3d5ffd5b505050507f00000000000000000000000000000000000000000000000000000000000000006001600160a01b03167f141df868a6331af528e38c83b7aa03edc19be66e37ae67f9285bf4f8e3c6a1a861041c6107b8565b6004545f886040516104319493929190610bc6565b60405180910390a2604080516001600160a01b03878116602483015286166044808301919091528251808303909101815260649091019091526020810180516001600160e01b0316636cd1040960e11b17905261048e86826108b0565b505050505050565b6001600160a01b037f00000000000000000000000000000000000000000000000000000000000000001630036104de5760405162461bcd60e51b81526004016100f690610a0c565b6005546001146105005760405162461bcd60e51b81526004016100f690610a5c565b5f546001600160a01b03908116908216810361056a5760405162461bcd60e51b815260206004820152602360248201527f5361666520697320616c7265616479207573696e67207468652073696e676c656044820152623a37b760e91b60648201526084016100f6565b5f816001600160a01b031663ffa1ad746040518163ffffffff1660e01b81526004015f60405180830381865afa1580156105a6573d5f5f3e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526105cd9190810190610ab2565b6040516020016105dd9190610b65565b6040516020818303038152906040528051906020012090505f836001600160a01b031663ffa1ad746040518163ffffffff1660e01b81526004015f60405180830381865afa158015610631573d5f5f3e3d5ffd5b505050506040513d5f823e601f3d908101601f191682016040526106589190810190610ab2565b6040516020016106689190610b65565b6040516020818303038152906040528051906020012090508082146106e95760405162461bcd60e51b815260206004820152603160248201527f4c322073696e676c65746f6e206d757374206d617463682063757272656e74206044820152703b32b939b4b7b71039b4b733b632ba37b760791b60648201526084016100f6565b604051640312e332e360dc1b602082015260250160405160208183030381529060405280519060200120811480610748575060405164312e342e3160d81b60208201526025016040516020818303038152906040528051906020012081145b6107645760405162461bcd60e51b81526004016100f690610b7b565b604080516001600160a01b0386166024808301919091528251808303909101815260449091019091526020810180516001600160e01b0316637793125760e11b1790526107b185826108b0565b5050505050565b60605f60035467ffffffffffffffff8111156107d6576107d6610a9e565b6040519080825280602002602001820160405280156107ff578160200160208202803683370190505b5060015f81815260026020527fe90b7bceb6e7df5418fb78d8ee546e97c83a08bbccc01a0644d599ccd2a7c2e05492935090916001600160a01b03165b826001600160a01b0316816001600160a01b0316146108a7578084838151811061086857610868610c31565b6001600160a01b039283166020918202929092018101919091529181165f9081526002909252604090912054168161089f81610c45565b92505061083c565b50919392505050565b5f80546001600160a01b0319166001600160a01b03841617815560045460408051602081018490523381830152606080820193909352815180820390930183526080019081905290917f66753cd2356569ee081232e3be8909b950e0a76c1f8460c3a5e3c2be32b11bed91610957917f0000000000000000000000000000000000000000000000000000000000000000918690600190839081908190819081908c90610c97565b60405180910390a16040516001600160a01b03841681527f75e41bc35ff1bf14d81d1d2f649c0084a0f974f9289c803ec9898eeec4c8d0b89060200160405180910390a1505050565b80356001600160a01b03811681146109b6575f5ffd5b919050565b5f5f604083850312156109cc575f5ffd5b6109d5836109a0565b91506109e3602084016109a0565b90509250929050565b5f602082840312156109fc575f5ffd5b610a05826109a0565b9392505050565b60208082526030908201527f4d6967726174696f6e2073686f756c64206f6e6c792062652063616c6c65642060408201526f1d9a584819195b1959d85d1958d85b1b60821b606082015260800190565b60208082526022908201527f53616665206d7573742068617665206e6f7420657865637574656420616e79206040820152610e8f60f31b606082015260800190565b634e487b7160e01b5f52604160045260245ffd5b5f60208284031215610ac2575f5ffd5b815167ffffffffffffffff811115610ad8575f5ffd5b8201601f81018413610ae8575f5ffd5b805167ffffffffffffffff811115610b0257610b02610a9e565b604051601f8201601f19908116603f0116810167ffffffffffffffff81118282101715610b3157610b31610a9e565b604052818152828201602001861015610b48575f5ffd5b8160208401602083015e5f91810160200191909152949350505050565b5f82518060208501845e5f920191825250919050565b6020808252602b908201527f50726f76696465642073696e676c65746f6e2076657273696f6e206973206e6f60408201526a1d081cdd5c1c1bdc9d195960aa1b606082015260800190565b608080825285519082018190525f90602087019060a0840190835b81811015610c085783516001600160a01b0316835260209384019390920191600101610be1565b5050602084019690965250506001600160a01b0392831660408201529116606090910152919050565b634e487b7160e01b5f52603260045260245ffd5b5f60018201610c6257634e487b7160e01b5f52601160045260245ffd5b5060010190565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b60018060a01b038b16815289602082015261016060408201525f610cbf61016083018b610c69565b60028a10610cdb57634e487b7160e01b5f52602160045260245ffd5b8960608401528860808401528760a08401528660c0840152610d0860e08401876001600160a01b03169052565b6001600160a01b03851661010084015282810361012084018190525f8252602090810161014085015201610d3c8185610c69565b9d9c5050505050505050505050505056fea26469706673582212205a530dc0bbed6d3f795aefce092c0f4b41194ca796d52122ab69894fb195538c64736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_MIGRATION_SINGLETON = "MIGRATION_SINGLETON";

    public static final String FUNC_MIGRATEFROMV111 = "migrateFromV111";

    public static final String FUNC_MIGRATETOL2 = "migrateToL2";

    public static final Event CHANGEDMASTERCOPY_EVENT = new Event("ChangedMasterCopy", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
    ;

    public static final Event SAFEMULTISIGTRANSACTION_EVENT = new Event("SafeMultiSigTransaction", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
    ;

    public static final Event SAFESETUP_EVENT = new Event("SafeSetup", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<DynamicArray<Address>>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected SafeToL2Migration(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SafeToL2Migration(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SafeToL2Migration(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SafeToL2Migration(String contractAddress, Web3j web3j,
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

    public static List<SafeMultiSigTransactionEventResponse> getSafeMultiSigTransactionEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SAFEMULTISIGTRANSACTION_EVENT, transactionReceipt);
        ArrayList<SafeMultiSigTransactionEventResponse> responses = new ArrayList<SafeMultiSigTransactionEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SafeMultiSigTransactionEventResponse typedResponse = new SafeMultiSigTransactionEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.operation = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.safeTxGas = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.baseGas = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.gasPrice = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse.gasToken = (String) eventValues.getNonIndexedValues().get(7).getValue();
            typedResponse.refundReceiver = (String) eventValues.getNonIndexedValues().get(8).getValue();
            typedResponse.signatures = (byte[]) eventValues.getNonIndexedValues().get(9).getValue();
            typedResponse.additionalInfo = (byte[]) eventValues.getNonIndexedValues().get(10).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static SafeMultiSigTransactionEventResponse getSafeMultiSigTransactionEventFromLog(
            Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SAFEMULTISIGTRANSACTION_EVENT, log);
        SafeMultiSigTransactionEventResponse typedResponse = new SafeMultiSigTransactionEventResponse();
        typedResponse.log = log;
        typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.data = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.operation = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.safeTxGas = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.baseGas = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
        typedResponse.gasPrice = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
        typedResponse.gasToken = (String) eventValues.getNonIndexedValues().get(7).getValue();
        typedResponse.refundReceiver = (String) eventValues.getNonIndexedValues().get(8).getValue();
        typedResponse.signatures = (byte[]) eventValues.getNonIndexedValues().get(9).getValue();
        typedResponse.additionalInfo = (byte[]) eventValues.getNonIndexedValues().get(10).getValue();
        return typedResponse;
    }

    public Flowable<SafeMultiSigTransactionEventResponse> safeMultiSigTransactionEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getSafeMultiSigTransactionEventFromLog(log));
    }

    public Flowable<SafeMultiSigTransactionEventResponse> safeMultiSigTransactionEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SAFEMULTISIGTRANSACTION_EVENT));
        return safeMultiSigTransactionEventFlowable(filter);
    }

    public static List<SafeSetupEventResponse> getSafeSetupEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SAFESETUP_EVENT, transactionReceipt);
        ArrayList<SafeSetupEventResponse> responses = new ArrayList<SafeSetupEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SafeSetupEventResponse typedResponse = new SafeSetupEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.initiator = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.owners = (List<String>) ((Array) eventValues.getNonIndexedValues().get(0)).getNativeValueCopy();
            typedResponse.threshold = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.initializer = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.fallbackHandler = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static SafeSetupEventResponse getSafeSetupEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SAFESETUP_EVENT, log);
        SafeSetupEventResponse typedResponse = new SafeSetupEventResponse();
        typedResponse.log = log;
        typedResponse.initiator = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.owners = (List<String>) ((Array) eventValues.getNonIndexedValues().get(0)).getNativeValueCopy();
        typedResponse.threshold = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.initializer = (String) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.fallbackHandler = (String) eventValues.getNonIndexedValues().get(3).getValue();
        return typedResponse;
    }

    public Flowable<SafeSetupEventResponse> safeSetupEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getSafeSetupEventFromLog(log));
    }

    public Flowable<SafeSetupEventResponse> safeSetupEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SAFESETUP_EVENT));
        return safeSetupEventFlowable(filter);
    }

    public RemoteFunctionCall<String> MIGRATION_SINGLETON() {
        final Function function = new Function(FUNC_MIGRATION_SINGLETON, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> migrateFromV111(String l2Singleton,
            String fallbackHandler) {
        final Function function = new Function(
                FUNC_MIGRATEFROMV111, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, l2Singleton), 
                new org.web3j.abi.datatypes.Address(160, fallbackHandler)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> migrateToL2(String l2Singleton) {
        final Function function = new Function(
                FUNC_MIGRATETOL2, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, l2Singleton)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SafeToL2Migration load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeToL2Migration(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SafeToL2Migration load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeToL2Migration(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SafeToL2Migration load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SafeToL2Migration(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SafeToL2Migration load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SafeToL2Migration(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SafeToL2Migration> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SafeToL2Migration.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<SafeToL2Migration> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SafeToL2Migration.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SafeToL2Migration> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SafeToL2Migration.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SafeToL2Migration> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SafeToL2Migration.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class SafeMultiSigTransactionEventResponse extends BaseEventResponse {
        public String to;

        public BigInteger value;

        public byte[] data;

        public BigInteger operation;

        public BigInteger safeTxGas;

        public BigInteger baseGas;

        public BigInteger gasPrice;

        public String gasToken;

        public String refundReceiver;

        public byte[] signatures;

        public byte[] additionalInfo;
    }

    public static class SafeSetupEventResponse extends BaseEventResponse {
        public String initiator;

        public List<String> owners;

        public BigInteger threshold;

        public String initializer;

        public String fallbackHandler;
    }
}
