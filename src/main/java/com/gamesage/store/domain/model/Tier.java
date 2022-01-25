package com.gamesage.store.domain.model;

import java.io.Serializable;

public class Tier implements Serializable {

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

