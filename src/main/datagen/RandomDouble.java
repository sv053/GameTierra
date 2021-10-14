package main.main.datagen;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class RandomDouble {
    public static double getAndFormatRandomDouble() {
        return new BigDecimal(Math.abs(new Random().nextGaussian() * 100.d))
                .setScale(2, RoundingMode.UP).doubleValue();
    }
}



