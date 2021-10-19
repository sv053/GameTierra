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

    public Game getGameByIndex(Integer gameCode) {
        return games.get(gameCode - 1);
    }

    public BigDecimal addCashback(BigDecimal gamePrice, User user) {
        return gamePrice.multiply(BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01));
    }

    public User buyGame(int gameNumber, User user) {
        Game gameToBuy = getGameByIndex(gameNumber);
        if (user.canPay(gameToBuy.getPrice())) {
            user.setBalance(user.pay(gameToBuy.getPrice()).add(addCashback(gameToBuy.getPrice(), user)));
            user.addToOwnedGames(gameToBuy);
        }
        return user;
    }
}
