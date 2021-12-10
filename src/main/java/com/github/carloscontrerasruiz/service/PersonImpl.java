package com.github.carloscontrerasruiz.service;


import com.github.carloscontrerasruiz.data.PersonDatabase;
import com.github.carloscontrerasruiz.proto.Person;
import com.github.carloscontrerasruiz.proto.PersonServiceGrpc;
import com.github.carloscontrerasruiz.proto.Response;
import io.grpc.stub.StreamObserver;

public class PersonImpl extends PersonServiceGrpc.PersonServiceImplBase {
    @Override
    public void getPersonByAge(Person request, StreamObserver<Person> responseObserver) {

        Person person = PersonDatabase.getPerson(request);

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
