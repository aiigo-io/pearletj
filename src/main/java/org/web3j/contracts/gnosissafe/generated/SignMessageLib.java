package org.web3j.contracts.gnosissafe.generated;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
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
public class SignMessageLib extends Contract {
    public static final String BINARY = "6080604052348015600e575f5ffd5b506103828061001c5f395ff3fe608060405234801561000f575f5ffd5b5060043610610034575f3560e01c80630a1028c41461003857806385a5affe1461005d575b5f5ffd5b61004b610046366004610214565b610072565b60405190815260200160405180910390f35b61007061006b3660046102c7565b610180565b005b5f5f7f60b3cbf8b4a223d68d641b3b6ddf9a298e7f33710cf3d3a9d1146b5a6150fbca5f1b83805190602001206040516020016100b9929190918252602082015260400190565b60408051601f19818403018152828252805160209182012063f698da2560e01b84529151919350601960f81b92600160f81b92309263f698da2592600480820193918290030181865afa158015610112573d5f5f3e3d5ffd5b505050506040513d601f19601f820116820180604052508101906101369190610335565b6040516001600160f81b0319938416602082015292909116602183015260228201526042810182905260620160405160208183030381529060405280519060200120915050919050565b5f6101bf83838080601f0160208091040260200160405190810160405280939291908181526020018383808284375f9201919091525061007292505050565b5f81815260076020526040808220600190555191925082917fe7f4675038f4f6034dfcbbb24c4dc08e4ebf10eb9d257d3d02c0f38d122ac6e49190a2505050565b634e487b7160e01b5f52604160045260245ffd5b5f60208284031215610224575f5ffd5b813567ffffffffffffffff81111561023a575f5ffd5b8201601f8101841361024a575f5ffd5b803567ffffffffffffffff81111561026457610264610200565b604051601f8201601f19908116603f0116810167ffffffffffffffff8111828210171561029357610293610200565b6040528181528282016020018610156102aa575f5ffd5b816020840160208301375f91810160200191909152949350505050565b5f5f602083850312156102d8575f5ffd5b823567ffffffffffffffff8111156102ee575f5ffd5b8301601f810185136102fe575f5ffd5b803567ffffffffffffffff811115610314575f5ffd5b856020828401011115610325575f5ffd5b6020919091019590945092505050565b5f60208284031215610345575f5ffd5b505191905056fea26469706673582212204dfd71b0e6fa199ffa12abda0f020c38c14eb4cbbe2ceb102ac8311fb653d2c064736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_GETMESSAGEHASH = "getMessageHash";

    public static final String FUNC_SIGNMESSAGE = "signMessage";

    public static final Event SIGNMSG_EVENT = new Event("SignMsg", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}));
    ;

    @Deprecated
    protected SignMessageLib(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SignMessageLib(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SignMessageLib(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SignMessageLib(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<SignMsgEventResponse> getSignMsgEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SIGNMSG_EVENT, transactionReceipt);
        ArrayList<SignMsgEventResponse> responses = new ArrayList<SignMsgEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SignMsgEventResponse typedResponse = new SignMsgEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.msgHash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static SignMsgEventResponse getSignMsgEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SIGNMSG_EVENT, log);
        SignMsgEventResponse typedResponse = new SignMsgEventResponse();
        typedResponse.log = log;
        typedResponse.msgHash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<SignMsgEventResponse> signMsgEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getSignMsgEventFromLog(log));
    }

    public Flowable<SignMsgEventResponse> signMsgEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SIGNMSG_EVENT));
        return signMsgEventFlowable(filter);
    }

    public RemoteFunctionCall<byte[]> getMessageHash(byte[] message) {
        final Function function = new Function(FUNC_GETMESSAGEHASH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(message)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> signMessage(byte[] _data) {
        final Function function = new Function(
                FUNC_SIGNMESSAGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(_data)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SignMessageLib load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new SignMessageLib(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SignMessageLib load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SignMessageLib(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SignMessageLib load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new SignMessageLib(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SignMessageLib load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SignMessageLib(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SignMessageLib> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SignMessageLib.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SignMessageLib> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SignMessageLib.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<SignMessageLib> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SignMessageLib.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SignMessageLib> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SignMessageLib.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class SignMsgEventResponse extends BaseEventResponse {
        public byte[] msgHash;
    }
}
