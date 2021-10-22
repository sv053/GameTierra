package model;

import utils.SampleData;

import java.math.BigDecimal;
import java.util.List;

public class Store {
    private final List<Game> games;
    private static Store instance;

    private Store() {
        games = SampleData.getGames();
    }

    public static Store getInstance() {
        if (instance == null)
            instance = new Store();
        return instance;
    }

    public Game getGameById(Integer gameId) {

        return games.get(gameId);
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        double percentage = user.getTier().getCashbackPercentage();
        BigDecimal percentageShare = BigDecimal.valueOf(percentage * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public User buyGame(int gameNumber, User user) {
        Game gameToBuy = getGameById(gameNumber);
        if (user.canPay(gameToBuy.getPrice())) {
            BigDecimal cashback = calculateCashback(gameToBuy.getPrice(), user);
            user.depositBalance(cashback);
            user.addToOwnedGames(gameToBuy);
        }
        return user;
    }
}

