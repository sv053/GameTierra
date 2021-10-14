package main.main.model;

import java.util.Objects;

public class Game {
    public static int gameCode;

    static {
        gameCode = new Integer(1);
    }

    private final Integer gameNumber;
    private final String name;
    private final Double price;

    public Game(int number, String name, Double price) {
        this.name = name;
        this.price = price;
        gameNumber = number;
    }

    public Integer getGameNumber() {
        return gameNumber;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Game{" +
                "code='" + gameNumber + '\'' +
                "name='" + name + '\'' +
                ", price=$" + price +
                '}' + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (!Objects.equals(gameNumber, game.gameNumber)) return false;
        if (!Objects.equals(name, game.name)) return false;
        return Objects.equals(price, game.price);
    }

    @Override
    public int hashCode() {
        int result = gameNumber != null ? gameNumber.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}

