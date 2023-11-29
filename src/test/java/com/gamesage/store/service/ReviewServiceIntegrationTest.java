package com.gamesage.store.service;

import com.gamesage.store.domain.model.*;
import com.gamesage.store.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    private final int rating = 5;
    @Autowired
    private ReviewService reviewService;


    private final String description = "wow";
    private final LocalDateTime reviewDateTime = LocalDateTime.of(2002, 3, 26, 6, 53);
    @Autowired
    private OrderService orderService;
    private User user;
    private Game game;
    private int gameId;
    private int userId;
    private Order order;
    private Review review;
    private Review reviewToCreate;

    @BeforeEach
    void init() {
        User userToCreate = new User(null, "aqua", "marina", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        user = userService.createOne(userToCreate);
        userId = user.getId();
        game = gameService.createOne(new Game("gone with the wind", BigDecimal.TEN));
        gameId = game.getId();
        reviewToCreate = new Review(1, userId, gameId, rating, description, reviewDateTime);
        orderService.buyGame(game.getId(), user.getId());
    }

    @Test
    void findById_Success() throws Throwable {
//        Review createdReview = reviewService.createReview(n)
        review = reviewService.createReview(reviewToCreate);

        assertEquals(review, reviewService.findById(review.getId()));
    }

    @Test
    void findById_Failure_Exception() {
        assertThrows(EntityNotFoundException.class, () -> reviewService.findById(9999999));
    }

    @Test
    void findReviewsByGameId_Success() {
        review = reviewService.createReview(reviewToCreate);

        assertTrue(reviewService.findByGameId(review.getGameId()).contains(review));
    }

    @Test
    void findByGameId_Failure() {
        assertThrows(EntityNotFoundException.class, () -> reviewService.findByGameId(99999999));
    }

    @Test
    void findByUserId_Success() {
        review = reviewService.createReview(reviewToCreate);

        assertEquals(review, reviewService.findByUserId(review.getId()));
    }

    @Test
    void findByUserId_Failure() {
        assertThrows(EntityNotFoundException.class, () -> reviewService.findByUserId(-613620));
    }

    @Test
    void findAll_Success() throws Throwable {
        review = reviewService.createReview(reviewToCreate);

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
        review = reviewService.createReview(reviewToCreate);

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
    void updateReview_Success() throws Throwable {
        review = reviewService.createReview(reviewToCreate);
        review.setOpinion("so so");
        reviewService.updateOrCreateReview(review);

        assertEquals(review.getOpinion(), reviewService.findById(review.getId()).getOpinion());
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
}

