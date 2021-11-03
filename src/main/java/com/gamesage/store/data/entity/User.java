package com.gamesage.store.data.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class User {
    private final Set<Integer> gameIds;
    private int id;
    private final Tier tier;
    private BigDecimal balance;

    public User(Tier tier, BigDecimal balance) {
        this.tier = tier;
        this.balance = balance;
        gameIds = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tier getTier() {
        return tier;
    }

    public Set<Integer> getGames() {
        return gameIds;
    }

    public boolean addGame(int id) {
        return gameIds.add(id);
    }

    @Override
    public String toString() {
        return "User{" +
                " tier=" + tier.getLevel() +
                ", cashback=" + tier.getCashbackPercentage() + "%" +
                ", balance=$" + balance +
                ", gameIds=\\n" + getGames() +
                '}';
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal updateBalance(BigDecimal restOfBalance) {
        balance = restOfBalance.plus();
        return balance;
    }
}

