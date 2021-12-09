package com.github.carloscontrerasruiz;

import com.github.carloscontrerasruiz.proto.Address;
import com.github.carloscontrerasruiz.proto.Car;
import com.github.carloscontrerasruiz.proto.Person;

import java.util.Arrays;
import java.util.List;

public class CompositionDemo {
    public static void main(String[] args) {
        Address address = Address.newBuilder().setPostbox(123).setStreet("calle").setCity("ciudad").build();

        Car car = Car.newBuilder().setMake("honda").setModel("accord").setYear(2020).build();

        List<Car> cars = Arrays.asList(Car.newBuilder().setMake("honda").setModel("accord").setYear(2020).build(),
                Car.newBuilder().setMake("honda").setModel("accord").setYear(2020).build(),
                Car.newBuilder().setMake("honda").setModel("accord").setYear(2020).build());
        Person sam = Person.newBuilder().setName("sam").setAge(25)
                .addAllCar(cars)
                .addCar(car)
                //.setCar(car)
                .setAddress(address)
                .build();

        System.out.println(sam.toString());
    }
}
