package org.coshift.a_domain.time;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeAccount {
    long id;
    /**
     * Transactions sorted from old to new.
     */
    List<TimeTransaction> transactions = new ArrayList<>(); /* Vielleicht nur drei Jahre retrograd?
    Dann würde man jedes Jahr die Transaktionen älter 3 Jahre weg sichern. */
    TimeBalance balance;
    Logger LOGGER = LoggerFactory.getLogger(TimeAccount.class);

    public TimeAccount(long id, TimeBalance balance){
        this.id = id;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public List<TimeTransaction> getTransactions() {
        return transactions;
    }

    public TimeTransaction getLastTransaction(){
        if(transactions.isEmpty()){
            return null;
        }
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
        if(transactions.isEmpty()){
            LOGGER.warn("transactions is empty. Nothing to remove.");
        } else {
            transactions.removeLast();
        }
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
            for(TimeTransaction transaction : getTransactionsAfter(lastBalance)){
                bal += transaction.getAmountInMinutes();;
            }
            this.balance = new TimeBalance(bal, now);
        }
    }

    private void sortTransactions(){
        transactions.sort(Comparator.comparing(TimeTransaction::getPointInTime));
    }

    @Override
    public String toString() {
        return "TimeAccount{" +
                "id=" + id +
                ", transactions=" + transactions +
                ", balance=" + balance +
                '}';
    }

}
