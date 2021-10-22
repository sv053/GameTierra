package utils;

import model.Game;
import model.Tier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class SampleData {

    private static final List<Game> games;
    private static final List<Tier> tiers;

    static {
        games = Collections.unmodifiableList(
                Arrays.asList(new Game("THE_WITCHER", BigDecimal.valueOf(17.28d)),
                        new Game("GRAND_THEFT_AUTO", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                        new Game("RED_DEAD_REDEMPTION", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                        new Game("SKYRIM", BigDecimal.valueOf(87.88d)),
                        new Game("FORZA_HORIZON", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                        new Game("DOOM_ETERNAL", BigDecimal.valueOf(635.48d)),
                        new Game("ASSASSIN_S_CREED", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                        new Game("A_NEW_ONE", RandomBigDecimal.getAndFormatRandomBigDecimal())));

        tiers = Arrays.asList(
                new Tier("FREE", 0),
                new Tier("BRONZE", 5),
                new Tier("SILVER", 10),
                new Tier("GOLD", 20),
                new Tier("PLATINUM", 30));
    }

    private SampleData() {
    }

    public static List<Game> getGames() {
        return games;
    }

    public static List<Tier> getTiers() {
        return tiers;
    }
}

