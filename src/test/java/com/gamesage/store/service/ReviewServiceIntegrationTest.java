package com.gamesage.store.service;

import com.gamesage.store.domain.data.SampleData;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.GameReview;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.CannotCreateEntityException;
import com.gamesage.store.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class ReviewServiceIntegrationTest {

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

    @BeforeEach
    void init() {
        User userToCreate = SampleData.USERS.get(1);
        user = userService.createOne(userToCreate);
        userId = user.getId();
        game = gameService.createOne(SampleData.GAMES.get(1));
        gameId = game.getId();
        reviewToCreate = new Review(1, userId, gameId, true, description, reviewDateTime);
        orderService.buyGame(game.getId(), user.getId());
    }

    @Test
    void findById_Success() throws Throwable {
        review = reviewService.createReview(reviewToCreate);

        assertEquals(reviewToCreate.getOpinion(), reviewService.findById(review.getId()).getOpinion());
    }

    @Test
    void findById_Failure_Exception() {
        assertThrows(EntityNotFoundException.class, () -> reviewService.findById(9999999));
    }

    @Test
    void findReviewsByGameId_Success() {
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
        Review secondSavedReview = reviewService.createReview(secondReview);

        GameReview gameReview = reviewService.findByGameId(game.getId(), 1, 10);
        assertTrue(gameReview.getReviews().contains(review));
        assertTrue(reviewService.findByGameId(review.getGameId(), 1, 10).getReviews().contains(secondSavedReview));
    }

    @Test
    void findByUserId_Success() {
        Review review = reviewService.createReview(reviewToCreate);
        List<Review> foundReviews = reviewService.findByUserId(review.getUserId(), 1, 10);

        assertTrue(foundReviews.contains(review));
    }

    @Test
    void findByUserId_Failure() {
        assertThrows(EntityNotFoundException.class, () -> reviewService.findByUserId(-8888888, 1, 1));
    }

    @Test
    void createReview_HasAuthHasGame_Success() {
        review = reviewService.createReview(reviewToCreate);

        assertEquals(reviewToCreate.getOpinion(), review.getOpinion());
    }

    @Test
    void createReview_WrongUserId_Failure() {
        assertThrows(EntityNotFoundException.class, () ->
                reviewService.createReview(new Review(
                        9876,
                        9876,
                        gameId,
                        rating,
                        description,
                        reviewDateTime)));
    }

    @Test
    void createReview_WrongGameId_Failure() {
        assertThrows(EntityNotFoundException.class, () -> reviewService.findByUserId(-613620, 1, 1));
    }

    @Test
    void createReview_NotAnOwner_Failure() {
        int noOwnerGameId = 7878787;
        assertThrows(CannotCreateEntityException.class, () ->
                reviewService.createReview(new Review(
                        98,
                        userId,
                        noOwnerGameId,
                        rating,
                        description,
                        reviewDateTime)));
    }

    @Test
    void updateReviewOpinion_Success() throws Throwable {
        String changedOpinion = "so so";
        review = reviewService.createReview(reviewToCreate);
        review.setOpinion(changedOpinion);
        reviewService.updateReview(review);

        assertEquals(changedOpinion, reviewService.findById(review.getId()).getOpinion());
    }

    @Test
    void updateReview_WrongGameId_Failure() throws Throwable {
        Review reviewToUpdate = reviewService.createReview(reviewToCreate);
        int noOwnerGameId = 7878787;
        String newOpinion = "wow";
        Review updatingReview = new Review(
                reviewToUpdate.getId(),
                reviewToUpdate.getUserId(),
                noOwnerGameId,
                reviewToUpdate.getRating(),
                newOpinion,
                reviewToUpdate.getDateTime());
        assertThrows(EntityNotFoundException.class, () ->
                reviewService.updateReview(updatingReview));
        assertEquals(reviewToCreate.getOpinion(), reviewService.findById(reviewToUpdate.getId()).getOpinion());
    }
}

