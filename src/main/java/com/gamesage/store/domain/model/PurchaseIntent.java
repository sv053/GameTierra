package com.gamesage.store.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;


public class PurchaseIntent {

    private final boolean isBought;
    private final Game targetGame;
    private final User buyer;
    private final LocalDateTime orderDateTime;
    private final String message;

    private PurchaseIntent(Builder builder) {
        isBought = builder.gameIsBought;
        targetGame = builder.targetGame;
        message = builder.message.getPhrase();
        buyer = builder.buyer;
        orderDateTime = builder.orderDateTime;
    }

    public boolean isBought() {
        return isBought;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
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
        PURCHASE_SUCCESSFUL("Congrats! You have bought the game!"),
        IS_ALREADY_OWNED("Looks like you already have got this game"),
        NOT_ENOUGH_BALANCE("Sorry, your balance is not enough to buy the game");

        public final String phrase;

        Message(String message) {
            phrase = message;
        }

        public String getPhrase() {
            return phrase;
        }
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

