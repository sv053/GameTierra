package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ReviewServiceIntegrationTest {

    private final int userId = 8;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;

    private User user;
    private Game game;
    private final int gameId = 7;
    private final int rating = 5;
    private final String description = "wow";
    private final LocalDateTime reviewDateTime = LocalDateTime.of(2002, 3, 26, 6, 53);
    @Autowired
    private ReviewService reviewService;
    private Review review;
    private Review newReview;

    @BeforeEach
    void init() {
        newReview = new Review(1, userId, gameId, rating, description, reviewDateTime);
        User userToCreate = new User(null, "aqua", "marina", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        user = userService.createOne(userToCreate);
        game = gameService.createOne(new Game("gone with the wind", BigDecimal.TEN));
        review = reviewService.createReview(newReview);
    }

    @Test
    void findById_Success() throws Throwable {
//        Review createdReview = reviewService.createReview(n)
        assertEquals(review, reviewService.findById(newReview.getId()));
    }

    @Test
    void findById_Failure() throws Throwable {
        assertEquals(review, reviewService.findById(review.getId()));
    }

    @Test
    void findByGameId_Success() throws Throwable {
        assertEquals(review, reviewService.findById(review.getId()));
    }

    @Test
    void findByGameId_Failure() throws Throwable {
        assertEquals(review, reviewService.findById(review.getId()));
    }

    @Test
    void findByUserId_Success() throws Throwable {
        assertEquals(review, reviewService.findById(review.getId()));
    }

    @Test
    void findByUserId_Failure() throws Throwable {
        assertEquals(review, reviewService.findById(review.getId()));
    }

    @Test
    void findAll_Success() throws Throwable {
        assertEquals(review, reviewService.findById(review.getId()));
    }

    @Test
    void findAll_Failure_EmptyList() {
        LocalDateTime beforeDateTime = LocalDateTime.now();

    }

    @Test
    void findAll_Failure_() throws Throwable {
        assertEquals(review, reviewService.findById(review.getId()));
    }

    @Test
    void createReview_Success() {
    }

    @Test
    void createReview_Failure_WrongUserId_UserDoesNotExist() {
    }

    @Test
    void createReview_Failure_WrongGameId_UserIsNotOwner() {
    }

    @Test
    void createReview_Failure_WrongUserId() {
    }

    @Test
    void updateReview_Success() {
    }

    @Test
    void updateReview_Failure_WrongId_ReviewDoesNotExist() {
    }

    @Test
    void updateReview_Failure_WrongUserId() {
    }

    @Test
    void updateReview_Failure_WrongGameId() {
//
//        assertEquals(expectedPurchase.getTargetGame(), result.getTargetGame());
    }


    @Test
    void buyGame_BuyerIsCorrect_Success() {
    }

    @Test
    void buyGame_PurchaseTimeIsOk_Success() {
    }

    @AfterEach
    void tearDown() {
        userService.deleteAll();
        gameService.deleteAll();
        reviewService.deleteAll();
    }
}

