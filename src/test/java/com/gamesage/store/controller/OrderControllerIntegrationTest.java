package com.gamesage.store.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.GameService;
import com.gamesage.store.service.OrderService;
import com.gamesage.store.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private static final String USER_JSON_FILE_PATH = "src/main/resources/user.txt";

    private final Tier tier = new Tier(5, "PLATINUM", 30.0);
    private final User user = new User(null, "admin", "letmein", tier,
            BigDecimal.valueOf(1000));
    private final Game game = new Game("THE_LAST_OF_US", BigDecimal.valueOf(7.28d));

    private String USER_JSON;
    private String TOKEN;

    private User savedUser;
    private Game savedGame;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setup() throws Exception {

        savedUser = userService.createOne(user);
        savedGame = gameService.createOne(game);

        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(USER_JSON_FILE_PATH));
        String reading = "";
        while ((reading = bufferedReader.readLine()) != null) {
            USER_JSON += reading;
        }
        TOKEN = loginAndGetToken();
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
        orderService.buyGame(savedGame.getId(), savedUser.getId());

        mockMvc.perform(get(API_ORDER_ENDPOINT)
                        .header(TOKEN_HEADER_TITLE, "TOKEN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*.user.login", Matchers.containsInAnyOrder(savedUser.getLogin())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*.game.name", Matchers.containsInAnyOrder(savedGame.getName())));
    }

    @Test
    void givenRightCreds_shouldFindOrderById() throws Exception {
        orderService.buyGame(savedGame.getId(), savedUser.getId());

        Order order = orderService.findAll().get(0);

        mockMvc.perform(get(ORDER_ID_ENDPOINT, order.getId())
                        .header(TOKEN_HEADER_TITLE, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.login").value(savedUser.getLogin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(order.getUser().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.game.name").value(savedGame.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.game.id").value(savedGame.getId()));
    }

    @Test
    void givenRightCreds_shouldBuyGame() throws Exception {
        mockMvc.perform(post(ORDER_BUY_ENDPOINT, savedGame.getId(), savedUser.getId())
                        .header(TOKEN_HEADER_TITLE, TOKEN)
                        .content(USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bought").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.id").value(savedUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.buyer.login").value(savedUser.getLogin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetGame.name").value(savedGame.getName()));

    }

    @Test
    void givenWrongCreds_shouldNotBuyGame() throws Exception {
        mockMvc.perform(post(ORDER_BUY_ENDPOINT, savedGame.getId(), savedUser.getId())
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .content(USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private String loginAndGetToken() throws Exception {
        return mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .content(USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(TOKEN_HEADER_TITLE);
    }
}

