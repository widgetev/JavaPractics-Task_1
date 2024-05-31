package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;


class AccountTest {
    @DisplayName("Установка пустого имени владельца :")
    @ParameterizedTest(name = "-> \"{0}\"")
    @MethodSource("org.example.NameSource#blankNames")
    void setBlankClientName(String clientName) {
        Account acc = new Account("Vasya");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {acc.setClientName(clientName);});
    }

    @Test
    @DisplayName("Установка NULL в имени владельца")
    void setNullClientName() {
        String str = null;
        Account acc = new Account("Vasya");
        Assertions.assertThrows(NullPointerException.class, () -> {acc.setClientName(str);});
    }

    @DisplayName("Установка имени владельца :")
    @ParameterizedTest(name = "-> \"{0}\"")
    @MethodSource("org.example.NameSource#correctNames")
    void setCorrectClientName(String clientName) {
        Account acc = new Account("Fake Name");
        acc.setClientName(clientName);
    }

    @DisplayName("Установка пустого имени владельца :")
    @ParameterizedTest(name = "-> \"{0}\"")
    @MethodSource("org.example.NameSource#blankNames")
    void newAccoutnBlankClientName(String clientName) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {new Account(clientName);});
    }

    @Test
    @DisplayName("Новый счет с NULL в имени владельца")
    void newAccoutnNullClientName() {
        String str = null;
        Assertions.assertThrows(NullPointerException.class, () -> {new Account(str);});
    }

    @DisplayName("Новый счет с корректным именем владельца :")
    @ParameterizedTest(name = "-> \"{0}\"")
    @MethodSource("org.example.NameSource#correctNames")
    void newAccoutnCorrectClientName(String clientName) {
        Account acc = new Account(clientName);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Получить имя клиента")
    void getClientName() {
        Account acc = new Account("Vasya");
        Assertions.assertEquals(acc.getClientName(), "Vasya");
    }


    @ParameterizedTest(name = "updateMoney (Names) -> \"{0}\"")
    @MethodSource("org.example.CurSource#curCorrectNames")
    void updateMoneyCorrectName(String cur) {
        Account acc = new Account("Vasya");
        acc.updateMoney(Currency.valueOf(cur),99L);
        Assertions.assertEquals(true, acc.getMoney().containsKey(Currency.valueOf(cur)));
    }

    @ParameterizedTest(name = "updateMoney (BAD Names) -> \"{0}\"")
    @MethodSource("org.example.CurSource#curBadNames")
    void updateMoneyBadName(String cur) {
        Account acc = new Account("Vasya");
        Assertions.assertThrows(IllegalArgumentException.class, () -> acc.updateMoney(Currency.valueOf(cur),1L));
    }

    @ParameterizedTest(name = "updateMoney (Values) -> \"{0}\"")
    @MethodSource("org.example.CurSource#curCorrectValues")
    void updateMoneyCorrecValues(Long value) {
        Account acc = new Account("Vasya");
        acc.updateMoney(Currency.valueOf("RUB"), value);
        Assertions.assertEquals(value, acc.getMoney().get(Currency.valueOf("RUB")));
    }

    @ParameterizedTest(name = "updateMoney (BAD Values) -> \"{0}\"")
    @MethodSource("org.example.CurSource#curBadValues")
    void updateMoneyBadValues(String value) {
        Account acc = new Account("Vasya");
        Assertions.assertThrows(IllegalArgumentException.class, () -> acc.updateMoney(Currency.valueOf("RUB"), Long.valueOf(value)));
    }

    @Test
    @DisplayName("updateMoney (NULL Values)")
    void updateMoneyNULLValues() {
        Account acc = new Account("Vasya");
        Assertions.assertThrows(IllegalArgumentException.class, () -> acc.updateMoney(Currency.valueOf("RUB"), null));
    }
}

class NameSource {
    public static List<String> blankNames() {
        return List.of("","  ", "\t");
    }

    public static List<String> correctNames() {
        return List.of("Vasya","vasya", "Иван VI Антонович");
    }
}

class CurSource {
    public static List<String> curCorrectNames() {
        return List.of("RUB","USD", "EUR");
    }
    public static List<String> curBadNames() {
        return List.of("RUBLES","CNY", " ");
    }

    public static List<Long> curCorrectValues() {
        return List.of(1000L,50L, 0L,99999999999999999L);
    }

    public static List<String> curBadValues() {
        return List.of("-5", "-1","-99999999999");
    }
}