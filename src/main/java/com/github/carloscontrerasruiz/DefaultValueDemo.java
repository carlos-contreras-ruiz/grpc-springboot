package com.github.carloscontrerasruiz;

import com.github.carloscontrerasruiz.proto.Person;

public class DefaultValueDemo {

    public static void main(String[] args) {
        Person build = Person.newBuilder().build();

        System.out.println(build.getAddress());
        System.out.println(build.getCarList());
        System.out.println(build.getName());
        System.out.println(build.getAge());
        System.out.println(build.getCarCount());

        //Para saber si tiene valor una propiedadse usa has
        //Si es un message propio
        System.out.println(build.hasAddress());
        System.out.println(build.getCarCount());
    }
}
