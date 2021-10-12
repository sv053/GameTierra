package main.model;

public enum TierLevels {
    FREE(0),
    BRONZE(5),
    SILVER(10),
    GOLD(20),
    PLATINUM(30);

    public final int Cashback;

    TierLevels(int cashback) {
        Cashback = cashback;
    }

    public int getCashback() {
        return Cashback;
    }
}
