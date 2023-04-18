package com.gamesage.store.controller;

import static java.nio.file.Path.of;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.GameService;
import com.gamesage.store.service.OrderService;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderControllerIntegrationTest {

    private static final String API_LOGIN_ENDPOINT = "/login";
    private static final String API_ORDER_ENDPOINT = "/cart";
    private static final String ORDER_ID_ENDPOINT = "/cart/{id}";
    private static final String ORDER_BUY_ENDPOINT = "/cart/{gameId}/{userId}";
    private static final String WRONG_TOKEN_HEADER = "unknownTokenValue";
    private static final String TOKEN_HEADER_TITLE = "X-Auth-Token";
    @Value("classpath:request/user/test.json")
    private Resource userJsonResource;

    private final Game game = new Game("THE_LAST_OF_US", BigDecimal.valueOf(7.28d));
    private final Game gameOneMore = new Game("THE_LAST_OF_THEM", BigDecimal.valueOf(7.28d));

    private String userJson;
    private String token;
    private User savedUser;
    private Game savedGame;

    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @BeforeAll
    void setup() throws Exception {
        userJson = Files.readString(of(userJsonResource.getURI()));
        User user = objectMapper.readValue(userJson, User.class);
        userService.deleteAll();
        savedUser = userService.createOne(user);
        token = loginAndGetToken();
    }

    @Test
    void givenWrongCreds_shouldNotFindOrderByIdAndReturn401() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(ORDER_ID_ENDPOINT, wrongId)
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenUnknownUser_shouldNotFindOrderByIdAndReturn403() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(ORDER_ID_ENDPOINT, wrongId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenRightCreds_shouldFindAllOrders() throws Exception {
        List<Game> savedGames = gameService.createAll(List.of(game, gameOneMore));
        orderService.buyGame(savedGames.get(0).getId(), savedUser.getId());
        orderService.buyGame(savedGames.get(1).getId(), savedUser.getId());
        int ordersAmount = orderService.findAll().size();

        mockMvc.perform(get(API_ORDER_ENDPOINT)
                        .header(TOKEN_HEADER_TITLE, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*.game.name", containsInAnyOrder(
                        savedGames.get(0).getName(), savedGames.get(1).getName())))
                .andExpect(jsonPath("$.[%d].user.login", ordersAmount - 1).value(savedUser.getLogin()));
    }

    @Test
    void givenRightCreds_shouldFindOrderById() throws Exception {
        savedGame = gameService.createOne(game);
        orderService.buyGame(savedGame.getId(), savedUser.getId());

        Order order = orderService.findAll().get(0);

        mockMvc.perform(get(ORDER_ID_ENDPOINT, order.getId())
                        .header(TOKEN_HEADER_TITLE, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.login").value(savedUser.getLogin()))
                .andExpect(jsonPath("$.user.id").value(order.getUser().getId()))
                .andExpect(jsonPath("$.game.name").value(savedGame.getName()))
                .andExpect(jsonPath("$.game.id").value(savedGame.getId()));
    }

    @Test
    void givenRightCreds_shouldBuyGame() throws Exception {
        savedGame = gameService.createOne(game);

        mockMvc.perform(post(ORDER_BUY_ENDPOINT, savedGame.getId(), savedUser.getId())
                        .header(TOKEN_HEADER_TITLE, token)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bought").value(true))
                .andExpect(jsonPath("$.buyer.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.buyer.login").value(savedUser.getLogin()))
                .andExpect(jsonPath("$.targetGame.name").value(savedGame.getName()));
    }

    @Test
    void givenWrongCreds_shouldNotBuyGame() throws Exception {
        mockMvc.perform(post(ORDER_BUY_ENDPOINT, savedGame.getId(), savedUser.getId())
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private String loginAndGetToken() throws Exception {
        return mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(TOKEN_HEADER_TITLE);
    }
}

