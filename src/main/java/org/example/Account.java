package org.example;

import lombok.Getter;
import lombok.NonNull;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
public class Account implements Cloneable{


    @Getter @NonNull
    private String clientName;
    private HashMap<Currency, Long> money;
    private Deque<Executable> undoSteps = new ArrayDeque<>();

    public Account undo(){
        if(undoSteps.isEmpty()) throw new NothingToUndo("Nothing for undo");
        undoSteps.pop().exec();
        return this;
    }
    public void updateMoney(Currency cur, Long value) {
        if(value == null || value < 0) throw new IllegalArgumentException("Сумма должны быть больше 0");

        /*undo steps*/
        //если уже есть то фиксируте откат к тек значению и сохраняю новое
        this.money.computeIfPresent( cur, (k,v) ->{
            //делаю сохранение для отката
            Long oldVal = this.money.get(cur);
            this.undoSteps.push(()->this.money.put(cur, oldVal));
            return value;
        });
        //Если нету то запонить удаление и новое значение сохранить
        this.money.computeIfAbsent(cur, (k) -> {
            //надо что-то запомнить для удаления
            this.undoSteps.push(() -> this.money.remove(cur));
            return value;
        });
    }

    public void setClientName(String clientName) {
        checkClientName(clientName);
        String oldName = this.clientName;
        this.undoSteps.push(() -> this.clientName = oldName);
        this.clientName = clientName;
    }

    public HashMap<Currency, Long> getMoney() {
        return new HashMap<>(money);
    }

    private Deque<Executable> getUndoSteps() {
        return new ArrayDeque<>(this.undoSteps);
    }

    private void checkClientName(@NonNull String clientName) {
        if(StringUtils.isBlank(clientName)) {
            throw new IllegalArgumentException("Field clientName can't be blank/empty/null");
        }
    }
    public Account(String clientName) {
        this.checkClientName(clientName);
        this.clientName = clientName;
        this.money = new HashMap<>();

    }

    public Loadable save() {
        return new Snapshot();
    }

    @Override
    //только в тестировании сохранялки используется, но путсть будет
    public Account clone() {
        Account acc = new Account(this.clientName);
        acc.money = this.getMoney();
        acc.undoSteps =this.getUndoSteps();
        return acc;
    }

    private class Snapshot implements Loadable{

        private final String clientName;
        private final HashMap<Currency,Long> money;

        public Snapshot (){
            this.clientName = Account.this.clientName;
            this.money = new HashMap<>(Account.this.money);
        }


        @Override
        public void load() {
            Account.this.clientName = this.clientName;
            Account.this.money = new HashMap<>(this.money);
        }
    }
    @Override
    public String toString() {
        StringBuilder retval = new StringBuilder("Account{" +
                "clientName='" + clientName + '\''
                + ", money :  \n");
        for (Currency key: money.keySet()) {
            retval.append("\t")
                    .append(key.displayName + " = ")
                    .append(money.get(key).toString())
                    .append("\n");
        }

        retval.append("\n }");
        return retval.toString();
    }
}
