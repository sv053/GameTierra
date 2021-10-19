package model;

import java.math.BigDecimal;

public class Game {
    public static int gameCode;

    static {
        gameCode = new Integer(1);
    }

    private final Integer id;
    private final String name;
    private final BigDecimal price;

    public Game(String name, BigDecimal price) {
        id = gameCode++;
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
                "code='" + id + '\'' +
                "name='" + name + '\'' +
                ", price=$" + price +
                '}' + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return name.equals(game.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
