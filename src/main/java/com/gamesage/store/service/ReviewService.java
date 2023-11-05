package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.ReviewRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository<Review, Integer> repository;
    private final UserService userService;
    private final GameService gameService;

    public ReviewService(ReviewRepository<Review, Integer> repository,
                         UserService userService, GameService gameService) {
        this.repository = repository;
        this.userService = userService;
        this.gameService = gameService;
    }

    public Review findById(int id) throws Throwable {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<Review> findByUserId(int id) {
        return repository.findByUserId(id);
    }

    public List<Review> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Review createReview(Review review) {
        Game game = gameService.findById(review.getGameId());
        User user = userService.findById(review.getUserId());
        if (user.hasGame(game)) {
            repository.createOne(review);
        }

        return null;
    }

    public Review updateReview(Review review) {
        return repository.createOne(review);
    }
}

