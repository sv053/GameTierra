package com.gamesage.store.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Game {

    private Integer id;
    private final String name;
    private final BigDecimal price;

    public Game(String name, BigDecimal price) {
        this(null, name, price);
    }

    public Game(Integer id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;

        final Game game = (Game) o;
        return Objects.equals(this.id, game.id) &&
                Objects.equals(this.name, game.name);
    }

    @Override
    public int hashCode() {
        int result = this.id.hashCode();
        result = 31 * result + this.name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + this.id + '\'' +
                "name='" + this.name + '\'' +
                ", price=$" + this.price +
                '}' + '\n';
    }
}

