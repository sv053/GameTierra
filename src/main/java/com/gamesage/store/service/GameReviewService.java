package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.GameReview;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameReviewsRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameReviewService {

    private final GameReviewsRepository<Review, Integer, GameReview> repository;
    private final UserService userService;
    private final GameService gameService;

    public GameReviewService(GameReviewsRepository<Review, Integer, GameReview> repository,
                             UserService userService, GameService gameService) {
        this.repository = repository;
        this.userService = userService;
        this.gameService = gameService;
    }

    public Review findById(int id) throws Throwable {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public GameReview findByGameId(int id) {
        return repository.findByGameId(id);
    }

    public List<Review> findByUserId(int id) {
        return repository.findByUserId(id);
    }

    public List<Review> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Review createReview(int gameId, int userId) {
        Game game = gameService.findById(gameId);
        User user = userService.findById(userId);
        boolean hasGame = user.hasGame(game);

        return null;
    }

    private Review prepareReview(boolean canPay, boolean hasGame) {

        return null;
    }

    private boolean checkIfHasGame(Integer userId, Integer gameId) {
        return userService.findById(userId).getGames().contains(gameService.findById(gameId));
    }

    public Review saveReview() {

        return null;
    }

    public Review updateReview() {

        return null;
    }

}

