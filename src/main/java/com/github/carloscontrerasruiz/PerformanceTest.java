package com.github.carloscontrerasruiz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.carloscontrerasruiz.json.JsonPerson;
import com.github.carloscontrerasruiz.proto.Person;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;

public class PerformanceTest {
    public static void main(String[] args) {
        //json
        JsonPerson person = new JsonPerson();
        person.setName("sam");
        person.setAge(10);
        ObjectMapper mapper = new ObjectMapper();

        Runnable runnable1 = ()->{
            try {
                byte[] bytes = mapper.writeValueAsBytes(person);
                JsonPerson person1 = mapper.readValue(bytes, JsonPerson.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };


        //protobuf
        Person sam = Person.newBuilder().setName("sam").setAge(10).build();
        Runnable runnable2 = ()->{
            byte[] bytes = sam.toByteArray();
            try {
                Person sam1 = Person.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < 5; i++) {
            runPerformanceTest(runnable1,"JSONMETHOD");
            runPerformanceTest(runnable2,"GRPCMETHOD");
        }


    }

    private static void runPerformanceTest(Runnable runnable, String method){
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 5000000; i++) {
            runnable.run();
        }
        long time2 = System.currentTimeMillis();
        System.out.println(method + " : "+(time2-time1)+"ms");
    }
}
