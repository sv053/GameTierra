package com.gamesage.store.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {

    private final Set<Game> games;
    private final String login;
    private Integer id;
    private final Tier tier;
    private BigDecimal balance;

    public User(final Integer id, final String login, Tier tier, BigDecimal balance) {
        this.id = id;
        this.login = login;
        this.tier = tier;
        this.balance = balance;
        this.games = new HashSet<>();
    }

    public String getLogin() {
        return this.login;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tier getTier() {
        return this.tier;
    }

    public Set<Game> getGames() {
        return this.games;
    }

    public boolean addGame(Game game) {
        return this.games.add(game);
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public BigDecimal depositBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        return this.balance;
    }

    public BigDecimal withdrawBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        return this.balance;
    }

    public boolean canPay(BigDecimal price) {
        return price.compareTo(this.balance) <= 0;
    }

    public boolean hasGame(Game game) {
        return this.games.contains(game);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!Objects.equals(this.login, user.login)) return false;
        return this.id != null ? this.id.equals(user.id) : user.id == null;
    }

    @Override
    public int hashCode() {
        int result = this.login != null ? this.login.hashCode() : 0;
        result = 31 * result + Integer.hashCode(this.id);
        return result;
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
}

