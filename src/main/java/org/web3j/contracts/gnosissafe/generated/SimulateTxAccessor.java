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
public class SimulateTxAccessor extends Contract {
    public static final String BINARY = "60a0604052348015600e575f5ffd5b503060805260805161032961002a5f395f606601526103295ff3fe608060405234801561000f575f5ffd5b5060043610610029575f3560e01c80631c5fb2111461002d575b5f5ffd5b61004061003b3660046101d0565b610058565b60405161004f93929190610275565b60405180910390f35b5f8060606001600160a01b037f00000000000000000000000000000000000000000000000000000000000000001630036100fe5760405162461bcd60e51b815260206004820152603960248201527f53696d756c61746554784163636573736f722073686f756c64206f6e6c79206260448201527f652063616c6c6564207669612064656c656761746563616c6c00000000000000606482015260840160405180910390fd5b5f5a9050610144898989898080601f0160208091040260200160405190810160405280939291908181526020018383808284375f920191909152508b925050505a610179565b92505a61015190826102ba565b935060405160203d0181016040523d81523d5f602083013e8092505050955095509592505050565b5f600183600181111561018e5761018e6102df565b036101a5575f5f8551602087018986f490506101b4565b5f5f855160208701888a87f190505b95945050505050565b8035600281106101cb575f5ffd5b919050565b5f5f5f5f5f608086880312156101e4575f5ffd5b85356001600160a01b03811681146101fa575f5ffd5b945060208601359350604086013567ffffffffffffffff81111561021c575f5ffd5b8601601f8101881361022c575f5ffd5b803567ffffffffffffffff811115610242575f5ffd5b886020828401011115610253575f5ffd5b60209190910193509150610269606087016101bd565b90509295509295909350565b8381528215156020820152606060408201525f82518060608401528060208501608085015e5f608082850101526080601f19601f830116840101915050949350505050565b818103818111156102d957634e487b7160e01b5f52601160045260245ffd5b92915050565b634e487b7160e01b5f52602160045260245ffdfea2646970667358221220381af13aa8e57dec24caf8c9771e723712849dc2e83c28d769b6e3b38025845064736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_SIMULATE = "simulate";

    @Deprecated
    protected SimulateTxAccessor(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SimulateTxAccessor(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SimulateTxAccessor(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SimulateTxAccessor(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> simulate(String to, BigInteger value, byte[] data,
            BigInteger operation) {
        final Function function = new Function(
                FUNC_SIMULATE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(value), 
                new org.web3j.abi.datatypes.DynamicBytes(data), 
                new org.web3j.abi.datatypes.generated.Uint8(operation)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SimulateTxAccessor load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimulateTxAccessor(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SimulateTxAccessor load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimulateTxAccessor(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SimulateTxAccessor load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SimulateTxAccessor(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SimulateTxAccessor load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SimulateTxAccessor(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SimulateTxAccessor> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SimulateTxAccessor.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<SimulateTxAccessor> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SimulateTxAccessor.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SimulateTxAccessor> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SimulateTxAccessor.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<SimulateTxAccessor> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SimulateTxAccessor.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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
