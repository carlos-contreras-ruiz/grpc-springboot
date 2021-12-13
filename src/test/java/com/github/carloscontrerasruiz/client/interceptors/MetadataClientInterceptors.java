package com.github.carloscontrerasruiz.client.interceptors;

import com.github.carloscontrerasruiz.client.bank.BalanceStreamObserver;
import com.github.carloscontrerasruiz.client.bank.MoneyStreamingResponse;
import com.github.carloscontrerasruiz.interceptor.DeadlineInterceptor;
import com.github.carloscontrerasruiz.proto.BankServiceGrpc;
import com.github.carloscontrerasruiz.proto.DepositRequest;
import com.github.carloscontrerasruiz.proto.WithdrawRequest;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataClientInterceptors {

    BankServiceGrpc.BankServiceBlockingStub blockingStub;
    BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .intercept(MetadataUtils.newAttachHeadersInterceptor(ClientConstants.getClientToken()))
                .intercept(new DeadlineInterceptor())
                .usePlaintext()
                .build();

        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
        this.bankServiceStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void withdrawTest() {

        for (int i = 0; i < 4; i++) {
            try {
                this.blockingStub
                        //we can set a custom deadline
                        .withDeadline(Deadline.after(4, TimeUnit.SECONDS))
                        .withCallCredentials(new UserSessionToken("user-secret-" + i + " standard"))
                        .withdraw(
                                WithdrawRequest.newBuilder()
                                        .setAccountNumber(1)
                                        .setAmount(50)
                                        .build()
                        )
                        //foreach remainig es un consumer cuando el server es streaming
                        .forEachRemaining(money -> System.out.println("Received: " + money.getValue()));
            } catch (Exception e) {
                System.out.println("Fail number " + i);
            }
        }

    }

    @Test
    public void withdrawTestAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        this.bankServiceStub.withdraw(
                WithdrawRequest.newBuilder()
                        .setAccountNumber(1)
                        .setAmount(50)
                        .build(),
                new MoneyStreamingResponse(latch)
        );
        latch.await();

    }

    @Test
    public void cashStreamingRequest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> streamObserver = this.bankServiceStub.cashDeposit(new BalanceStreamObserver(latch));
        for (int i = 0; i < 10; i++) {
            DepositRequest request = DepositRequest.newBuilder().setAccountNumber(1).setAmount(10).build();
            streamObserver.onNext(request);
        }
        streamObserver.onCompleted();
        latch.await();
    }
}
