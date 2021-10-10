package app.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.SplittableRandom;

public class RandomDouble {
    public static double getAndFormatRandomDouble() {
        return new BigDecimal(Math.abs(new Random().nextGaussian() * 100.d))
                .setScale(2, RoundingMode.UP).doubleValue();
    }

    public static double getSplittableRandom() {
        return Math.fma(new SplittableRandom().nextDouble(), 10.d, 10.d);
    }
}
