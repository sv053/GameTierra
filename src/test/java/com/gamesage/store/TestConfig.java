package com.gamesage.store;

import com.gamesage.store.service.GameService;
import com.gamesage.store.service.ReviewService;
import com.gamesage.store.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource("classpath:application-test.properties")
public class TestConfig {

    private final ReviewService reviewService;
    private final UserService userService;
    private final GameService gameService;

    public TestConfig(ReviewService reviewService, UserService userService, GameService gameService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.gameService = gameService;
    }

//    @Bean
//    public ReviewService provideReviewService() {
//        return reviewService;
//    }
//
//    @Bean
//    public GameService provideGameService() {
//        return gameService;
//    }
//
//    @Bean
//    public UserService provideUserService() {
//        return userService;
//    }
}

