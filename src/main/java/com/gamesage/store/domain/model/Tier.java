package com.gamesage.store.domain.model;


public class Tier {

    private final String level;
    private final double cashbackPercentage;

    public Tier(final String level, final double cashbackPercentage) {
        this.level = level;
        this.cashbackPercentage = cashbackPercentage;
    }

    public String getLevel() {
        return this.level;
    }

    public double getCashbackPercentage() {
        return this.cashbackPercentage * .01;
    }
}

