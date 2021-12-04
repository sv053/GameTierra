package com.gamesage.store.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tier {

    private final String level;
    private final double cashbackPercentage;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Tier(@JsonProperty("level") String level,
                @JsonProperty("cashbackPercentage") double cashbackPercentage) {
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

