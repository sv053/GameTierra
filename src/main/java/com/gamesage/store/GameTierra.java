package com.gamesage.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GameTierra {

    private static final Logger logger = LoggerFactory.getLogger(GameTierra.class);

    public static void main(String[] args) {

        SpringApplication.run(GameTierra.class);

        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.setPort(8888);
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    webServerFactoryCustomizer() {

        String rootContext = new StringBuilder()
                .append("/")
                .append(GameTierra.logger.getName().split("\\.")[1])
                .append("/api")
                .toString();
        return factory -> factory.setContextPath(rootContext);
    }
}

