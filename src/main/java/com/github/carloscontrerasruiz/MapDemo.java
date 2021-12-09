package com.github.carloscontrerasruiz;


import com.github.carloscontrerasruiz.proto.common.BodyStyle;
import com.github.carloscontrerasruiz.proto.common.Car;
import com.github.carloscontrerasruiz.proto.common.Dealer;

public class MapDemo {

    public static void main(String[] args) {
        Car honda = Car.newBuilder().setMake("honda")
                .setModel("accord")
                .setBodyStyle(BodyStyle.PICKUP)
                .setYear(2020).build();
        Car civic = Car.newBuilder().setMake("honda").setModel("civic").setYear(2020).build();

         Dealer builder = Dealer.newBuilder().putModel(2000,civic)
                .putModel(2001,honda).build();

        System.out.println(builder.getModelOrThrow(2000).getBodyStyle());
    }





}
