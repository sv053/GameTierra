package com.gamesage.store.data.entity;

import com.gamesage.store.data.repository.SampleData;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void addGame() {
        User user = new User(SampleData.TIERS.get(1), BigDecimal.valueOf(87.5));
        int gameId = 4;

        assertTrue(user.addGame(gameId));
        assertTrue(user.getGames().contains(gameId));
    }
}

