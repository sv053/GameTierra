package com.gamesage.store.domain.data.sample;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.util.RandomBigDecimal;

import java.math.BigDecimal;
import java.util.List;

public final class SampleData {

    public static final List<Game> GAMES;
    public static final List<Tier> TIERS;
    public static final List<User> USERS;

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
                new Tier("BRONZE", 5.d),
                new Tier("SILVER", 10.d),
                new Tier("GOLD", 20.d),
                new Tier("PLATINUM", 30.d));

        USERS = List.of(
                new User(1, "terral", SampleData.TIERS.get(3), BigDecimal.valueOf(100)),
                new User(2, "tierra", SampleData.TIERS.get(2), BigDecimal.valueOf(777)),
                new User(3, "turron", SampleData.TIERS.get(1), BigDecimal.valueOf(888))
        );
    }

    private SampleData() {
    }
}

