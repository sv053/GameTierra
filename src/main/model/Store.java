package main.main.model;

import main.main.datagen.DataGenerator;
import main.main.datagen.Strings;

import java.util.List;

public class Store {
    private final User user;
    private final List<Game> games;

    public Store(User user) {
        // user = DataGenerator.getRandomUser();
        this.user = user;
        games = DataGenerator.getGames();
    }

    public User getUser() {
        return user;
    }

    public Game getGameByIndex(Integer gameCode) {
        return games.stream().filter(g -> g.getGameNumber().equals(gameCode)).findFirst().get();
    }

    public String buyGame(int gameNumber) {
        Game gameToBuy = getGameByIndex(gameNumber);
        if (user.canPay(gameToBuy.getPrice())) {
            user.pay(gameToBuy.getPrice());
            user.addGameToTheUserGamesList(gameToBuy);
            user.addCashback(gameToBuy.getPrice());
            return Strings.CONGRATS.getMsg();
        }
        return Strings.NOT_ENOUGH_MONEY.getMsg();
    }

    public String checkIfHasGame(Game game) {
        if (user.ifGameIsAlreadyBought(game))
            return Strings.ALREADY_BOUGHT.getMsg();
        return "";
    }

}
