package com.gamesage.store.domain.model;

import java.util.Objects;

public class PurchaseIntent {

    private boolean status;
    private String message;
    private Game targetGame;
    private User buyer;
    private Order order;

    public boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Game getTargetGame() {
        return targetGame;
    }

    public User getBuyer() {
        return buyer;
    }

    public Order getOrder() {
        return order;
    }

    public PurchaseIntent status(boolean purchaseStatus) {
        status = purchaseStatus;
        return this;
    }

    public PurchaseIntent targetGame(Game targetGame) {
        this.targetGame = targetGame;
        return this;
    }

    public PurchaseIntent message(String message) {
        this.message = message;
        return this;
    }

    public PurchaseIntent buyer(User buyer) {
        this.buyer = buyer;
        return this;
    }

    public PurchaseIntent order(Order order) {
        this.order = order;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchaseIntent purchase = (PurchaseIntent) o;

        return Objects.equals(status, purchase.getStatus())
                && Objects.equals(message, purchase.getMessage())
                && Objects.equals(targetGame, purchase.getTargetGame())
                && Objects.equals(buyer, purchase.getBuyer())
                && Objects.equals(order, purchase.getOrder());
    }

    @Override
    public int hashCode() {
        int result = (status ? 1 : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (targetGame != null ? targetGame.hashCode() : 0);
        result = 31 * result + (buyer != null ? buyer.hashCode() : 0);
        result = 31 * result + (order != null ? order.hashCode() : 0);
        return result;
    }
}

