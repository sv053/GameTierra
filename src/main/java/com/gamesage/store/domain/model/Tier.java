package com.gamesage.store.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tier {

    private final String name;
    private final double cashbackPercentage;
    private final Integer id;

    @JsonCreator
    public Tier(@JsonProperty("id") Integer id,
                @JsonProperty("level") String name,
                @JsonProperty("cashbackPercentage") double cashbackPercentage) {
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

