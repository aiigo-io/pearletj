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
public class CreateCall extends Contract {
    public static final String BINARY = "6080604052348015600e575f5ffd5b506103128061001c5f395ff3fe608060405234801561000f575f5ffd5b5060043610610034575f3560e01c80634847be6f146100385780634c8c9ea114610067575b5f5ffd5b61004b61004636600461024c565b61007a565b6040516001600160a01b03909116815260200160405180910390f35b61004b610075366004610298565b610117565b5f8183518460200186f590506001600160a01b0381166100dd5760405162461bcd60e51b815260206004820152601960248201527810dbdd5b19081b9bdd0819195c1b1bde4818dbdb9d1c9858dd603a1b60448201526064015b60405180910390fd5b6040516001600160a01b038216907f4db17dd5e4732fb6da34a148104a592783ca119a1e7bb8829eba6cbadef0b511905f90a29392505050565b5f81516020830184f090506001600160a01b0381166101745760405162461bcd60e51b815260206004820152601960248201527810dbdd5b19081b9bdd0819195c1b1bde4818dbdb9d1c9858dd603a1b60448201526064016100d4565b6040516001600160a01b038216907f4db17dd5e4732fb6da34a148104a592783ca119a1e7bb8829eba6cbadef0b511905f90a292915050565b634e487b7160e01b5f52604160045260245ffd5b5f82601f8301126101d0575f5ffd5b813567ffffffffffffffff8111156101ea576101ea6101ad565b604051601f8201601f19908116603f0116810167ffffffffffffffff81118282101715610219576102196101ad565b604052818152838201602001851015610230575f5ffd5b816020850160208301375f918101602001919091529392505050565b5f5f5f6060848603121561025e575f5ffd5b83359250602084013567ffffffffffffffff81111561027b575f5ffd5b610287868287016101c1565b925050604084013590509250925092565b5f5f604083850312156102a9575f5ffd5b82359150602083013567ffffffffffffffff8111156102c6575f5ffd5b6102d2858286016101c1565b915050925092905056fea26469706673582212208064d500a25b9b46a659552532fbd7706fa702e1c9d7c3429bfebeca1b3382e164736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_PERFORMCREATE = "performCreate";

    public static final String FUNC_PERFORMCREATE2 = "performCreate2";

    public static final Event CONTRACTCREATION_EVENT = new Event("ContractCreation", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected CreateCall(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected CreateCall(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected CreateCall(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected CreateCall(String contractAddress, Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ContractCreationEventResponse> getContractCreationEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(CONTRACTCREATION_EVENT, transactionReceipt);
        ArrayList<ContractCreationEventResponse> responses = new ArrayList<ContractCreationEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ContractCreationEventResponse typedResponse = new ContractCreationEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newContract = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ContractCreationEventResponse getContractCreationEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(CONTRACTCREATION_EVENT, log);
        ContractCreationEventResponse typedResponse = new ContractCreationEventResponse();
        typedResponse.log = log;
        typedResponse.newContract = (String) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<ContractCreationEventResponse> contractCreationEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getContractCreationEventFromLog(log));
    }

    public Flowable<ContractCreationEventResponse> contractCreationEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRACTCREATION_EVENT));
        return contractCreationEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> performCreate(BigInteger value,
            byte[] deploymentData) {
        final Function function = new Function(
                FUNC_PERFORMCREATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(value), 
                new org.web3j.abi.datatypes.DynamicBytes(deploymentData)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> performCreate2(BigInteger value,
            byte[] deploymentData, byte[] salt) {
        final Function function = new Function(
                FUNC_PERFORMCREATE2, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(value), 
                new org.web3j.abi.datatypes.DynamicBytes(deploymentData), 
                new org.web3j.abi.datatypes.generated.Bytes32(salt)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static CreateCall load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new CreateCall(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static CreateCall load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new CreateCall(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static CreateCall load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new CreateCall(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static CreateCall load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new CreateCall(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<CreateCall> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CreateCall.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<CreateCall> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CreateCall.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<CreateCall> deploy(Web3j web3j, TransactionManager transactionManager,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(CreateCall.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<CreateCall> deploy(Web3j web3j, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(CreateCall.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class ContractCreationEventResponse extends BaseEventResponse {
        public String newContract;
    }
}
