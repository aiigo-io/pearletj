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
import org.web3j.abi.datatypes.DynamicBytes;
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
public class SafeProxyFactory extends Contract {
    public static final String BINARY = "6080604052348015600e575f5ffd5b506107668061001c5f395ff3fe608060405234801561000f575f5ffd5b5060043610610055575f3560e01c80631688f0b9146100595780633408e4701461008957806353e5d93514610097578063d18af54d146100ac578063ec9e80bb146100bf575b5f5ffd5b61006c610067366004610472565b6100d2565b6040516001600160a01b0390911681526020015b60405180910390f35b604051468152602001610080565b61009f610166565b60405161008091906104f6565b61006c6100ba36600461050f565b610190565b61006c6100cd366004610472565b61025f565b5f5f8380519060200120836040516020016100f7929190918252602082015260400190565b60405160208183030381529060405280519060200120905061011a858583610290565b6040516001600160a01b038781168252919350908316907f4f51faf6c4561ff95f067657e43439f0f856d97c04d9ec9070a6199ad418e2359060200160405180910390a2509392505050565b606060405180602001610178906103af565b601f1982820381018352601f90910116604052919050565b5f5f83836040516020016101c092919091825260601b6bffffffffffffffffffffffff1916602082015260340190565b604051602081830303815290604052805190602001205f1c90506101e58686836100d2565b91506001600160a01b03831615610256576040516303ca56a360e31b81526001600160a01b03841690631e52b518906102289085908a908a908a90600401610577565b5f604051808303815f87803b15801561023f575f5ffd5b505af1158015610251573d5f5f3e3d5ffd5b505050505b50949350505050565b5f5f8380519060200120836102714690565b60408051602081019490945283019190915260608201526080016100f7565b5f833b6102e45760405162461bcd60e51b815260206004820152601f60248201527f53696e676c65746f6e20636f6e7472616374206e6f74206465706c6f7965640060448201526064015b60405180910390fd5b5f604051806020016102f5906103af565b601f1982820381018352601f90910116604081905261032291906001600160a01b038816906020016105b3565b6040516020818303038152906040529050828151826020015ff591506001600160a01b03821661038a5760405162461bcd60e51b815260206004820152601360248201527210dc99585d194c8818d85b1b0819985a5b1959606a1b60448201526064016102db565b8351156103a7575f5f5f8651602088015f875af1036103a7575f5ffd5b509392505050565b610163806105ce83390190565b6001600160a01b03811681146103d0575f5ffd5b50565b634e487b7160e01b5f52604160045260245ffd5b5f82601f8301126103f6575f5ffd5b813567ffffffffffffffff811115610410576104106103d3565b604051601f8201601f19908116603f0116810167ffffffffffffffff8111828210171561043f5761043f6103d3565b604052818152838201602001851015610456575f5ffd5b816020850160208301375f918101602001919091529392505050565b5f5f5f60608486031215610484575f5ffd5b833561048f816103bc565b9250602084013567ffffffffffffffff8111156104aa575f5ffd5b6104b6868287016103e7565b93969395505050506040919091013590565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b602081525f61050860208301846104c8565b9392505050565b5f5f5f5f60808587031215610522575f5ffd5b843561052d816103bc565b9350602085013567ffffffffffffffff811115610548575f5ffd5b610554878288016103e7565b93505060408501359150606085013561056c816103bc565b939692955090935050565b6001600160a01b038581168252841660208201526080604082018190525f906105a2908301856104c8565b905082606083015295945050505050565b5f83518060208601845e919091019182525060200191905056fe6080604052348015600e575f5ffd5b50604051610163380380610163833981016040819052602b9160b2565b6001600160a01b038116608f5760405162461bcd60e51b815260206004820152602260248201527f496e76616c69642073696e676c65746f6e20616464726573732070726f766964604482015261195960f21b606482015260840160405180910390fd5b5f80546001600160a01b0319166001600160a01b039290921691909117905560dd565b5f6020828403121560c1575f5ffd5b81516001600160a01b038116811460d6575f5ffd5b9392505050565b607a806100e95f395ff3fe60806040525f80546001600160a01b03169035632cf35bc960e11b01602657805f5260205ff35b365f5f375f5f365f845af490503d5f5f3e80603f573d5ffd5b503d5ff3fea264697066735822122009649836d841916ca65250a9ccf007cb34f01f069834b4fbe5097516116eb54b64736f6c634300081d0033a26469706673582212203fc3b90f8adabb80826f1c3256107e1e443ee9bbda510b87bfa1e6db31f3138764736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATECHAINSPECIFICPROXYWITHNONCE = "createChainSpecificProxyWithNonce";

