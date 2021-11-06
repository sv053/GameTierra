package com.gamesage.store.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Game {
    private int id;
    private final String name;
    private final BigDecimal price;

    public Game(String name, BigDecimal price) {
        this(0, name, price);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Game(int id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;

        return id == ((Game) o).id &&
                Objects.equals(name, ((Game) o).name);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", price=$" + price +
                '}' + '\n';
    }
}

