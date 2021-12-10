package com.github.carloscontrerasruiz.data;

import com.github.carloscontrerasruiz.proto.Person;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PersonDatabase {

    private static final Map<Integer, Person> MAP = Arrays.asList(
            Person.newBuilder().setName("C1").setAge(1).build(),
            Person.newBuilder().setName("C2").setAge(2).build(),
            Person.newBuilder().setName("C3").setAge(3).build(),
            Person.newBuilder().setName("C4").setAge(4).build(),
            Person.newBuilder().setName("C5").setAge(5).build(),
            Person.newBuilder().setName("C6").setAge(6).build(),
            Person.newBuilder().setName("C7").setAge(7).build(),
            Person.newBuilder().setName("C8").setAge(8).build(),
            Person.newBuilder().setName("C9").setAge(9).build()
    ).stream().collect(Collectors.toMap(
            Person::getAge,
            Function.identity()
    ));

    public static Person getPerson(Person person){
        return MAP.get(person.getAge());
    }


}