    public static final String FUNC_CREATEPROXYWITHCALLBACK = "createProxyWithCallback";

    public static final String FUNC_CREATEPROXYWITHNONCE = "createProxyWithNonce";

    public static final String FUNC_GETCHAINID = "getChainId";

    public static final String FUNC_PROXYCREATIONCODE = "proxyCreationCode";

    public static final Event PROXYCREATION_EVENT = new Event("ProxyCreation", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected SafeProxyFactory(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SafeProxyFactory(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SafeProxyFactory(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SafeProxyFactory(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ProxyCreationEventResponse> getProxyCreationEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(PROXYCREATION_EVENT, transactionReceipt);
        ArrayList<ProxyCreationEventResponse> responses = new ArrayList<ProxyCreationEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ProxyCreationEventResponse typedResponse = new ProxyCreationEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.proxy = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.singleton = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ProxyCreationEventResponse getProxyCreationEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(PROXYCREATION_EVENT, log);
        ProxyCreationEventResponse typedResponse = new ProxyCreationEventResponse();
        typedResponse.log = log;
        typedResponse.proxy = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.singleton = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ProxyCreationEventResponse> proxyCreationEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getProxyCreationEventFromLog(log));
    }

    public Flowable<ProxyCreationEventResponse> proxyCreationEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(PROXYCREATION_EVENT));
        return proxyCreationEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> createChainSpecificProxyWithNonce(
            String _singleton, byte[] initializer, BigInteger saltNonce) {
        final Function function = new Function(
                FUNC_CREATECHAINSPECIFICPROXYWITHNONCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _singleton), 
                new org.web3j.abi.datatypes.DynamicBytes(initializer), 
                new org.web3j.abi.datatypes.generated.Uint256(saltNonce)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createProxyWithCallback(String _singleton,
            byte[] initializer, BigInteger saltNonce, String callback) {
        final Function function = new Function(
                FUNC_CREATEPROXYWITHCALLBACK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _singleton), 
                new org.web3j.abi.datatypes.DynamicBytes(initializer), 
                new org.web3j.abi.datatypes.generated.Uint256(saltNonce), 
                new org.web3j.abi.datatypes.Address(160, callback)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createProxyWithNonce(String _singleton,
            byte[] initializer, BigInteger saltNonce) {
        final Function function = new Function(
                FUNC_CREATEPROXYWITHNONCE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _singleton), 
                new org.web3j.abi.datatypes.DynamicBytes(initializer), 
                new org.web3j.abi.datatypes.generated.Uint256(saltNonce)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getChainId() {
        final Function function = new Function(FUNC_GETCHAINID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<byte[]> proxyCreationCode() {
        final Function function = new Function(FUNC_PROXYCREATIONCODE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    @Deprecated
    public static SafeProxyFactory load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeProxyFactory(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SafeProxyFactory load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SafeProxyFactory(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SafeProxyFactory load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SafeProxyFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SafeProxyFactory load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SafeProxyFactory(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SafeProxyFactory> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SafeProxyFactory.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SafeProxyFactory> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SafeProxyFactory.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<SafeProxyFactory> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SafeProxyFactory.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SafeProxyFactory> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SafeProxyFactory.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class ProxyCreationEventResponse extends BaseEventResponse {
        public String proxy;

        public String singleton;
    }
}
