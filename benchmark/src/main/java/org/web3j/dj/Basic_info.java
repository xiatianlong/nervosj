package org.web3j.dj;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.StaticArray5;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple10;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class Basic_info extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b604051604080610c5e8339810160405280805190602001909190805190602001909190505081600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550806000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050610b98806100c66000396000f30060606040526004361061004c576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063466e94b314610051578063d4fc9fc6146101b6575b600080fd5b341561005c57600080fd5b6101b460048080356000191690602001909190803573ffffffffffffffffffffffffffffffffffffffff16906020019091908035600019169060200190919080356000191690602001909190803560001916906020019091908035600019169060200190919080356000191690602001909190803560001916906020019091908060a00190600580602002604051908101604052809291908260056020028082843782019150505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803573ffffffffffffffffffffffffffffffffffffffff1690602001909190505061036c565b005b34156101c157600080fd5b6101ed600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190505061060c565b604051808b600019166000191681526020018a600019166000191681526020018960001916600019168152602001886000191660001916815260200187600019166000191681526020018660001916600019168152602001856000191660001916815260200184600560200280838360005b8381101561027a57808201518184015260208101905061025f565b505050509050018060200180602001838103835285818151815260200191508051906020019080838360005b838110156102c15780820151818401526020810190506102a6565b50505050905090810190601f1680156102ee5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b8381101561032757808201518184015260208101905061030c565b50505050905090810190601f1680156103545780820380516001836020036101000a031916815260200191505b509c5050505050505050505050505060405180910390f35b610374610946565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156103cf57600080fd5b81600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614151561042c57600080fd5b610160604051908101604052808f6000191681526020018e73ffffffffffffffffffffffffffffffffffffffff1681526020018d6000191681526020018c6000191681526020018b6000191681526020018a600019168152602001896000191681526020018860001916815260200187815260200186815260200185815250915081600260008f73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000820151816000019060001916905560208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060408201518160020190600019169055606082015181600301906000191690556080820151816004019060001916905560a0820151816005019060001916905560c0820151816006019060001916905560e08201518160070190600019169055610100820151816008019060056105bc9291906109de565b5061012082015181600d0190805190602001906105da929190610a24565b5061014082015181600e0190805190602001906105f8929190610a24565b509050505050505050505050505050505050565b600080600080600080600061061f610aa4565b610627610acf565b61062f610acf565b610637610946565b600260008d73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002061016060405190810160405290816000820154600019166000191681526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200160028201546000191660001916815260200160038201546000191660001916815260200160048201546000191660001916815260200160058201546000191660001916815260200160068201546000191660001916815260200160078201546000191660001916815260200160088201600580602002604051908101604052809291908260058015610797576020028201915b8154600019168152602001906001019080831161077f575b50505050508152602001600d82018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108395780601f1061080e57610100808354040283529160200191610839565b820191906000526020600020905b81548152906001019060200180831161081c57829003601f168201915b50505050508152602001600e82018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108db5780601f106108b0576101008083540402835291602001916108db565b820191906000526020600020905b8154815290600101906020018083116108be57829003601f168201915b505050505081525050905080600001518160400151826060015183608001518460a001518560c001518660e001518761010001518861012001518961014001518292508191508090509a509a509a509a509a509a509a509a509a509a50509193959799509193959799565b6101e06040519081016040528060008019168152602001600073ffffffffffffffffffffffffffffffffffffffff1681526020016000801916815260200160008019168152602001600080191681526020016000801916815260200160008019168152602001600080191681526020016109be610ae3565b81526020016109cb610b0e565b81526020016109d8610b0e565b81525090565b8260058101928215610a13579160200282015b82811115610a125782518290600019169055916020019190600101906109f1565b5b509050610a209190610b22565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610a6557805160ff1916838001178555610a93565b82800160010185558215610a93579182015b82811115610a92578251825591602001919060010190610a77565b5b509050610aa09190610b47565b5090565b60a0604051908101604052806005905b6000801916815260200190600190039081610ab45790505090565b602060405190810160405280600081525090565b60a0604051908101604052806005905b6000801916815260200190600190039081610af35790505090565b602060405190810160405280600081525090565b610b4491905b80821115610b40576000816000905550600101610b28565b5090565b90565b610b6991905b80821115610b65576000816000905550600101610b4d565b5090565b905600a165627a7a72305820de4e6d4228a5fdccd81145aec16305dd75a5c1a97ad3aca39361f12ee953682b0029";

    protected Basic_info(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        super(BINARY, contractAddress, web3j, transactionManager);
    }

    public RemoteCall<TransactionReceipt> upload(byte[] _memId, String _memAddress, byte[] _name, byte[] _gender, byte[] _nation, byte[] _birthday, byte[] _political, byte[] _marrage, List<byte[]> _extraInfo, String _homeAddress, String _origin, String _committee, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, BigInteger version) {
        Function function = new Function(
                "upload", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_memId), 
                new org.web3j.abi.datatypes.Address(_memAddress), 
                new org.web3j.abi.datatypes.generated.Bytes32(_name), 
                new org.web3j.abi.datatypes.generated.Bytes32(_gender), 
                new org.web3j.abi.datatypes.generated.Bytes32(_nation), 
                new org.web3j.abi.datatypes.generated.Bytes32(_birthday), 
                new org.web3j.abi.datatypes.generated.Bytes32(_political), 
                new org.web3j.abi.datatypes.generated.Bytes32(_marrage), 
                new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.Utils.typeMap(_extraInfo, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                new org.web3j.abi.datatypes.Utf8String(_homeAddress), 
                new org.web3j.abi.datatypes.Utf8String(_origin), 
                new org.web3j.abi.datatypes.Address(_committee)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, quota, nonce, validUntilBlock, version);
    }

    public RemoteCall<Tuple10<byte[], byte[], byte[], byte[], byte[], byte[], byte[], List<byte[]>, String, String>> query(String _memAddress) {
        final Function function = new Function("query", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_memAddress)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<StaticArray5<Bytes32>>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple10<byte[], byte[], byte[], byte[], byte[], byte[], byte[], List<byte[]>, String, String>>(
                new Callable<Tuple10<byte[], byte[], byte[], byte[], byte[], byte[], byte[], List<byte[]>, String, String>>() {
                    @Override
                    public Tuple10<byte[], byte[], byte[], byte[], byte[], byte[], byte[], List<byte[]>, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);;
                        return new Tuple10<byte[], byte[], byte[], byte[], byte[], byte[], byte[], List<byte[]>, String, String>(
                                (byte[]) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue(), 
                                (byte[]) results.get(4).getValue(), 
                                (byte[]) results.get(5).getValue(), 
                                (byte[]) results.get(6).getValue(), 
                                (List<byte[]>) results.get(7).getValue(), 
                                (String) results.get(8).getValue(), 
                                (String) results.get(9).getValue());
                    }
                });
    }

    public static RemoteCall<Basic_info> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger quota, BigInteger nonce, BigInteger validUntilBlock, BigInteger version, String _committee, String _operator) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_committee), 
                new org.web3j.abi.datatypes.Address(_operator)));
        return deployRemoteCall(Basic_info.class, web3j, transactionManager, quota, nonce, validUntilBlock, version, BINARY, encodedConstructor);
    }

    public static Basic_info load(String contractAddress, Web3j web3j, TransactionManager transactionManager) {
        return new Basic_info(contractAddress, web3j, transactionManager);
    }
}