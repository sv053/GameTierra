package com.gamesage.store.util;

public enum UiPhrases {
    WELCOME("\nHi! Welcome to GameTierra! \nYour subscription type is : "),
    INPUT_NUMBER("Please, enter a valid game number!"),
    CASHBACK_INFO(" and your cashback is "),
    CONGRATS("Congratulations! You have bought the game! "),
    SHOWCASE("\nThe games you can choose are : \n"),
    OFFER("\nPlease, choose game by number from 1 to "),
    BALANCE("Your balance is $"),
    BALANCE_WITH_CASHBACK("\nYour current balance with cashback is $"),
    INPUT_Y_N("Press Y for YES/any other key means NO"),
    BUY_ANOTHER("\nChoose another? "),
    CHOICE_IS("\nYou have chosen : "),
    NOT_ENOUGH_MONEY("\nSorry, your balance is less than the game price. "),
    ALREADY_BOUGHT("You already have this game! Choose another? ");

    public final String phrase;

    UiPhrases(String message) {
        phrase = message;
    }

    public String getPhrase() {
        return phrase;
    }
}

