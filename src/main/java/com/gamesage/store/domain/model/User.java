package com.gamesage.store.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class User {

    private final String login;
    private final Tier tier;
    private final Set<Game> games;
    private Integer id;
    private BigDecimal balance;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public User(Integer id, String login, Tier tier, BigDecimal balance) {
        this.id = id;
        this.login = login;
        this.tier = tier;
        this.balance = balance;
        games = new HashSet<>();
    }

    public User(@JsonProperty("id") Integer id,
                @JsonProperty("login") String login,
                @JsonProperty("password") String password,
                @JsonProperty("tier") Tier tier,
                @JsonProperty("balance") BigDecimal balance) {
        this(id, login, tier, balance);
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void addGames(List<Game> games) {
        this.games.addAll(games);
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
        return "\nUser{"
                + "id=" + id
                + ", login=" + login
                + ", tier=" + tier.getLevel()
                + ", cashback=" + tier.getCashbackPercentage() * 100 + "%}"
                + ", balance=$" + balance
                + ", \ngames=\n" + getGames()
                + '}';
    }
}

