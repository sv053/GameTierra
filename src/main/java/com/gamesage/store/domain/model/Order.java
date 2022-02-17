package com.gamesage.store.domain.model;

import java.util.Date;
import java.util.Objects;

public class Order {

    private Integer id;
    private User user;
    private Game game;
    private Date date;

    public Order(Integer id, User user, Game game, Date date) {
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

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(user, order.user) && Objects.equals(game, order.game) && Objects.equals(date, order.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, game, date);
    }
}

