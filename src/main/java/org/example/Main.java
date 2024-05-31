package org.example;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.example.Currency.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // create a HashMap and add some values
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", 10000);
        map.put("b", 55000);
        map.put("c", 44300);
        map.put("e", 53200);

        // print original map
        System.out.println("HashMap:\n " + map.toString());

        // put a new value which is not mapped
        // before in map
        System.out.println("put new key. Result : " + map.putIfAbsent("d", 77633));

        System.out.println("New HashMap:\n " + map);

        // try to put a new value with existing key
        // before in map
        System.out.println("put exists key. Result : " + map.putIfAbsent("d", 55555));

        // print newly mapped map
        System.out.println("Unchanged HashMap:\n " + map);




        Account acc= new Account("Vaysa");
        //acc.setMoney(RUB, 1000L);

        for (Currency cur : Currency.values()) {
            System.out.println(cur.a3Code + " - " + cur.numCode + " - " + cur.symbol + " - " + cur.displayName);
        }
    }
}

