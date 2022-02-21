package com.gamesage.store.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private final Integer id;
    private final User user;
    private final Game game;
    private final LocalDateTime date;

    public Order(Integer id, User user, Game game, LocalDateTime date) {
        this.id = id;
        this.user = user;
        this.game = game;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Game getGame() {
        return game;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if ((!Objects.equals(id, order.id))
            || (!Objects.equals(user, order.user))
                ||(!Objects.equals(game, order.game))) return false;
        return Objects.equals(date, order.date);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (game != null ? game.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}

