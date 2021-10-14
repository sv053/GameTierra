package main.main.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final List<Game> userGames;
    private final Tier tier;
    private Double balance;

    public User(Tier tier, Double balance) {
        this.tier = tier;
        this.balance = balance;
        userGames = new ArrayList<>();
    }

    public List<Game> getUserGames() {
        return userGames;
    }

    public boolean ifGameIsAlreadyBought(Game game) {
        return userGames.stream().anyMatch(g -> g.getName().equals(game.getName()));
    }

    public boolean addGameToTheUserGamesList(Game game) {
        return userGames.add(game);
    }

    public double addCashback(double gamePrice) {
        return balance + gamePrice * this.tier.getCashback() * 0.01d;
    }

    @Override
    public String toString() {
        return "User{" +
                "tier=" + tier.getLevel() +
                "cashback=" + tier.getCashback() + "%" +
                "balance=" + balance +
                '}';
    }

    public Double getBalance() {
        return balance;
    }

    public boolean canPay(double price) {
        return price <= balance;
    }

    public void pay(double price) {
        balance -= price;
    }
}




