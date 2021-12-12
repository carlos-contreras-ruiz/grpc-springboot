package com.github.carloscontrerasruiz.data;

import com.github.carloscontrerasruiz.proto.Person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BankDatabase {

    private static Map<Integer, Integer> MAP = new HashMap<Integer, Integer>() {{
        put(1, 100);
        put(2, 120);
        put(3, 130);
        put(4, 140);
        put(5, 150);
        put(6, 160);
        put(7, 170);
        put(8, 180);
        put(9, 190);
        put(10, 200);
        put(11, 210);
    }};

    public static int getBalance(int accountnNumber) {
        return MAP.get(accountnNumber);
    }


    public static int addBalance(int accountNumber, int amount) {
        MAP.put(accountNumber, amount);
        return MAP.get(accountNumber);
    }

    public static int deductBalance(int accountNumber, int amount) {
        MAP.put(accountNumber, (MAP.get(accountNumber) - amount));
        return MAP.get(accountNumber);
    }

    public static int addBalanceCorrect(int accountNumber, int amount) {
        MAP.put(accountNumber, (MAP.get(accountNumber) + amount));
        return MAP.get(accountNumber);
    }

    public static Map<Integer, Integer> allAccounts() {
        return MAP;
    }
}
