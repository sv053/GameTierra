package model;

import utils.SampleData;

import java.math.BigDecimal;
import java.util.List;

public class Store {
    private final List<Game> games;
    private static Store instance;

    private Store() {
        games = SampleData.GAMES;
    }

    public static Store getInstance() {
        if (instance == null)
            instance = new Store();
        return instance;
    }

    public Game getGameById(Integer gameId) {
        return games.stream()
                .filter(g -> g.getId() == gameId)
                .findAny()
                .get();
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        double percentage = user.getTier()
                .getCashbackPercentage();
        BigDecimal percentageShare = BigDecimal.valueOf(percentage * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public User buyGame(int gameId, User user) {
        Game gameToBuy = getGameById(gameId);
        if (user.canPay(gameToBuy.getPrice())) {
            BigDecimal cashback = calculateCashback(gameToBuy.getPrice(), user);
            user.withdrawBalance(gameToBuy.getPrice());
            user.depositBalance(cashback);
            user.addToOwnedGames(gameToBuy);
        }
        return user;
    }
}

