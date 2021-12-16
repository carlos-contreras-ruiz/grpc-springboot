package com.github.carloscontrerasruiz.client.bank;

import com.github.carloscontrerasruiz.interceptor.DeadlineInterceptor;
import com.github.carloscontrerasruiz.proto.BankServiceGrpc;
import com.github.carloscontrerasruiz.proto.DepositRequest;
import com.github.carloscontrerasruiz.proto.WithdrawRequest;
import com.github.carloscontrerasruiz.ssl.GrpcServerSsl;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.net.ssl.SSLException;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClient {

    BankServiceGrpc.BankServiceBlockingStub blockingStub;
    BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {

        URL resourceCa = GrpcServerSsl.class.getClassLoader().getResource("ssl/ca.cert.pem");

        SslContext sslContext = null;
        try {
            sslContext = GrpcSslContexts.forClient()
                    .trustManager(new File(resourceCa.toURI()))
                    .build();
        } catch (SSLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 6565)
                .intercept(new DeadlineInterceptor())
                .sslContext(sslContext)
                //.usePlaintext()
                .build();

        this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
        this.bankServiceStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void withdrawTest() {
        this.blockingStub
                //we can set a custom deadline
                .withDeadline(Deadline.after(4, TimeUnit.SECONDS))
                .withdraw(
                        WithdrawRequest.newBuilder()
                                .setAccountNumber(1)
                                .setAmount(50)
                                .build()
                )
                //foreach remainig es un consumer cuando el server es streaming
                .forEachRemaining(money -> System.out.println("Received: " + money.getValue()));
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
