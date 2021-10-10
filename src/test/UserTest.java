package test;

import app.model.Game;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private List<Game> createUserGamesList(){
        return Arrays.asList(new Game(1, "gameName1", "desc1", false, 35.11),
                new Game(2, "gameName2", "desc2", true, 1118.17),
                new Game(3, "gameName3", "desc3", true, 26.02));
    }
    @Test
    void ifGameIsAlreadyBought() {
        Game game = new Game(2,"gameName2", "", true, 0.0d);
        assertEquals(true, createUserGamesList().stream().anyMatch(g -> g.getName().equals(game.getName()))); ;
    }
    //?
    @org.junit.jupiter.api.Test
    void sendCashback() {
        double gamePrice = 0.23546546546;
        double balance = 0.235465465461;
        int cashbackPercent = 30;
        assertEquals(0.306105105099, balance + gamePrice * cashbackPercent * 0.01d);
    }
    //?
    @org.junit.jupiter.api.Test
    void ifCanPay() {
        double price = 0.23546546546;
        double balance = 0.235465465461;
        assertEquals(true, price <= balance);
        balance = 0.23546546545;
        assertEquals(false, price <= balance);
    }
    //?
    @org.junit.jupiter.api.Test
    void pay() {
        double price = 0.23546546546;
        double balance = 0.235465465461;
        double sum = balance - price;
        assertEquals(sum, balance -= price);
    }
}