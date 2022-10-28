package com.gamesage.store.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class LoginControllerTest {

    @LocalServerPort
    private int serverPort;

//    @Before
//    void initRestAssured() {
//        RestAssured.port = serverPort;
//        RestAssured.filters(new ResponseLoggingFilter());
//        RestAssured.filters(new RequestLoggingFilter());
//    }
//
//    @Test
//    void api_call_without_authentication_must_fail() {
//        when()
//                .get("/")
//                .then()
//                .statusCode(HttpStatus.UNAUTHORIZED);
//    }
}