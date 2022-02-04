package com.gamesage.store.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {

    private final String login;
    private final Tier tier;
    private final Set<Game> games;
    private Integer id;
    private BigDecimal balance;

    public User(Integer id, String login, Tier tier, BigDecimal balance) {
        this.id = id;
        this.login = login;
        this.tier = tier;
        this.balance = balance;
        games = new HashSet<>();
    }

    public String getLogin() {
        return login;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tier getTier() {
        return tier;
    }

    public Set<Game> getGames() {
        return games;
    }

    public boolean addGame(Game game) {
        return games.add(game);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal depositBalance(BigDecimal amount) {
        balance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        return balance;
    }

    public BigDecimal withdrawBalance(BigDecimal amount) {
        balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        return balance;
    }

    public boolean canPay(BigDecimal price) {
        return price.compareTo(balance) <= 0;
    }

    public boolean hasGame(Game game) {
        return games.contains(game);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return Objects.equals(login, user.login) && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + Integer.hashCode(id);
        return result;
    }

    @Override
    public String toString() {
        return "User{"
                + "login=" + login
                + ", tier=" + tier.getLevel()
                + ", cashback=" + tier.getCashbackPercentage() * 100 + "%}"
                + ", balance=$" + balance
                + ", games=\\n" + getGames()
                + '}';
    }
}

