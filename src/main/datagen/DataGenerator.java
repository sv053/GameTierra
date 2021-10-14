package main.main.datagen;

import main.main.model.Game;
import main.main.model.Tier;
import main.main.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private static final User randomUser;
    private static List<Game> games = new ArrayList<>();
    private static List<Tier> tiers = new ArrayList<>();

    static {
        games = Arrays.asList(new Game(1, "THE_WITCHER", 17.28),
                new Game(2, "GRAND_THEFT_AUTO", RandomDouble.getAndFormatRandomDouble()),
                new Game(3, "RED_DEAD_REDEMPTION", RandomDouble.getAndFormatRandomDouble()),
                new Game(4, "SKYRIM", 87.88),
                new Game(5, "FORZA_HORIZON", RandomDouble.getAndFormatRandomDouble()),
                new Game(6, "DOOM_ETERNAL", 635.48),
                new Game(7, "ASSASSIN_S_CREED", RandomDouble.getAndFormatRandomDouble()),
                new Game(8, "A_NEW_ONE", RandomDouble.getAndFormatRandomDouble()));

        tiers = Arrays.asList(
                new Tier("FREE", 0),
                new Tier("BRONZE", 5),
                new Tier("SILVER", 10),
                new Tier("GOLD", 20),
                new Tier("PLATINUM", 30));

        randomUser = new User(tiers.get(Math.abs(new Random().nextInt(tiers.size() - 1))), RandomDouble.getAndFormatRandomDouble());
    }

    public static User getRandomUser() {
        return randomUser;
    }

    public static List<Game> getGames() {
        return games;
    }

    public static List<Tier> getTiers() {
        return tiers;
    }
}






