package model;

import java.math.BigDecimal;

public class Game {
    public static int idCounter;

    private final Integer id;
    private final String name;
    private final BigDecimal price;

    public Game(String name, BigDecimal price) {
        id = idCounter++;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", price=$" + price +
                '}' + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return name.equals(game.getName());
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = 17 * result + (name == "" ? 0 : (name + price).hashCode());
        return result;
    }
}

