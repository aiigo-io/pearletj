package org.web3j.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes4;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
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
public class TokenCallbackHandler extends Contract {
    public static final String BINARY = "6080604052348015600e575f5ffd5b5061048f8061001c5f395ff3fe608060405234801561000f575f5ffd5b5060043610610054575f3560e01c806223de291461005857806301ffc9a714610072578063150b7a021461009a578063bc197c81146100d2578063f23a6e61146100f4575b5f5ffd5b6100706100663660046101c5565b5050505050505050565b005b61008561008036600461026e565b610114565b60405190151581526020015b60405180910390f35b6100b96100a836600461029c565b630a85bd0160e11b95945050505050565b6040516001600160e01b03199091168152602001610091565b6100b96100e0366004610347565b63bc197c8160e01b98975050505050505050565b6100b96101023660046103e6565b63f23a6e6160e01b9695505050505050565b5f6001600160e01b03198216630271189760e51b148061014457506001600160e01b03198216630a85bd0160e11b145b8061015f57506001600160e01b031982166301ffc9a760e01b145b92915050565b80356001600160a01b038116811461017b575f5ffd5b919050565b5f5f83601f840112610190575f5ffd5b50813567ffffffffffffffff8111156101a7575f5ffd5b6020830191508360208285010111156101be575f5ffd5b9250929050565b5f5f5f5f5f5f5f5f60c0898b0312156101dc575f5ffd5b6101e589610165565b97506101f360208a01610165565b965061020160408a01610165565b955060608901359450608089013567ffffffffffffffff811115610223575f5ffd5b61022f8b828c01610180565b90955093505060a089013567ffffffffffffffff81111561024e575f5ffd5b61025a8b828c01610180565b999c989b5096995094979396929594505050565b5f6020828403121561027e575f5ffd5b81356001600160e01b031981168114610295575f5ffd5b9392505050565b5f5f5f5f5f608086880312156102b0575f5ffd5b6102b986610165565b94506102c760208701610165565b935060408601359250606086013567ffffffffffffffff8111156102e9575f5ffd5b6102f588828901610180565b969995985093965092949392505050565b5f5f83601f840112610316575f5ffd5b50813567ffffffffffffffff81111561032d575f5ffd5b6020830191508360208260051b85010111156101be575f5ffd5b5f5f5f5f5f5f5f5f60a0898b03121561035e575f5ffd5b61036789610165565b975061037560208a01610165565b9650604089013567ffffffffffffffff811115610390575f5ffd5b61039c8b828c01610306565b909750955050606089013567ffffffffffffffff8111156103bb575f5ffd5b6103c78b828c01610306565b909550935050608089013567ffffffffffffffff81111561024e575f5ffd5b5f5f5f5f5f5f60a087890312156103fb575f5ffd5b61040487610165565b955061041260208801610165565b94506040870135935060608701359250608087013567ffffffffffffffff81111561043b575f5ffd5b61044789828a01610180565b979a969950949750929593949250505056fea264697066735822122070e4ae6b3ddc4db750f6af4ca8015fb5ad419cd2679bddfff3f8f279466d57a164736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ONERC1155BATCHRECEIVED = "onERC1155BatchReceived";

    public static final String FUNC_ONERC1155RECEIVED = "onERC1155Received";

    public static final String FUNC_ONERC721RECEIVED = "onERC721Received";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final String FUNC_TOKENSRECEIVED = "tokensReceived";

    @Deprecated
    protected TokenCallbackHandler(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected TokenCallbackHandler(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected TokenCallbackHandler(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected TokenCallbackHandler(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<byte[]> onERC1155BatchReceived(String param0, String param1,
            List<BigInteger> param2, List<BigInteger> param3, byte[] param4) {
        final Function function = new Function(FUNC_ONERC1155BATCHRECEIVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.Address(160, param1), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(param2, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(param3, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.DynamicBytes(param4)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> onERC1155Received(String param0, String param1,
            BigInteger param2, BigInteger param3, byte[] param4) {
        final Function function = new Function(FUNC_ONERC1155RECEIVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.Address(160, param1), 
                new org.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.web3j.abi.datatypes.generated.Uint256(param3), 
                new org.web3j.abi.datatypes.DynamicBytes(param4)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> onERC721Received(String param0, String param1,
            BigInteger param2, byte[] param3) {
        final Function function = new Function(FUNC_ONERC721RECEIVED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.Address(160, param1), 
                new org.web3j.abi.datatypes.generated.Uint256(param2), 
                new org.web3j.abi.datatypes.DynamicBytes(param3)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes4>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static TokenCallbackHandler load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new TokenCallbackHandler(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static TokenCallbackHandler load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new TokenCallbackHandler(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static TokenCallbackHandler load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TokenCallbackHandler(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TokenCallbackHandler load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TokenCallbackHandler(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<TokenCallbackHandler> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TokenCallbackHandler.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<TokenCallbackHandler> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TokenCallbackHandler.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<TokenCallbackHandler> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(TokenCallbackHandler.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<TokenCallbackHandler> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(TokenCallbackHandler.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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
