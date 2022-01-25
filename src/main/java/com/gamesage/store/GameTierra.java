package com.gamesage.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameTierra {

    public static final Logger logger = LoggerFactory.getLogger(GameTierra.class);

    public static void main(String[] args) {
        SpringApplication.run(GameTierra.class);
    }
}

