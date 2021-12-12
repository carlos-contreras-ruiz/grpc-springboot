package com.github.carloscontrerasruiz.transferImpl;

import com.github.carloscontrerasruiz.data.BankDatabase;
import com.github.carloscontrerasruiz.proto.Account;
import com.github.carloscontrerasruiz.proto.TransferRequest;
import com.github.carloscontrerasruiz.proto.TransferResponse;
import com.github.carloscontrerasruiz.proto.TransferStatus;
import io.grpc.stub.StreamObserver;

public class TransferStreamingRequest implements StreamObserver<TransferRequest> {

    private StreamObserver<TransferResponse> responseStreamObserver;

    public TransferStreamingRequest(StreamObserver<TransferResponse> responseStreamObserver) {
        this.responseStreamObserver = responseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        int fromAccount = transferRequest.getFromAccount();
        int toAccount = transferRequest.getToAccount();
        int amount = transferRequest.getAmount();
        int balance = BankDatabase.getBalance(fromAccount);
        TransferStatus status = TransferStatus.FAILED;

        if (balance >= amount && fromAccount != toAccount) {
            BankDatabase.deductBalance(fromAccount, amount);
            BankDatabase.addBalance(toAccount, amount);
            status = TransferStatus.SUCCESS;
        }
        Account fromAccountInfo = Account.newBuilder().setAccountNumber(fromAccount)
                .setAmount(BankDatabase.getBalance(fromAccount))
                .build();

        Account toAccountInfo = Account.newBuilder().setAccountNumber(toAccount)
                .setAmount(BankDatabase.getBalance(toAccount))
                .build();

        TransferResponse transferResponse = TransferResponse.newBuilder()
                .setStatus(status)
                .addAccounts(fromAccountInfo)
                .addAccounts(toAccountInfo)
                .build();

        this.responseStreamObserver.onNext(transferResponse);

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        System.out.println(
                BankDatabase.allAccounts()
        );

        this.responseStreamObserver.onCompleted();
    }
}
