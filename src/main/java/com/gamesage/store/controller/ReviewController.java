package com.gamesage.store.controller;

import com.gamesage.store.domain.model.GameReview;
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

    @GetMapping("/games/{id}/{page}/{size}")
    public GameReview findReviewByGameId(@PathVariable Integer id,
                                         @PathVariable int page,
                                         @PathVariable int size) {
        return reviewService.prepareGameReview(id, page, size);
    }

    @GetMapping("/users/{id}/{page}/{size}")
    public List<Review> findReviewByUserId(@PathVariable Integer id,
                                           @PathVariable int page,
                                           @PathVariable int size) {
        return reviewService.findByUserId(id, page, size);
    }

    @GetMapping("/page/size/{page}/{size}")
    public List<Review> findAllReviews(@PathVariable int page,
                                       @PathVariable int size) {
        return reviewService.findAll(page, size);
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) throws Throwable {
        return reviewService.updateOrCreateReview(review);
    }

    @PostMapping("/{id}")
    public Review updateReview(@RequestBody Review review) throws Throwable {
        return reviewService.updateOrCreateReview(review);
    }
}

