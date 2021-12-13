package com.github.carloscontrerasruiz.client.deadline;

import com.github.carloscontrerasruiz.interceptor.DeadlineInterceptor;
import com.github.carloscontrerasruiz.proto.Person;
import com.github.carloscontrerasruiz.proto.PersonServiceGrpc;
import com.github.carloscontrerasruiz.proto.Response;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonServiceDeadlineClient {

    private PersonServiceGrpc.PersonServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .intercept(new DeadlineInterceptor())
                .usePlaintext()
                .build();

        blockingStub = PersonServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void personServiceTest(){
        Response resp = this.blockingStub
                //.withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                .getPerson(
                Person.newBuilder()
                        .setName("Carlos")
                        .build()
        );

        System.out.println(resp);

    }

    @Test
    public void personServiceTestByAge(){
        try{
            Person resp = this.blockingStub
                    //.withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .getPersonByAge(
                            Person.newBuilder()
                                    .setName("Carlos")
                                    .setAge(4)
                                    .build()
                    );
            System.out.println(resp);
        }catch (StatusRuntimeException e){
            System.out.println("Deadline "+e.getMessage());
        }



    }
}
