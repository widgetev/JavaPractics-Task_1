package org.example;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigInteger;
import java.util.*;
import static org.example.Currency.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class Account {
    @Getter @NonNull
    private String clientName;

    private HashMap<Currency, Long> money;

    private void updateClientName(@NonNull String clientName) {
        if(StringUtils.isBlank(clientName)) {
            throw new IllegalArgumentException("Field clientName can't be blank/empty/null");
        }
        this.clientName = clientName;
    }

    public Account(String clientName) {
        this.updateClientName(clientName);
        this.money = new HashMap<>();

    }


    public void updateMoney(Currency cur, Long value) {
        if(value == null || value < 0) throw new IllegalArgumentException("Сумма должны быть больше 0");
        this.money.put(cur,value);
    }

    public void setClientName(String clientName) {
       updateClientName(clientName);
    }

    public HashMap<Currency, Long> getMoney() {
        return new HashMap<Currency, Long>(money);
    }

}
