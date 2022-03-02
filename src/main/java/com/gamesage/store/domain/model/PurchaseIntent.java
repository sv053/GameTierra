package com.gamesage.store.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class PurchaseIntent {

    private final boolean isBought;
    private final Game targetGame;
    private final User buyer;
    private final LocalDateTime orderDateTime;
    private final PurchaseMessage purchaseMessage;

    private PurchaseIntent(Builder builder) {
        isBought = builder.gameIsBought;
        targetGame = builder.targetGame;
        purchaseMessage = builder.purchaseMessage;
        buyer = builder.buyer;
        orderDateTime = builder.orderDateTime;
    }

    public boolean isBought() {
        return isBought;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public PurchaseMessage getMessage() {
        return purchaseMessage;
    }

    public Game getTargetGame() {
        return targetGame;
    }

    public User getBuyer() {
        return buyer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PurchaseIntent purchase = (PurchaseIntent) o;

        return Objects.equals(isBought, purchase.isBought())
                && Objects.equals(purchaseMessage, purchase.getMessage())
                && Objects.equals(targetGame, purchase.getTargetGame())
                && Objects.equals(buyer, purchase.getBuyer())
                && Objects.equals(orderDateTime, purchase.getOrderDateTime());
    }

    @Override
    public int hashCode() {
        int result = (isBought ? 1 : 0);
        result = 31 * result + (purchaseMessage != null ? purchaseMessage.hashCode() : 0);
        result = 31 * result + (targetGame != null ? targetGame.hashCode() : 0);
        result = 31 * result + (buyer != null ? buyer.hashCode() : 0);
        result = 31 * result + (orderDateTime != null ? orderDateTime.hashCode() : 0);
        return result;
    }

    public enum PurchaseMessage {

        PURCHASE_SUCCESSFUL("Congrats! You have bought the game!"),
        ALREADY_OWNED("Looks like you already have got this game"),
        NOT_ENOUGH_BALANCE("Sorry, your balance is not enough to buy the game");

        private final String message;

        PurchaseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class Builder {

        private final Game targetGame;
        private User buyer;
        private LocalDateTime orderDateTime;
        private PurchaseMessage purchaseMessage;
        private boolean gameIsBought;

        public Builder(Game targetGame) {
            this.targetGame = targetGame;
        }

        public Builder gameIsBought(boolean isBought) {
            this.gameIsBought = isBought;
            return this;
        }

        public Builder buyer(User buyer) {
            this.buyer = buyer;
            return this;
        }

        public Builder message(PurchaseMessage purchaseMessage) {
            this.purchaseMessage = purchaseMessage;
            return this;
        }

        public Builder orderDateTime(LocalDateTime dateTime) {
            this.orderDateTime = dateTime;
            return this;
        }

        public PurchaseIntent build() {
            return new PurchaseIntent(this);
        }
    }
}

