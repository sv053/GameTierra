package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.db.DbUserRepository;
import com.gamesage.store.security.model.VerificationToken;
import com.gamesage.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder encoder;
    private final DbUserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AuthService(UserDetailsService userDetailsService, BCryptPasswordEncoder encoder, DbUserRepository userRepository, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public boolean checkIfUserExists(String login, String pass) {
        String storedPass = userDetailsService.loadUserByUsername(login).getPassword();
        return encoder.matches(pass, storedPass);
    }

    private VerificationToken provideWithToken(int userId) {
        Optional<VerificationToken> token =
                userRepository.findToken(userId).or(() -> Optional.of(userRepository.createToken(userId)));
        return token.get();
    }

    public User provideCheckedUserWithToken(String login) {
        User user = userService.findByLogin(login);
        userService.updateToken(user.getId());
        user.setToken(provideWithToken(user.getId()).getToken());
        LOGGER.info(user.toString());
        return user;
    }
}

