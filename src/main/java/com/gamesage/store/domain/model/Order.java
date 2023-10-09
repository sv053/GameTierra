package com.gamesage.store.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private final Integer id;
    private final User user;
    private final Game game;
    private final LocalDateTime dateTime;

    public Order(User user, Game game) {
        this(null, user, game, LocalDateTime.now());
    }

    public Order(Integer id, User user, Game game, LocalDateTime dateTime) {
        this.id = id;
        this.user = user;
        this.game = game;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public int getUserId() {
        return user.getId();
    }

    public Game getGame() {
        return game;
    }

    public int getGameId() {
        return game.getId();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return Objects.equals(id, order.id)
            && Objects.equals(user, order.user)
            && Objects.equals(game, order.game)
            && Objects.equals(dateTime, order.dateTime);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (game != null ? game.hashCode() : 0);
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        return result;
    }
}

