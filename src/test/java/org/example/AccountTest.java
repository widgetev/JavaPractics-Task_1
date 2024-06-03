package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.example.Currency.*;


class AccountTest {
    @DisplayName("Установка пустого имени владельца :")
    @ParameterizedTest(name = "-> \"{0}\"")
    @MethodSource("org.example.NameSource#blankNames")
    void setBlankClientName(String clientName) {
        Account acc = new Account("Vasya");
        assertThrows(IllegalArgumentException.class, () -> acc.setClientName(clientName));
    }

    @Test
    @DisplayName("Установка NULL в имени владельца")
    void setNullClientName() {
        String str = null;
        Account acc = new Account("Vasya");
        assertThrows(NullPointerException.class, () -> acc.setClientName(str));
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
        assertThrows(IllegalArgumentException.class, () -> new Account(clientName));
    }

    @Test
    @DisplayName("Новый счет с NULL в имени владельца")
    void newAccoutnNullClientName() {
        String str = null;
        assertThrows(NullPointerException.class, () -> new Account(str));
    }

    @DisplayName("Новый счет с корректным именем владельца :")
    @ParameterizedTest(name = "-> \"{0}\"")
    @MethodSource("org.example.NameSource#correctNames")
    void newAccoutnCorrectClientName(String clientName) {
        Assertions.assertDoesNotThrow(() ->new Account(clientName));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Получить имя клиента")
    void getClientName() {
        Account acc = new Account("Vasya");
        assertEquals(acc.getClientName(), "Vasya");
    }


    @ParameterizedTest(name = "updateMoney (Names) -> \"{0}\"")
    @MethodSource("org.example.CurSource#curCorrectNames")
    void updateMoneyCorrectName(String cur) {
        Account acc = new Account("Vasya");
        acc.updateMoney(Currency.valueOf(cur),99L);
        assertTrue(acc.getMoney().containsKey(valueOf(cur)));
    }

    @ParameterizedTest(name = "updateMoney (BAD Names) -> \"{0}\"")
    @MethodSource("org.example.CurSource#curBadNames")
    void updateMoneyBadName(String cur) {
        Account acc = new Account("Vasya");
        assertThrows(IllegalArgumentException.class, () -> acc.updateMoney(Currency.valueOf(cur),1L));
    }

    @ParameterizedTest(name = "updateMoney (Values) -> \"{0}\"")
    @MethodSource("org.example.CurSource#curCorrectValues")
    void updateMoneyCorrecValues(Long value) {
        Account acc = new Account("Vasya");
        acc.updateMoney(Currency.valueOf("RUB"), value);
        assertEquals(value, acc.getMoney().get(Currency.valueOf("RUB")));
    }

    @ParameterizedTest(name = "updateMoney (BAD Values) -> \"{0}\"")
    @MethodSource("org.example.CurSource#curBadValues")
    void updateMoneyBadValues(String value) {
        Account acc = new Account("Vasya");
        assertThrows(IllegalArgumentException.class, () -> acc.updateMoney(Currency.valueOf("RUB"), Long.valueOf(value)));
    }

    @Test
    @DisplayName("updateMoney (NULL Values)")
    void updateMoneyNULLValues() {
        Account acc = new Account("Vasya");
        assertThrows(IllegalArgumentException.class, () -> acc.updateMoney(Currency.valueOf("RUB"), null));
    }

    @Test
    void undoName() {
        Account acc = new Account("Default Name");
        String dName = acc.getClientName();
        acc.setClientName("Fake Name");

        assertNotEquals(dName, acc.getClientName());
        assertEquals(dName,acc.undo().getClientName());
        assertThrows(NothingToUndo.class, acc::undo);
    }

    @Test
    void undoMoney() {
        Account acc = new Account("Default Name");
        acc.updateMoney(RUB,100L);
        acc.updateMoney(EUR,200L);
        acc.updateMoney(USD,300L);
        Long rub = acc.getMoney().get(RUB);
        Long eur = acc.getMoney().get(EUR);
        Long usd = acc.getMoney().get(USD);

        acc.updateMoney(RUB,110L);
        acc.updateMoney(EUR,220L);
        acc.updateMoney(USD,330L);
        assertNotEquals(rub, acc.getMoney().get(RUB));
        assertNotEquals(eur, acc.getMoney().get(EUR));
        assertNotEquals(usd, acc.getMoney().get(USD));

        //откат денег :)
        assertDoesNotThrow(acc::undo);
        assertNotEquals(rub, acc.getMoney().get(RUB));
        assertNotEquals(eur, acc.getMoney().get(EUR));
        assertEquals(usd, acc.getMoney().get(USD));

        assertDoesNotThrow(acc::undo);
        assertDoesNotThrow(acc::undo);

        //удаление денег
        assertDoesNotThrow(acc::undo);
        assertEquals(rub, acc.getMoney().get(RUB));
        assertEquals(eur, acc.getMoney().get(EUR));
        assertNull(acc.getMoney().get(USD));

        assertDoesNotThrow(acc::undo);
        assertDoesNotThrow(acc::undo);
        assertNull(acc.getMoney().get(EUR));
        assertNull(acc.getMoney().get(RUB));

        ///Больше нечего откатывать
        assertThrows(NothingToUndo.class, acc::undo);
    }
    @Test
    void cloneAccount() {
        Account acc = new Account("Default Name");
        acc.updateMoney(RUB,100L);
        acc.updateMoney(USD, 10L);
        Account accClone = (Account) acc.clone();
        assertEquals(acc.getClientName(), accClone.getClientName());

        assertEquals(acc.getMoney().size(), accClone.getMoney().size());
        for (Currency cur : acc.getMoney().keySet()) {
            assertEquals(acc.getMoney().get(cur), accClone.getMoney().get(cur));
        }
    }

    @Test
    void save() {
        Account acc = new Account("Default Name");
        acc.updateMoney(RUB,100L);
        acc.updateMoney(USD, 10L);

        Loadable save_1 = acc.save();

        Account accClone = acc.clone();
        acc.undo();
        acc.setClientName("Vasya-Petya");
        acc.updateMoney(EUR, 98523L);
        save_1.load();

        assertTrue(EqualsBuilder.reflectionEquals(acc,accClone,"undoSteps"));
        assertFalse(EqualsBuilder.reflectionEquals(acc, accClone));

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