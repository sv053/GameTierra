package app.view;

import app.model.*;

import java.util.Random;
import java.util.Scanner;

public class MainPage {
    public static void start() {
        Tier tier = new Tiers().getTiers().get(chooseTierType());
        print(Strings.WELCOME.getMsg() + tier.getLevel() + Strings.CASHBACK_INFO.getMsg() + tier.getCashback() + "%");

        print(Strings.SHOWCASE.getMsg());
        System.out.println(Games.getGames());
        print(Strings.OFFER.getMsg() + (Games.getGames().size()));

        User user = setRandomUser(tier);
        print(Strings.BALANCE.getMsg() + user.getBalance());
        buy(user);
        chooseAnotherGame(user);
    }

    private static User setRandomUser(Tier tier) {
        return new User(tier, RandomDouble.getAndFormatRandomDouble());
    }

    private static Game getChosenGame() {
        Scanner scanner = new Scanner(System.in);
        int choosenGameNumber = 0;
        while (choosenGameNumber < 1 || choosenGameNumber > Games.getGames().size()) {
            print(Strings.INPUT_NUMBER.getMsg());
            try {
                choosenGameNumber = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                e.getStackTrace();
                getChosenGame();
            }
        }
        return Games.getGameByIndex(choosenGameNumber);
    }

    private static boolean tryToBuy(User user, Game game) {
        if (user.ifGameIsAlreadyBought(game)) {
            print(Strings.ALREADY_BOUGHT.getMsg());
        } else {
            if (user.canPay(game.getPrice())) {
                user.pay(game.getPrice());
                user.addGameToTheUserGameList(game);

                print(Strings.CONGRATS.getMsg() + Strings.CASHBACK_INFO.getMsg() +
                        (user.getTier().getCashback() * 0.01d * game.getPrice()));

                print(Strings.BALANCE_WITH_CASHBACK.getMsg() + " $" +
                        user.addCashback(user.getTier().getCashback(), game.getPrice()));
            } else {
                print(Strings.NOT_ENOUGH_MONEY.getMsg());
            }
        }
        chooseAnotherGame(user);
        return true;
    }

    private static void buy(User user) {
        Game choosenGame = getChosenGame();
        print(Strings.CHOICE_IS.getMsg() + choosenGame.getName() + " , $" + choosenGame.getPrice());
        print(Strings.BALANCE.getMsg() + user.getBalance());
        tryToBuy(user, choosenGame);
    }

    private static void chooseAnotherGame(User user) {
        print(Strings.BUY_ANOTHER.getMsg() + Strings.INPUT_Y_N.getMsg());
        Scanner sc = new Scanner(System.in);

        if (sc.nextLine().equalsIgnoreCase("y")) {
            buy(user);
        } else System.exit(0);
    }

    private static int chooseTierType() {
        return new Random().nextInt(5);
    }

    private static void print(String msg) {
        System.out.println(msg);
    }
}
