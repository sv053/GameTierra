package com.gamesage.store.controller;

import com.gamesage.store.domain.model.Review;
import com.gamesage.store.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{id}")
    public Review findReviewById(@PathVariable Integer id) throws Throwable {
        return reviewService.findById(id);
    }

    @GetMapping("/game/{id}")
    public List<Review> findReviewByGameId(@PathVariable Integer id) {
        return reviewService.findByGameId(id);
    }

    @GetMapping("/user/{id}")
    public List<Review> findReviewByUserId(@PathVariable Integer id) {
        return reviewService.findByUserId(id);
    }

    @GetMapping
    public List<Review> findAllReviews() {
        return reviewService.findAll();
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.updateOrCreateReview(review);
    }

    @PostMapping("/{id}")
    public Review updateReview(@RequestBody Review review) {
        return reviewService.updateOrCreateReview(review);
    }


}

