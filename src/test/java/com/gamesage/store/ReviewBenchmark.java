package com.gamesage.store;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Review;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.GameService;
import com.gamesage.store.service.OrderService;
import com.gamesage.store.service.ReviewService;
import com.gamesage.store.service.UserService;
import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class ReviewBenchmark {

    private final LocalDateTime reviewDateTime = LocalDateTime.of(2002, 3, 26, 6, 53);
    private final List<Double> allM100 = new ArrayList<>();
    private final List<Double> allM500 = new ArrayList<>();
    private final List<Double> allM1000 = new ArrayList<>();
    private List<Game> games;
    private ReviewService reviewService;
    private ConfigurableApplicationContext context;

    @Setup(Level.Trial)
    public void setup() {
        context = SpringApplication.run(GameTierra.class);
        UserService userService = context.getBean(UserService.class);
        GameService gameService = context.getBean(GameService.class);
        reviewService = context.getBean(ReviewService.class);
        OrderService orderService = context.getBean(OrderService.class);
        int usersAmount = 1001;
        List<User> users = userService.findAll();
        int usersAmountToAdd = Objects.nonNull(users) ? usersAmount - users.size() : usersAmount;
        if (usersAmountToAdd > 0) {
            for (int i = 0; i < usersAmountToAdd; i++) {
                String generatedString = createRandomString();
                User user = new User(i, generatedString, generatedString, new Tier(
                        3, "silver", 10.d), BigDecimal.valueOf(12340000));
                User savedUser = userService.createOne(user);
                users.add(savedUser);
                System.out.println("users " + i + " : " + user);
            }
        }
        for (User user : users) {
            userService.updateBalance(new User(user.getId(), user.getLogin(), user.getPassword(), user.getTier(), BigDecimal.valueOf(100000000)));
        }

        int gamesAmount = 1;
        games = gameService.findAll();
        int gamesAmountToAdd = gamesAmount - games.size();
        if (gamesAmountToAdd > 0) {
            for (int i = 0; i < gamesAmountToAdd; i++) {
                String generatedString = createRandomString();
                Game game = gameService.createOne(new Game(generatedString, BigDecimal.ONE));
                games.add(game);
                System.out.println("games " + i + " : " + game);
            }
        }

        for (int i = 0; i < usersAmount; i++) {
            var order = orderService.buyGame(games.get(0).getId(), users.get(i).getId());
            System.out.println("order- games_id " + games.get(0).getId() + " user_id " + users.get(i).getId() + " : " + order);
            String description = "some desc";
            var review = reviewService.createReview(new Review(1, users.get(i).getId(), games.get(0).getId(), true, description, reviewDateTime));
            System.out.println("review- games_id " + games.get(0).getId() + " user_id " + users.get(i).getId() + " : " + review);
        }

        timeReviews(100, allM100);
        timeReviews(500, allM500);
        timeReviews(1000, allM1000);

        System.out.println("min100 = " + allM100.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElseThrow());
        System.out.println("avg100 = " + allM100.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow());
        System.out.println("max100 = " + allM100.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElseThrow());

        System.out.println("min500 = " + allM500.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElseThrow());
        System.out.println("avg500 = " + allM500.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow());
        System.out.println("max500 = " + allM500.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElseThrow());

        System.out.println("min1000 = " + allM1000.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElseThrow());
        System.out.println("avg1000 = " + allM1000.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElseThrow());
        System.out.println("max1000 = " + allM1000.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElseThrow());
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 0)
    @Timeout(time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 1)
    public void measure200Reviews() {
        findByGameId(games.get(0).getId(), 200);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 0)
    @Timeout(time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 1)
    public void measure500Reviews() {
        findByGameId(games.get(0).getId(), 500);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 0)
    @Timeout(time = 100, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 1)
    public void measure1000Reviews() {
        findByGameId(games.get(0).getId(), 1000);
    }

    public void findByGameId(int gameId, int size) {
        reviewService.findByGameId(gameId, 1, size);
    }

    private String createRandomString() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


    private void timeReviews(int reviewsNumber, List<Double> results) {
        for (int i = 0; i < 5; i++) {
            System.out.println(reviewsNumber + " reviews - ");
            long currentTimeMillis = System.currentTimeMillis();
            findByGameId(games.get(0).getId(), reviewsNumber);
            double timingResult = (double) (System.currentTimeMillis() - currentTimeMillis);
            System.out.println(timingResult);
            results.add(timingResult);
        }
    }

    @TearDown
    public void tearDown() {
        SpringApplication.exit(context);
    }
}

