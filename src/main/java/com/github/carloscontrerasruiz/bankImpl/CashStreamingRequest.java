package com.github.carloscontrerasruiz.bankImpl;

import com.github.carloscontrerasruiz.data.BankDatabase;
import com.github.carloscontrerasruiz.proto.Balance;
import com.github.carloscontrerasruiz.proto.DepositRequest;
import io.grpc.stub.StreamObserver;

public class CashStreamingRequest implements StreamObserver<DepositRequest> {

    private StreamObserver<Balance> balanceStreamObserver;
    private int accountBalance;

    public CashStreamingRequest(StreamObserver<Balance> balanceStreamObserver) {
        this.balanceStreamObserver = balanceStreamObserver;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        int accountNumber = depositRequest.getAccountNumber();
        int amount = depositRequest.getAmount();
        this.accountBalance = BankDatabase.addBalance(accountNumber,
                (BankDatabase.getBalance(accountNumber) + amount));
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("Algo paso con el cliente " + throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        Balance balance = Balance.newBuilder().setAmount(this.accountBalance).build();
        this.balanceStreamObserver.onNext(balance);
        this.balanceStreamObserver.onCompleted();
    }
}
