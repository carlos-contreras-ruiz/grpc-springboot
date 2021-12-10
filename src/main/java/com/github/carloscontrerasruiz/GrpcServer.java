package com.github.carloscontrerasruiz;

import com.github.carloscontrerasruiz.service.PersonImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(6565)
                .addService(new PersonImpl())
                .build();
        server.start();
        server.awaitTermination();
    }
}
