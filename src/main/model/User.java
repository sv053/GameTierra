package main.model;

import java.util.ArrayList;
import java.util.List;

public class User implements UserBalance {
    private final List<Game> userGames;
    Tier tier;
    private Double balance;

    public User(Tier tier, Double balance) {
        this.tier = tier;
        this.balance = balance;
        userGames = new ArrayList<>();
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public boolean ifGameIsAlreadyBought(Game game) {
        return userGames.stream().anyMatch(g -> g.getName().equals(game.getName()));
    }

    public void addGameToTheUserGameList(Game game) {
        userGames.add(game);
    }

    @Override
    public double addCashback(int cashbackPercent, double gamePrice) {
        return balance + gamePrice * cashbackPercent * 0.01d;
    }

    @Override
    public String toString() {
        return "User{" +
                "tier=" + tier.getLevel() +
                ", balance=" + balance +
                '}';
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addMoney(double moneyToAdd) {
        balance += moneyToAdd;
    }

    @Override
    public boolean canPay(double price) {
        return price <= balance;
    }

    @Override
    public void pay(double price) {
        balance -= price;
    }
}
