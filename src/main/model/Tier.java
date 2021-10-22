package model;


public class Tier {
    private final String level;
    private final double cashbackPercentage;

    public Tier(String level, int cashback) {
        this.level = level;
        this.cashbackPercentage = cashback;
    }

    public String getLevel() {
        return level;
    }

    public double getCashbackPercentage() {
        return cashbackPercentage;
    }
}

