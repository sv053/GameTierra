package main.test;

import main.main.datagen.DataGenerator;
import main.main.model.Tier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TierTest {

    private static List<Tier> tiers;

    @BeforeAll
    public static void TiersListPrepare() {
        tiers = DataGenerator.getTiers();
    }

    @Test
    void getLevel() {
        assertEquals("FREE", tiers.get(0).getLevel());
        assertEquals("BRONZE", tiers.get(1).getLevel());
        assertEquals("SILVER", tiers.get(2).getLevel());
        assertEquals("GOLD", tiers.get(3).getLevel());
        assertEquals("PLATINUM", tiers.get(4).getLevel());
    }

    @Test
    void getCashback() {
        assertEquals(0, tiers.get(0).getCashback());
        assertEquals(5, tiers.get(1).getCashback());
        assertEquals(10, tiers.get(2).getCashback());
        assertEquals(20, tiers.get(3).getCashback());
        assertEquals(30, tiers.get(4).getCashback());
    }
}

