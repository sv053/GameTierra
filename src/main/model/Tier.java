package main.main.model;


public class Tier {
    private final String level;
    private final int cashback;

    public Tier(String level, int cashback) {
        this.level = level;
        this.cashback = cashback;
    }

    public String getLevel() {
        return level;
    }

    public Integer getCashback() {
        return cashback;
    }
}

