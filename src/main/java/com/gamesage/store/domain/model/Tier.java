package com.gamesage.store.domain.model;

public class Tier {

    private final String name;
    private final double cashbackPercentage;
    private Integer id;

    public Tier(Integer id, String name, double cashbackPercentage) {

        this.id = id;
        this.name = name;
        this.cashbackPercentage = cashbackPercentage;
    }

    public Integer getId() {

        return id;
    }

    public String getLevel() {
        return name;
    }

    public double getCashbackPercentage() {
        return cashbackPercentage * .01;
    }
}

