package com.gamesage.service.domain.model;


public class Tier {
    private final String level;
    private final double cashbackPercentage;

    public Tier(String level, double cashback) {
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

