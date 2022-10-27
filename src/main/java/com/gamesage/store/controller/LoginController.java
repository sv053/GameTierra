package com.gamesage.store.controller;

import com.gamesage.store.security.config.auth.AuthProvider;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final AuthService authService;
    private final AuthProvider ap;
    private final UserService userService;

    public LoginController(AuthService authService, AuthProvider ap, UserService userService) {
        this.authService = authService;
        this.ap = ap;
        this.userService = userService;
    }

//    @PostMapping
//    public ResponseEntity<User> login(HttpServletRequest request, HttpServletResponse response) {
//        User existingUser = userService.findByLogin(user.getLogin());
//       // if (authService.checkIfUserExists(user.getLogin(), user.getPassword())) {
//
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            logger.error("currentUser " + authentication.getPrincipal().toString());
//
//            return
//            return ResponseEntity
//                    .ok()
//                    .header("x-auth-token", authService.provideNewToken("admin").getValue())
//                    .body(user);
//    //    }
//   //     return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    }

    @PostMapping
    public String login() {
        // User existingUser = userService.findByLogin(user.getLogin());
        // if (authService.checkIfUserExists(user.getLogin(), user.getPassword())) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.error("currentUser " + authentication.getPrincipal().toString());

        return "foijoaifj";

        //    }
        //     return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/postw")
    public Authentication login1() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication;
    }

    @GetMapping("/getw")
    public String login2() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return "authentication";
    }

//    @PostMapping("/")
//    public AuthToken login(@RequestBody User user) {
//        AuthToken authToken;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authService.checkIfUserExists(user.getLogin(), user.getPassword())) {
//
//            existingUser = authService.provideTokenForCheckedUser(user.getLogin());
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            logger.error("currentUser " + authentication.getPrincipal().toString());
//
//            return new ResponseEntity<>(authToken, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    }

    @GetMapping
    public String showAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ap.authenticate(authentication).getCredentials().toString();
    }
}

