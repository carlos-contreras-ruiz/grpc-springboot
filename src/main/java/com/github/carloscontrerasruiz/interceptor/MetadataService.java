package com.github.carloscontrerasruiz.interceptor;

import com.github.carloscontrerasruiz.bankImpl.CashStreamingRequest;
import com.github.carloscontrerasruiz.data.BankDatabase;
import com.github.carloscontrerasruiz.proto.*;
import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;

public class MetadataService extends BankServiceGrpc.BankServiceImplBase {


    @Override
    public void withdrawOneOfError(WithdrawRequest request, StreamObserver<ResponseErrorHandling> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();

        int balance = BankDatabase.getBalance(accountNumber);

        if (amount < 10 || (amount % 10) != 0) {
            responseObserver.onNext(ResponseErrorHandling.newBuilder()
                    .setErrorResponse(ErrorResponse.newBuilder()
                            .setErrorMessage(ErrorMessage.ONLY_TEN_MULTIPLES)
                            .build())
                    .build());
            responseObserver.onCompleted();
            return;
        }
        if (balance < amount) {
            responseObserver.onNext(ResponseErrorHandling.newBuilder()
                    .setErrorResponse(ErrorResponse.newBuilder()
                            .setErrorMessage(ErrorMessage.INSUFFICIENTE_BALANCE)
                            .build())
                    .build());
            responseObserver.onCompleted();
            return;
        }

        //all the validations pass
        for (int i = 0; i < (amount / 10); i++) {


            //Revisar si hay alguien escuchando la respuesta
            if (!Context.current().isCancelled()) {
                Money money = Money.newBuilder().setValue(10).build();
                ResponseErrorHandling responseErrorHandling = ResponseErrorHandling.newBuilder()
                        .setSuccessResponse(SuccessResponse.newBuilder()
                                .setMoney(money)
                                .build())
                        .build();
                responseObserver.onNext(responseErrorHandling);
            } else {
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
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();

        ServerConstants.UserRole userRole = ServerConstants.CTX_USER_ROLE_KEY.get();
        if (ServerConstants.UserRole.PRIME.equals(userRole)) {
            System.out.println("El usuario es prime " + userRole);
        } else {
            System.out.println("El usuario NO es prime " + userRole);
        }
        int balance = BankDatabase.getBalance(accountNumber);

        if (amount < 10 || (amount % 10) != 0) {
            Metadata metadata = new Metadata();
            Metadata.Key<WithdrawalError> errorKey = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance());
            WithdrawalError withdrawalError = WithdrawalError.newBuilder()
                    .setAmount(balance)
                    .setErrorMessage(ErrorMessage.ONLY_TEN_MULTIPLES).build();
            metadata.put(errorKey, withdrawalError);
            responseObserver.onError(Status.FAILED_PRECONDITION
                    .withDescription("Descripcion del eror")
                    .asRuntimeException(metadata));
            return;
        }
        if (balance < amount) {
            Metadata metadata = new Metadata();
            Metadata.Key<WithdrawalError> errorKey = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance());
            WithdrawalError withdrawalError = WithdrawalError.newBuilder()
                    .setAmount(amount)
                    .setErrorMessage(ErrorMessage.INSUFFICIENTE_BALANCE).build();
            metadata.put(errorKey, withdrawalError);
            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException(metadata));
            return;
        }

        //all the validations pass
        for (int i = 0; i < (amount / 10); i++) {
            Money money = Money.newBuilder().setValue(10).build();

            //Revisar si hay alguien escuchando la respuesta
            if (!Context.current().isCancelled()) {
                responseObserver.onNext(money);
            } else {
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
