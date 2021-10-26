package model;

import utils.SampleData;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Store {
    //    private Map<Integer, List<Game>> games;
    private Map<Integer, Game> games;
    private static Store instance;

    private Store() {
        games = new HashMap<>();
    }

    public void setGames() {
        this.games = SampleData.GAMES.stream()
                .distinct()
                .collect(
                        //                 Collectors.groupingBy(Game::getId));
                        Collectors.toMap(Game::getId, g -> g,
                                (oldValue, newValue) -> (oldValue)));
    }

    public Map<Integer, Game> getGames() {
        return games;
    }

    public static Store getInstance() {
        if (instance == null)
            instance = new Store();
        return instance;
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        double percentage = user.getTier().getCashbackPercentage();
        BigDecimal percentageShare = BigDecimal.valueOf(percentage * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public User buyGame(int gameId, User user) {
        setGames();
        if (!games.containsKey(gameId)) return user;
        Game gameToBuy = games.get(gameId);

        if (user.canPay(gameToBuy.getPrice()) && !user.hasGame(gameToBuy)) {
            BigDecimal cashback = calculateCashback(gameToBuy.getPrice(), user);
            user.withdrawBalance(gameToBuy.getPrice());
            user.depositBalance(cashback);
            user.addGame(gameToBuy);
        }
        return user;
    }
}

