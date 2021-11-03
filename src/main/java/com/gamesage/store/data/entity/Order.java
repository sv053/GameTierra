package com.gamesage.store.data.entity;

import java.util.Date;

public class Order {
    private final Date date;
    private final int userId;
    private final int gameId;

    public Order(Date date, int userId, int gameId) {
        this.date = date;
        this.userId = userId;
        this.gameId = gameId;
    }

    public Date getDate() {
        return date;
    }

    public int getUserId() {
        return userId;
    }

    public int getGameId() {
        return gameId;
    }
}
