package games.age.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class User {
    private final Set<Game> games;
    private final Tier tier;
    private BigDecimal balance;

    public User(Tier tier, BigDecimal balance) {
        this.tier = tier;
        this.balance = balance;
        games = new HashSet<>();
    }

    public Tier getTier() {
        return tier;
    }

    public Set<Game> getGames() {
        return games;
    }

    public boolean hasGame(Game game) {
        return games.contains(game);
    }

    public boolean addGame(Game game) {
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
        return balance;
    }

    public BigDecimal depositBalance(BigDecimal amount) {
        balance = balance.add(amount).plus().setScale(2, RoundingMode.HALF_UP);
        return balance;
    }

    public BigDecimal withdrawBalance(BigDecimal amount) {
        balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        return balance;
    }

    public boolean canPay(BigDecimal price) {
        return price.compareTo(balance) <= 0;
    }
}

