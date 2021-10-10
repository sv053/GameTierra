package app.model;

import java.util.Objects;

public class Game {
    public static int gameCode;

    static {
        gameCode = new Integer(1);
    }

    private final Integer gameNumber;
    private String name;
    private String gameDescription;
    private boolean isActive;
    private Double price;

    public Game(int number, String name, String description, boolean isActive, Double price) {
        this.name = name;
        this.gameDescription = description;
        this.isActive = isActive;
        this.price = price;
        gameNumber = number;
    }

    public Integer getGameNumber() {
        return gameNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return gameDescription;
    }

    public void setDescription(String description) {
        this.gameDescription = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Game{" +
                "code='" + gameNumber + '\'' +
                "name='" + name + '\'' +
//                ", description='" + gameDescription + '\'' +
                ", price=$" + price +
                '}' + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (!name.equals(game.name)) return false;
        if (!Objects.equals(gameDescription, game.gameDescription)) return false;
        return gameNumber.equals(game.gameNumber);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (gameDescription != null ? gameDescription.hashCode() : 0);
        result = 31 * result + gameNumber.hashCode();
        return result;
    }
}
