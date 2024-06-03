package org.example;

import java.util.*;

import static org.example.Currency.*;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Loadable> saveList = new HashMap<>();
        System.out.println("Hello world! \n Task 1");
        Account acc= new Account("Default Name");
        acc.updateMoney(RUB, 1000L);
        acc.updateMoney(USD, 300L);
        acc.updateMoney(EUR, 99_999_999_999_999_999L);

        System.out.println("Заполнил счет : \n" + acc);
        acc.undo();
        System.out.println("Откатил последнюю операцию \n" + acc);

        saveList.put("save 1", acc.save());
        System.out.println("save 1- ok");
        acc.updateMoney(EUR, 500L);
        System.out.println("New sum for EUR :" + acc);

        saveList.put("save 2", acc.save());
        System.out.println("save 2 - OK");
        acc.updateMoney(RUB, 900000L);
        System.out.println("RUB updated :" + acc);
        saveList.put("save 99", acc.save());
        System.out.println("save 99 - OK");

        Account acc2= new Account("Default Name 2");
        acc2.updateMoney(RUB, 1002L);
        acc2.updateMoney(USD, 302L);
        acc2.updateMoney(EUR, 10_999_999_999_000_000L);

        saveList.get("save 1").load();

        System.out.println(acc);
        System.out.println(acc2);


        //(acc.save());

        for (Currency cur : Currency.values()) {
            System.out.println(cur.a3Code + " - " + cur.numCode + " - " + cur.symbol + " - " + cur.displayName);
        }
    }
}

