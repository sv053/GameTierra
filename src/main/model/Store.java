package model;

import utility.SampleData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Store {
    private static Store instance;
    private List<Game> games;
    private Map<Integer, Game> gamesForSearch;

    private Store() {
        getAllGames();
    }

    public Game searchGame(int id) {
        getGamesForSearch();
        return gamesForSearch.get(id);
    }

    public static Store getInstance() {
        if (instance == null)
            instance = new Store();
        return instance;
    }

    public List<Game> getAllGames() {
        return games = SampleData.GAMES;
    }

    private Map<Integer, Game> getGamesForSearch() {
        return gamesForSearch = games.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (oldValue)));
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        double percentage = user.getTier().getCashbackPercentage();
        BigDecimal percentageShare = BigDecimal.valueOf(percentage * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public boolean buyGame(int gameId, User user) {
        getGamesForSearch();
        Game gameToBuy = null;
        try {
            gameToBuy = gamesForSearch.get(gameId);
        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (user.canPay(gameToBuy.getPrice()) && !user.hasGame(gameToBuy)) {
            BigDecimal cashback = calculateCashback(gameToBuy.getPrice(), user);
            user.withdrawBalance(gameToBuy.getPrice());
            user.depositBalance(cashback);
            user.addGame(gameToBuy);
            return true;
        }
        return false;
    }
}

