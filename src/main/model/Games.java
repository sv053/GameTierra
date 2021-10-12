package main.model;

import java.util.ArrayList;
import java.util.List;

public class Games {
    private static final List<Game> games;

    static {
        games = new ArrayList<>();
        createGamesList();
    }

    public static List<Game> getGames() {
        return games;
    }

    public static Game getGame(Game game) {
        return games.stream().filter(g -> g.equals(game)).findFirst().get();
    }

    public static Game getGameByIndex(Integer gameCode) {
        return games.stream().filter(g -> g.getGameNumber().equals(gameCode)).findFirst().get();
    }

    public static void createGamesList() {
        games.clear();
        for (GameNames gameName : GameNames.values()) {
            addGame(gameName.name());
        }
    }

//    public boolean removeGame(Integer gameCode) {
//        return games.remove(games.stream().filter(g -> g.getGameNumber().intValue() == gameCode).findFirst().get());
//    }
//
//    public boolean addGame(Game game) {
//        return games.add(game);
//    }

    public static boolean addGame(String gameName) {
        return games.add(new Game(Game.gameCode++, gameName, "some desc", true,
                RandomDouble.getAndFormatRandomDouble()));
    }

}

