package com.gamesage.store.service;

import com.gamesage.store.domain.data.SampleData;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.GameReview;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class GameRatingServiceIntegrationTest {

    private final boolean rating = true;
    private final String description = "on a need-to-know basis";
    private final LocalDateTime reviewDateTime = LocalDateTime.of(2002, 3, 26, 6, 53);
    private User user;
    private Game game;
    private Review review;
    private Review reviewToCreate;
    private int userId;
    private int gameId;

    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GameRatingService ratingService;

    @BeforeEach
    void init() {
        User userToCreate = SampleData.USERS.get(1);
        user = userService.createOne(userToCreate);
        userId = user.getId();
        game = gameService.createOne(SampleData.GAMES.get(1));
        gameId = game.getId();
        reviewToCreate = new Review(1, userId, gameId, false, description, reviewDateTime);
        orderService.buyGame(game.getId(), user.getId());
    }

    @Test
    void findRating_Success() {
        review = reviewService.createReview(reviewToCreate);
        int ratingsSum = ratingService.findRating(review.getGameId());
        int ratingsAmount = ratingService.findRatingsAmount(review.getGameId());

        assertEquals(0, ratingsSum);
        assertEquals(1, ratingsAmount);
    }

    @Test
    void findRatingsAmount_Success() {
        User userForSecondReview = SampleData.USERS.get(2);
        User secondSavedUser = userService.createOne(userForSecondReview);
        orderService.buyGame(game.getId(), secondSavedUser.getId());
        review = reviewService.createReview(reviewToCreate);

        Review secondReview = reviewService.createReview(new Review(
                98,
                secondSavedUser.getId(),
                game.getId(),
                rating,
                description,
                reviewDateTime));

        GameReview gameReview = reviewService.findByGameId(game.getId(), 1, 10);

        assertEquals(2, ratingService.findRatingsAmount(game.getId()));
    }

    @Test
    void updateRating_Success() throws Throwable {
        review = reviewService.createReview(reviewToCreate);

        assertEquals(0, ratingService.findRating(gameId));

        Review review = reviewService.updateReview(reviewToCreate);
        Review updatedReview = reviewService.updateReview(new Review(
                review.getId(),
                review.getUserId(),
                review.getGameId(),
                rating,
                description,
                reviewDateTime));

        assertEquals(1, ratingService.findRating(gameId));
    }
}

