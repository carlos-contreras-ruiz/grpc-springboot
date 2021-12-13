package com.github.carloscontrerasruiz.interceptor;

import com.github.carloscontrerasruiz.bankImpl.CashStreamingRequest;
import com.github.carloscontrerasruiz.data.BankDatabase;
import com.github.carloscontrerasruiz.proto.*;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class MetadataService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();

        ServerConstants.UserRole userRole = ServerConstants.CTX_USER_ROLE_KEY.get();
        if (ServerConstants.UserRole.PRIME.equals(userRole)) {
            System.out.println("El usuario es prime "+userRole);
        }else{
            System.out.println("El usuario NO es prime "+userRole);
        }
        int balance = BankDatabase.getBalance(accountNumber);

        if (balance < amount) {
            Status not_enough_money = Status.FAILED_PRECONDITION.withDescription("Not enough money");
            responseObserver.onError(not_enough_money.asRuntimeException());
        }

        //all the validations pass
        for (int i = 0; i < (amount / 10); i++) {
            Money money = Money.newBuilder().setValue(10).build();

            //Revisar si hay alguien escuchando la respuesta
            if (!Context.current().isCancelled()) {
                responseObserver.onNext(money);
            }else{
                break;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                responseObserver.onError(
                        Status.INTERNAL.withDescription("Something goes wrong")
                                .asRuntimeException()
                );
            }
        }
        responseObserver.onCompleted();

    }

    @Override
    public StreamObserver<DepositRequest> cashDeposit(StreamObserver<Balance> responseObserver) {
        return new CashStreamingRequest(responseObserver);
    }


}
