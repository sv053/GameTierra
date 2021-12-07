package com.gamesage.store.domain.model;

public class Tier {

    private final String level;
    private final double cashbackPercentage;

    public Tier(String level,
                double cashbackPercentage) {
        this.level = level;
        this.cashbackPercentage = cashbackPercentage;
    }

    public String getLevel() {
        return level;
    }

    public double getCashbackPercentage() {
        return cashbackPercentage * .01;
    }
}

