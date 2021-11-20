package com.gamesage.store.domain.model;

import org.springframework.stereotype.Component;

@Component
public class Tier {

    private String level;
    private double cashbackPercentage;

    public Tier() {
    }

    public Tier(String level, double cashbackPercentage) {
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

