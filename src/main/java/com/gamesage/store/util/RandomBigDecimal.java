package com.gamesage.store.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public final class RandomBigDecimal {

    private RandomBigDecimal() {
    }

    public static BigDecimal getAndFormatRandomBigDecimal() {
        return BigDecimal.valueOf(Math.abs(new Random().nextGaussian() * 100.d))
                .setScale(2, RoundingMode.HALF_UP);
    }
}

