package main.model;

import java.util.ArrayList;

public class Tiers {

    private static ArrayList<Tier> tiers;

    public Tiers() {
        tiers = formTiersList();
    }

    public static ArrayList<Tier> getTiers() {
        return tiers;
    }

    public static ArrayList<Tier> formTiersList() {

        tiers = new ArrayList<>();
        for (TierLevels tierLevel : TierLevels.values()) {
            tiers.add(new Tier(tierLevel.name(), tierLevel.getCashback()));
        }
        return tiers;
    }
}
