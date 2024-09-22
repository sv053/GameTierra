package com.gamesage.store.controller;

import java.util.List;

import com.gamesage.store.domain.model.GameReview;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.service.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/games/{id}")
    public GameReview findReviewByGameId(@PathVariable Integer id,
                                                                    @RequestParam(defaultValue = "0") Integer page,
                                                                    @RequestParam(defaultValue = "10") Integer size) {
        return reviewService.findByGameId(id, page, size);
    }

    @GetMapping("/users/{id}")
    public List<Review> findReviewByUserId(@PathVariable Integer id,
                                                                    @RequestParam(defaultValue = "0") Integer page,
                                                                    @RequestParam(defaultValue = "10") Integer size) {
        return reviewService.findByUserId(id, page, size);
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) throws Throwable {
        return reviewService.createReview(review);
    }

    @PutMapping("/{id}")
    public Review updateReview(@RequestBody Review review) throws Throwable {
        return reviewService.updateReview(review);
    }
}

