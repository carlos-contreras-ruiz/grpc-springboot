package com.github.carloscontrerasruiz.client;

import com.github.carloscontrerasruiz.proto.Person;
import com.github.carloscontrerasruiz.proto.PersonServiceGrpc;
import com.github.carloscontrerasruiz.proto.Response;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonServiceClient {

    private PersonServiceGrpc.PersonServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        blockingStub = PersonServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void personServiceTest(){
        Response resp = this.blockingStub.getPerson(
                Person.newBuilder()
                        .setName("Carlos")
                        .build()
        );

        System.out.println(resp);

    }

    @Test
    public void personServiceTestByAge(){
        Person resp = this.blockingStub.getPersonByAge(
                Person.newBuilder()
                        .setName("Carlos")
                        .setAge(4)
                        .build()
        );

        System.out.println(resp);

    }
}
