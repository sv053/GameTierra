package com.gamesage.store.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;


public class PurchaseIntent {

    private final boolean isBought;
    private final Game targetGame;
    private final User buyer;
    private final LocalDateTime orderDateTime;
    private final Message message;

    private PurchaseIntent(Builder builder) {
        isBought = builder.gameIsBought;
        targetGame = builder.targetGame;
        message = builder.message;
        buyer = builder.buyer;
        orderDateTime = builder.orderDateTime;
    }

    public boolean isBought() {
        return isBought;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public Message getMessage() {
        return message;
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
                && Objects.equals(message, purchase.getMessage())
                && Objects.equals(targetGame, purchase.getTargetGame())
                && Objects.equals(buyer, purchase.getBuyer())
                && Objects.equals(orderDateTime, purchase.getOrderDateTime());
    }

    @Override
    public int hashCode() {
        int result = (isBought ? 1 : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (targetGame != null ? targetGame.hashCode() : 0);
        result = 31 * result + (buyer != null ? buyer.hashCode() : 0);
        result = 31 * result + (orderDateTime != null ? orderDateTime.hashCode() : 0);
        return result;
    }

    public enum Message {
        PLAY_NOW,
        IS_ALREADY_OWNED,
        THE_GAME_PRICE_IS_HIGHER_THAN_THE_BALANCE
    }

    public static class Builder {

        private final Game targetGame;
        private User buyer;
        private LocalDateTime orderDateTime;
        private Message message;
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

        public Builder message(Message message) {
            this.message = message;
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

