package com.gamesage.store.util;

public enum ReviewRating {
    ZERO(0),
    ONE(1);

    private final int value;

    ReviewRating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
