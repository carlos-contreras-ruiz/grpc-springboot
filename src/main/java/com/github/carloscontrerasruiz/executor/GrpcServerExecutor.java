package com.github.carloscontrerasruiz.executor;

import com.github.carloscontrerasruiz.service.BankService;
import com.github.carloscontrerasruiz.service.PersonImpl;
import com.github.carloscontrerasruiz.service.TransferService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//cachedThreadPool deafault
//directExecutor better performance for non-blocking
//FixedThreadPoll Preferd for the teacher
public class GrpcServerExecutor {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(6565)
                //.directExecutor()
                .executor(Executors.newFixedThreadPool(20))
                .addService(new PersonImpl())
                .addService(new BankService())
                .addService(new TransferService())
                .build();
        server.start();
        server.awaitTermination();
    }
}
