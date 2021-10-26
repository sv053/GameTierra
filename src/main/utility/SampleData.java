package utility;

import model.Game;
import model.Tier;

import java.math.BigDecimal;
import java.util.List;

public final class SampleData {

    public static final List<Game> GAMES;
    public static final List<Tier> TIERS;

    static {
        GAMES = List.of(
                new Game("THE_WITCHER", BigDecimal.valueOf(17.28d)),
                new Game("GRAND_THEFT_AUTO", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game("RED_DEAD_REDEMPTION", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game("SKYRIM", BigDecimal.valueOf(87.88d)),
                new Game("FORZA_HORIZON", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game("DOOM_ETERNAL", BigDecimal.valueOf(635.48d)),
                new Game("ASSASSIN_S_CREED", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game("A_NEW_ONE", RandomBigDecimal.getAndFormatRandomBigDecimal()));

        TIERS = List.of(
                new Tier("FREE", 0.d),
                new Tier("BRONZE", 5.0d),
                new Tier("SILVER", 10.),
                new Tier("GOLD", 20.),
                new Tier("PLATINUM", 30.));
    }

    private SampleData() {
    }
}

