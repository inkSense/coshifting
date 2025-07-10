package org.coshift.a_domain.time;

import org.coshift.a_domain.person.Person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimeAccount {
    Long id;
    /**
     * Transactions sorted from old to new.
     */
    List<TimeTransaction> transactions = new ArrayList<>(); /* Vielleicht nur drei Jahre retrograd?
    Dann würde man jedes Jahr die Transaktionen älter 3 Jahre weg sichern. */
    TimeBalance balance;

    public TimeAccount(Long id){
        this.id = id;
        this.balance = new TimeBalance(0L, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public List<TimeTransaction> getTransactions() {
        return transactions;
    }

    public TimeTransaction getLastTransaction(){
        return transactions.getLast();
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

    public List<TimeTransaction> getTransactionsFromTo(LocalDateTime firstPointInTime, LocalDateTime secondPointInTime){
        if(firstPointInTime.isBefore(secondPointInTime)){
            return transactions.stream().filter(t->t.getPointInTime().isAfter(firstPointInTime) && t.getPointInTime().isBefore(secondPointInTime)).toList();
        } else {
            return transactions.stream().filter(t->t.getPointInTime().isAfter(secondPointInTime) && t.getPointInTime().isBefore(firstPointInTime)).toList();
        }
    }

    public void addTransaction(TimeTransaction delta){
        transactions.add(delta);
        sortTransactions();
    }

    public void removeLastTransaction(){
        transactions.removeLast();
    }

    public void removeTransaction(LocalDateTime pointInTime){
        transactions.removeIf(t->t.getPointInTime().equals(pointInTime));
    }

    public TimeBalance getBalance() {
        return balance;
    }

    public TimeBalance getCurrentBalance(LocalDateTime now){
        refreshBalance(now);
        return balance;
    }

    public void refreshBalance(LocalDateTime now){
        long bal = balance.getAmountInMinutes();
        LocalDateTime lastBalance = balance.getPointInTime();
        if(now.isAfter(lastBalance) ){
            for(TimeTransaction transaction : getTransactionsAfter(now)){
                bal += transaction.getAmountInMinutes();
            }
            this.balance = new TimeBalance(bal, now);
        }
    }

    private void sortTransactions(){
        transactions.sort(Comparator.comparing(TimeTransaction::getPointInTime));
    }


}
