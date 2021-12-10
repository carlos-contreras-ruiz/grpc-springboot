package com.github.carloscontrerasruiz;

import com.github.carloscontrerasruiz.proto.Television;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VersionCompatibilityTest {
    public static void main(String[] args) {
        try {
            Path pathv1 = Paths.get("tv-v1");
            Path pathv2 = Paths.get("tv-v2");

         /*   Television sony = Television.newBuilder()
                .setBrand("sony")
                .setModel(2016).setType(Type.OLED)
                .build();



            Files.write(pathv2, sony.toByteArray());*/




            byte[] bytes = Files.readAllBytes(pathv1);

           System.out.println(Television.parseFrom(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
