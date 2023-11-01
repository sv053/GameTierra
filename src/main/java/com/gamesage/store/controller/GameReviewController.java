package com.gamesage.store.controller;

import com.gamesage.store.domain.model.GameReview;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.service.GameReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class GameReviewController {

    private final GameReviewService gameReviewService;

    public GameReviewController(GameReviewService gameReviewService) {
        this.gameReviewService = gameReviewService;
    }

    @GetMapping("/{id}")
    public Review findReviewById(@PathVariable Integer id) throws Throwable {
        return gameReviewService.findById(id);
    }

    @GetMapping("/game/{id}")
    public GameReview findReviewByGameId(@PathVariable Integer id) {
        return gameReviewService.findByGameId(id);
    }

    @GetMapping("/user/{id}")
    public List<Review> findReviewByUserId(@PathVariable Integer id) {
        return gameReviewService.findByUserId(id);
    }

    @GetMapping
    public List<Review> findAllGameReviews() {
        return gameReviewService.findAll();
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return gameReviewService.createReview(review.getGameId(), review.getUserId());
    }
}

