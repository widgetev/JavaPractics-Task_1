package org.example;

public enum Currency {
    RUB, USD, EUR;

    /** В рамках задачи это лищнее, но использование java.util.Currency
     * повозилт  манипулировать общепрнятыми обзначениями, а не придумывать свои
     * + типа приложение не ограничивается рамками задачи.
     */
     String a3Code;
     String displayName;
     String numCode;
     String symbol;

    Currency(){
        java.util.Currency cur = java.util.Currency.getInstance(this.name());
        this.a3Code = cur.getCurrencyCode();
        this.numCode = cur.getNumericCodeAsString();
        this.displayName = cur.getDisplayName();
        this.symbol = cur.getSymbol();
    }
}
