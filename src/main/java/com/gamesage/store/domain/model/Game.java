package com.gamesage.store.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class Game {

    private final String name;
    private final BigDecimal price;
    private Integer id;

    public Game(String name, BigDecimal price) {
        this(null, name, price);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Game(@JsonProperty("id") Integer id,
                @JsonProperty("name") String name,
                @JsonProperty("price") BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(name, game.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Game{" + "id='" + id
                + '\'' + ", name='" + name
                + '\'' + ", price=$" + price
                + '}' + '\n';
    }
}

