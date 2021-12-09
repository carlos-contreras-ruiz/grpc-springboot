package com.github.carloscontrerasruiz;

import com.github.carloscontrerasruiz.proto.Person;
import com.google.protobuf.Int32Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PersonDemo {

    public static void main(String[] args) {
        Person sam = Person.newBuilder().setName("sam").setAge(10).build();
        /*Person sam = Person.newBuilder().setName("sam")
                .setAge(Int32Value.newBuilder().setValue(32).build())
                .build();
        System.out.println(sam.hasAge());*/
        Person sam2 = Person.newBuilder().setName("Sam").setAge(10).build();
       /* Person sam2 = Person.newBuilder().setName("Sam")
                .setAge(Int32Value.newBuilder().setValue(33).build())
                .build();*/

        System.out.println(sam.toString());
        //El metodo equals de protobuf compara campo por campo
        System.out.println(sam.equals(sam2));

        Path path = Paths.get("sam.ser");
        try {
            Files.write(path, sam.toByteArray());
            byte[] bytes = Files.readAllBytes(path);
            Person newSam = Person.parseFrom(bytes);
            System.out.println(sam.equals(newSam));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
