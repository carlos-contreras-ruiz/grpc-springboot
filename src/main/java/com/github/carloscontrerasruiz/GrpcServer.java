package com.github.carloscontrerasruiz;

import com.github.carloscontrerasruiz.service.BankService;
import com.github.carloscontrerasruiz.service.PersonImpl;
import com.github.carloscontrerasruiz.service.TransferService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(6565)
                .addService(new PersonImpl())
                .addService(new BankService())
                .addService(new TransferService())
                .build();
        server.start();
        server.awaitTermination();
    }
}
