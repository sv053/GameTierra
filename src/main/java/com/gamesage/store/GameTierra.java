package com.gamesage.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.setProperty;

@SpringBootApplication
public class GameTierra {

    public static void main(String[] args) {
        setProperty("server.servlet.context-path", "/gamesage/api");
        SpringApplication.run(GameTierra.class);
    }
}

