package com.github.carloscontrerasruiz.client.bank;

import com.github.carloscontrerasruiz.proto.Balance;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class BalanceStreamObserver implements StreamObserver<Balance> {

    CountDownLatch latch;

    public BalanceStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Balance balance) {
        System.out.println("Final Balance " + balance.getAmount());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Ocurruo u error "+throwable.getMessage());
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Server is done");
        this.latch.countDown();
    }
}
