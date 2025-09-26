package com.gamesage.store.controller;

import com.gamesage.store.domain.data.SampleData;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.OrderService;
import com.gamesage.store.service.ReviewService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String POST_REVIEW_ENDPOINT = "/reviews";
    private static final String REVIEW_ID_ENDPOINT = "/reviews/{id}";
    private static final String REVIEW_GAMEID_ENDPOINT = "/reviews/games/{gameid}/{page}/{size}";
    private static final String REVIEW_USERID_ENDPOINT = "/reviews/users/{userid}/{page}/{size}";
    private static final String WRONG_TOKEN_HEADER = "unknownTokenValue";
    private static final String TOKEN_HEADER_TITLE = "X-Auth-Token";

    private final String description = "not bad";
    private final LocalDateTime reviewDateTime = LocalDateTime.of(2002, 3, 26, 6, 53);

    private String userJson;
    private User user;
    private Game game;
    private List<Game> savedGames;
    private String token;
    @Autowired
    OrderService orderService;
    private Review reviewToCreate;
    @Autowired
    private ReviewService reviewService;

    @BeforeAll
    void setup() throws Exception {
        userJson = Files.readString(Path.of(userJsonResource.getURI()));
        User userToSave = objectMapper.readValue(userJson, User.class);
        user = userService.createOne(userToSave);
        token = loginAndGetToken(userJson);
        savedGames = gameService.createAll(SampleData.GAMES.subList(0, 2));
        game = savedGames.get(0);
        reviewToCreate = new Review(1, user.getId(), game.getId(), true, "cool", LocalDateTime.now());
    }

    @Test
    void findReviewById_givenRightCreds_Success() throws Throwable {
        orderService.buyGame(game.getId(), user.getId());
        Review savedReview = reviewService.createReview(reviewToCreate);

        mockMvc.perform(get(REVIEW_ID_ENDPOINT, savedReview.getId())
                        .header(TOKEN_HEADER_TITLE, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedReview.getId()))
                .andExpect(jsonPath("$.user_id").value(savedReview.getUserId()))
                .andExpect(jsonPath("$.game_id").value(savedReview.getGameId()))
                .andExpect(jsonPath("$.opinion").value(reviewToCreate.getOpinion()));
    }

    @Test
    void findReviewById_givenWrongToken_Failure() throws Exception {
        orderService.buyGame(game.getId(), user.getId());
        Review savedReview = reviewService.createReview(reviewToCreate);

        mockMvc.perform(get(REVIEW_ID_ENDPOINT, savedReview.getId(), 1, 1)
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findReviewById_givenUnknownUser_Failure() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(REVIEW_ID_ENDPOINT, wrongId, 1, 10)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllReviewsByGameId_givenRightCreds_Success() throws Exception {
        User userForSecondReview = SampleData.USERS.get(2);
        User savedSecondUser = userService.createOne(userForSecondReview);
        orderService.buyGame(game.getId(), user.getId());
        orderService.buyGame(game.getId(), savedSecondUser.getId());
        Review review = reviewService.createReview(reviewToCreate);
        Review secondReview = reviewService.createReview(new Review(
                112,
                savedSecondUser.getId(),
                game.getId(),
                false,
                description,
                reviewDateTime));
        double avgRating = review.getRating() ? 1 : 0;
        avgRating += secondReview.getRating() ? 1 : 0;
        avgRating /= 2;

        mockMvc.perform(get(REVIEW_GAMEID_ENDPOINT, game.getId(), 1, 2)
                        .header(TOKEN_HEADER_TITLE, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviews").isArray())
                .andExpect(jsonPath("$.reviews.*.game_id", containsInAnyOrder(
                        review.getGameId(), secondReview.getGameId())))
                .andExpect(jsonPath("$.reviews.*.user_id", containsInAnyOrder(
                        review.getUserId(), secondReview.getUserId())))
                .andExpect(jsonPath("$.reviews.*.opinion", containsInAnyOrder(
                        review.getOpinion(), secondReview.getOpinion())))
                .andExpect(jsonPath("$.avgRating").value(avgRating));
    }

    @Test
    void findAllReviewsByUserId_givenRightCreds_Success() throws Exception {
        Game secondGame = savedGames.get(1);
        orderService.buyGame(game.getId(), user.getId());
        orderService.buyGame(secondGame.getId(), user.getId());
        Review review = reviewService.createReview(reviewToCreate);
        Review secondReview = reviewService.createReview(new Review(
                112,
                user.getId(),
                secondGame.getId(),
                false,
                description,
                reviewDateTime));

        mockMvc.perform(get(REVIEW_USERID_ENDPOINT, user.getId(), 1, 2)
                        .header(TOKEN_HEADER_TITLE, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.*.game_id", containsInAnyOrder(
                        game.getId(), secondGame.getId())))
                .andExpect(jsonPath("$.*.user_id", containsInAnyOrder(
                        review.getUserId(), secondReview.getUserId())))
                .andExpect(jsonPath("$.*.opinion", containsInAnyOrder(
                        review.getOpinion(), secondReview.getOpinion())));
    }

    @Test
    void findReviewsByUserId_givenRightCreds_WringUserId_Exception() throws Exception {
        mockMvc.perform(get(REVIEW_USERID_ENDPOINT, 888, 1, 2)
                        .header(TOKEN_HEADER_TITLE, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void writeReview_givenRightCredsAndHasGame_Success() throws Exception {
        orderService.buyGame(game.getId(), user.getId());

        mockMvc.perform(post(POST_REVIEW_ENDPOINT)
                        .header(TOKEN_HEADER_TITLE, token)
                        .content(objectMapper.writeValueAsString(reviewToCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(user.getId()))
                .andExpect(jsonPath("$.game_id").value(game.getId()))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.rating").value(reviewToCreate.getRating()))
                .andExpect(jsonPath("$.opinion").value(reviewToCreate.getOpinion()));
    }

    @Test
    void updateReview_givenRightCredsAndHasGame_Success() throws Exception {
        orderService.buyGame(game.getId(), user.getId());
        Review reviewToUpdate = reviewService.createReview(reviewToCreate);
        boolean newRating = false;
        String newOpinion = "too good to go";

        mockMvc.perform(put(REVIEW_ID_ENDPOINT, reviewToUpdate.getId())
                        .header(TOKEN_HEADER_TITLE, token)
                        .content(objectMapper.writeValueAsString(
                                new Review(
                                        reviewToUpdate.getId(),
                                        user.getId(),
                                        game.getId(),
                                        newRating,
                                        newOpinion,
                                        reviewToUpdate.getDateTime())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(user.getId()))
                .andExpect(jsonPath("$.game_id").value(game.getId()))
                .andExpect(jsonPath("$.rating").value(newRating))
                .andExpect(jsonPath("$.opinion").value(newOpinion));
    }

    @Test
    void writeReview_givenWrongCreds_Failure() throws Exception {
        mockMvc.perform(post(REVIEW_ID_ENDPOINT, game.getId(), user.getId())
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void writeReview_hasNoGame_Failure() throws Exception {
        mockMvc.perform(post(POST_REVIEW_ENDPOINT)
                        .header(TOKEN_HEADER_TITLE, token)
                        .content(objectMapper.writeValueAsString(reviewToCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

