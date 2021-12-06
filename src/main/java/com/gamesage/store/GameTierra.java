package com.gamesage.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GameTierra {
    private static final Logger logger = LoggerFactory.getLogger(GameTierra.class);

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(GameTierra.class, args);
    }
}

