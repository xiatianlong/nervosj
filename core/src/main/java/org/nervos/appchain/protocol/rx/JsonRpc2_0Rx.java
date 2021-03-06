package org.nervos.appchain.protocol.rx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import org.nervos.appchain.protocol.Nervosj;
import org.nervos.appchain.protocol.core.DefaultBlockParameter;
import org.nervos.appchain.protocol.core.DefaultBlockParameterName;
import org.nervos.appchain.protocol.core.DefaultBlockParameterNumber;
import org.nervos.appchain.protocol.core.filters.BlockFilter;
import org.nervos.appchain.protocol.core.filters.Filter;
import org.nervos.appchain.protocol.core.filters.LogFilter;
import org.nervos.appchain.protocol.core.filters.PendingTransactionFilter;
import org.nervos.appchain.protocol.core.methods.request.AppFilter;
import org.nervos.appchain.protocol.core.methods.response.AppBlock;
import org.nervos.appchain.protocol.core.methods.response.Log;
import org.nervos.appchain.protocol.core.methods.response.Transaction;
import org.nervos.appchain.utils.Observables;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * nervosj reactive API implementation.
 */
public class JsonRpc2_0Rx {

    private final Nervosj nervosj;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Scheduler scheduler;

    public JsonRpc2_0Rx(Nervosj nervosj, ScheduledExecutorService scheduledExecutorService) {
        this.nervosj = nervosj;
        this.scheduledExecutorService = scheduledExecutorService;
        this.scheduler = Schedulers.from(scheduledExecutorService);
    }

    public Observable<String> appBlockHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            BlockFilter blockFilter = new BlockFilter(
                    nervosj, subscriber::onNext);
            run(blockFilter, subscriber, pollingInterval);
        });
    }

    public Observable<String> appPendingTransactionHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(
                    nervosj, subscriber::onNext);

            run(pendingTransactionFilter, subscriber, pollingInterval);
        });
    }

    public Observable<Log> appLogObservable(
            AppFilter appFilter, long pollingInterval) {
        return Observable.create((Subscriber<? super Log> subscriber) -> {
            LogFilter logFilter = new LogFilter(
                    nervosj, subscriber::onNext, appFilter);

            run(logFilter, subscriber, pollingInterval);
        });
    }

    private <T> void run(
            Filter<T> filter, Subscriber<? super T> subscriber,
            long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        subscriber.add(Subscriptions.create(filter::cancel));
    }

    public Observable<Transaction> transactionObservable(long pollingInterval) {
        return blockObservable(true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<Transaction> pendingTransactionObservable(long pollingInterval) {
        return appPendingTransactionHashObservable(pollingInterval)
                .flatMap(transactionHash ->
                        nervosj.appGetTransactionByHash(transactionHash).observable())
                .map(appTransaction -> appTransaction.getTransaction().get());
    }

    public Observable<AppBlock> blockObservable(
            boolean fullTransactionObjects, long pollingInterval) {
        return appBlockHashObservable(pollingInterval)
                .flatMap(blockHash ->
                        nervosj.appGetBlockByHash(blockHash, fullTransactionObjects).observable());
    }

    public Observable<AppBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksObservable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Observable<AppBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, ascending)
                .subscribeOn(scheduler);
    }

    private Observable<AppBlock> replayBlocksObservableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Observable<AppBlock> replayBlocksObservableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            endBlockNumber = getBlockNumber(endBlock);
        } catch (IOException e) {
            Observable.error(e);
        }

        if (ascending) {
            return Observables.range(startBlockNumber, endBlockNumber)
                    .flatMap(i -> nervosj.appGetBlockByNumber(
                            new DefaultBlockParameterNumber(i),
                            fullTransactionObjects).observable());
        } else {
            return Observables.range(startBlockNumber, endBlockNumber, false)
                    .flatMap(i -> nervosj.appGetBlockByNumber(
                            new DefaultBlockParameterNumber(i),
                            fullTransactionObjects).observable());
        }
    }

    public Observable<Transaction> replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return replayBlocksObservable(startBlock, endBlock, true)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<AppBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<AppBlock> onCompleteObservable) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return catchUpToLatestBlockObservableSync(
                startBlock, fullTransactionObjects, onCompleteObservable)
                .subscribeOn(scheduler);
    }

    public Observable<AppBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, Observable.empty());
    }

    private Observable<AppBlock> catchUpToLatestBlockObservableSync(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<AppBlock> onCompleteObservable) {

        BigInteger startBlockNumber;
        BigInteger latestBlockNumber;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            latestBlockNumber = getLatestBlockNumber();
        } catch (IOException e) {
            return Observable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteObservable;
        } else {
            return Observable.concat(
                    replayBlocksObservableSync(
                            new DefaultBlockParameterNumber(startBlockNumber),
                            new DefaultBlockParameterNumber(latestBlockNumber),
                            fullTransactionObjects),
                    Observable.defer(() -> catchUpToLatestBlockObservableSync(
                            new DefaultBlockParameterNumber(latestBlockNumber.add(BigInteger.ONE)),
                            fullTransactionObjects,
                            onCompleteObservable)));
        }
    }

    public Observable<Transaction> catchUpToLatestTransactionObservable(
            DefaultBlockParameter startBlock) {
        return catchUpToLatestBlockObservable(
                startBlock, true, Observable.empty())
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<AppBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            long pollingInterval) {

        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects,
                blockObservable(fullTransactionObjects, pollingInterval));
    }

    public Observable<Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock, long pollingInterval) {
        return catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlock, true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    private BigInteger getLatestBlockNumber() throws IOException {
        return getBlockNumber(DefaultBlockParameterName.LATEST);
    }

    private BigInteger getBlockNumber(
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        if (defaultBlockParameter instanceof DefaultBlockParameterNumber) {
            return ((DefaultBlockParameterNumber) defaultBlockParameter).getBlockNumber();
        } else {
            AppBlock latestEthBlock = nervosj.appGetBlockByNumber(
                    defaultBlockParameter, false).send();
            return latestEthBlock.getBlock().getHeader().getNumberDec();
        }
    }

    private static List<Transaction> toTransactions(AppBlock appBlock) {
        // If you ever see an exception thrown here, it's probably due to an incomplete chain in
        // Geth/Parity. You should resync to solve.
        return appBlock.getBlock().getBody().getTransactions().stream()
                .map(transactionResult -> (Transaction) transactionResult.get())
                .collect(Collectors.toList());
    }
}
