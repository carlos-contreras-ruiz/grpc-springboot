package com.github.carloscontrerasruiz.interceptor;

import com.github.carloscontrerasruiz.service.BankService;
import com.github.carloscontrerasruiz.service.PersonImpl;
import com.github.carloscontrerasruiz.service.TransferService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServerMetadata {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(6565)
                .intercept(new AuthInterceptor())
                .addService(new MetadataService())
                .build();
        server.start();
        server.awaitTermination();
    }
}
