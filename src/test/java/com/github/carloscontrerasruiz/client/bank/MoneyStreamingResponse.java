package com.github.carloscontrerasruiz.client.bank;

import com.github.carloscontrerasruiz.client.interceptors.ClientConstants;
import com.github.carloscontrerasruiz.proto.Money;
import com.github.carloscontrerasruiz.proto.WithdrawalError;
import io.grpc.Metadata;
import io.grpc.Status;
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
        Status status = Status.fromThrowable(throwable);
        Metadata metadata = Status.trailersFromThrowable(throwable);

        WithdrawalError withdrawalError = metadata.get(ClientConstants.WITHDRAWAL_ERROR_KEY);
        System.out.println("SATSU: "+status.getCode());
        System.out.println("SATSU: "+status.getDescription());
        System.out.println(withdrawalError.getAmount() +" || "+withdrawalError.getErrorMessage());
        System.out.println("Exception: "+throwable.getMessage());
        this.countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Stream completed");
        this.countDownLatch.countDown();
    }
}
