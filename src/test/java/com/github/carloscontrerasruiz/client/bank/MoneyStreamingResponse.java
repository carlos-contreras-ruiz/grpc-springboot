package com.github.carloscontrerasruiz.client.bank;

import com.github.carloscontrerasruiz.proto.Money;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingResponse implements StreamObserver<Money> {

    private CountDownLatch countDownLatch;

    public MoneyStreamingResponse(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onNext(Money money) {
        System.out.println("Received async: "+money.getValue());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Exception: "+throwable.getMessage());
        this.countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Stream completed");
        this.countDownLatch.countDown();
    }
}
