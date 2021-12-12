package com.github.carloscontrerasruiz.deadline;


import com.github.carloscontrerasruiz.data.PersonDatabase;
import com.github.carloscontrerasruiz.proto.Person;
import com.github.carloscontrerasruiz.proto.PersonServiceGrpc;
import com.github.carloscontrerasruiz.proto.Response;
import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class PersonDeadlineImpl extends PersonServiceGrpc.PersonServiceImplBase {
    @Override
    public void getPersonByAge(Person request, StreamObserver<Person> responseObserver) {

        Person person = PersonDatabase.getPerson(request);
        //simulate time consuming
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        responseObserver.onNext(person);
        responseObserver.onCompleted();
    }

    @Override
    public void getPerson(Person request, StreamObserver<Response> responseObserver) {
        String name = request.getName();
        int age = request.getAge();

        responseObserver.onNext(Response.newBuilder().setResponse("Hola " + name).build());
        responseObserver.onCompleted();
    }
}
