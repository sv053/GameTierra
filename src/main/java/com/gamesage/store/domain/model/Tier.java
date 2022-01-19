package com.gamesage.store.domain.model;

public class Tier {

    private Integer id;
    private final String name;
    private final double cashbackPercentage;

    public Tier(Integer id, String name, double cashbackPercentage) {
        this.id = id;
        this.name = name;
        this.cashbackPercentage = cashbackPercentage;
    }

    public String getLevel() {
        return name;
    }

    public double getCashbackPercentage() {
        return cashbackPercentage * .01;
    }
}

