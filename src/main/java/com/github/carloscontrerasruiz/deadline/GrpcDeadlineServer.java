package com.github.carloscontrerasruiz.deadline;

import com.github.carloscontrerasruiz.service.PersonImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcDeadlineServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(6565)
                .addService(new PersonDeadlineImpl())
                .build();
        server.start();
        server.awaitTermination();
    }
}
