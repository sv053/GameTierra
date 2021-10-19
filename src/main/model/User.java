package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class User {
    private final List<Game> games;
    private final Tier tier;
    private BigDecimal balance;

    public User(Tier tier, BigDecimal balance) {
        this.tier = tier;
        this.balance = balance;
        games = new ArrayList<>();
    }

    public Tier getTier() {
        return tier;
    }

    public BigDecimal addBalance(BigDecimal balance) {
        return this.balance.add(balance);
    }

    public List<Game> getGames() {
        return games;
    }

    public boolean hasGame(Game game) {
        return games.stream()
                .map(Game::getName)
                .anyMatch(name -> name.equals(game.getName()));
    }

    public boolean addToOwnedGames(Game game) {
        return games.add(game);
    }

    @Override
    public String toString() {
        return "User{" +
                " tier=" + tier.getLevel() +
                ", cashback=" + tier.getCashbackPercentage() + "%" +
                ", balance=$" + balance +
                ", games=\\n" + getGames() +
                '}';
    }

    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.UP);
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean canPay(BigDecimal price) {
        return price.compareTo(balance) <= 0.01;
    }

    public BigDecimal pay(BigDecimal price) {
        setBalance(getBalance().subtract(price));
        return getBalance();
    }
}
