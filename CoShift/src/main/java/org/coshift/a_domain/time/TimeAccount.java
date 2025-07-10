package org.coshift.a_domain.time;

import org.coshift.a_domain.person.Person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TimeAccount {
    Person owner;
    List<TimeTransaction> transactions = new ArrayList<>(); // Sortiert nach Zeit. Vielleicht nur zwei Jahre retrograd?
    TimeBalance balance = new TimeBalance(0L, null);

    public TimeAccount(Person owner) {
        this.owner = owner;
    }

    public void addTransaction(TimeTransaction delta){
        transactions.add(delta);
    }

    public void removeTransaction(int index){
        transactions.remove(index);
    }

    public void removeTransaction(LocalDateTime pointInTime){
        transactions.removeIf(t->t.getPointInTime().equals(pointInTime));
    }

    public TimeBalance getBalance() {
        return balance;
    }

    public TimeBalance getCurrentBalance(LocalDateTime now){
        long bal = balance.getAmountInMinutes();
        LocalDateTime lastBalance = balance.getPointInTime();

        if(now.isAfter(lastBalance) ){
            for(TimeTransaction transaction : getTransactionsAfter(now)){
                bal += transaction.getAmountInMinutes();
            }
            this.balance = new TimeBalance(bal, now);
        }
        return balance;
    }

    public List<TimeTransaction> getTransactionsAfter(LocalDateTime pointInTime){
        return transactions
                .stream()
                .filter(t->t.getPointInTime().isAfter(pointInTime))
                .toList();
    }

    public List<TimeTransaction> getTransactionsBefore(LocalDateTime pointInTime){
        return transactions
                .stream()
                .filter(t->t.getPointInTime().isBefore(pointInTime))
                .toList();
    }

    List<TimeTransaction> getTransactionsFromTo(LocalDateTime firstPointInTime, LocalDateTime secondPointInTime){
        if(firstPointInTime.isBefore(secondPointInTime)){
            return transactions.stream().filter(t->t.getPointInTime().isAfter(firstPointInTime) && t.getPointInTime().isBefore(secondPointInTime)).toList();
        } else {
            return transactions.stream().filter(t->t.getPointInTime().isAfter(secondPointInTime) && t.getPointInTime().isBefore(firstPointInTime)).toList();
        }
    }


}
