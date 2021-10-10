package app.model;

public interface UserBalance {

    double addCashback(int cashbackPercent, double gamePrice);

    boolean canPay(double price);

    void pay(double price);

}
