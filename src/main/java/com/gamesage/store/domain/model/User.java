package com.gamesage.store.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
    private final Set<Game> games;
    private final String login;
    private int id;
    private final Tier tier;
    private BigDecimal balance;

    public User(String login, final Tier tier, final BigDecimal balance) {
        this.login = login;
        this.tier = tier;
        this.balance = balance;
        this.games = new HashSet<>();
    }

    public String getLogin() {
        return this.login;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Tier getTier() {
        return this.tier;
    }

    public Set<Game> getGames() {
        return this.games;
    }

    public boolean addGame(final Game game) {
        return this.games.add(game);
    }

    @Override
    public String toString() {
        return "User{" +
                " tier=" + this.tier.getLevel() +
                ", cashback=" + this.tier.getCashbackPercentage() * 100 + "%" +
                ", balance=$" + this.balance +
                ", games=\\n" + this.getGames() +
                '}';
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public BigDecimal depositBalance(final BigDecimal amount) {
        this.balance = this.balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        return this.balance;
    }

    public BigDecimal withdrawBalance(final BigDecimal amount) {
        this.balance = this.balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        return this.balance;
    }

    public boolean canPay(final BigDecimal price) {
        return price.compareTo(this.balance) <= 0;
    }

    public boolean hasGame(final Game game) {
        return this.games.contains(game);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        return Objects.equals(this.login, ((User) o).login);
    }

    @Override
    public int hashCode() {
        return this.login != null ? 31 * this.login.hashCode() : 0;
    }
}

