package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.db.DbTokenRepository;
import com.gamesage.store.domain.repository.db.DbUserRepository;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final DbTokenRepository tokenRepository;

    @Autowired
    public AuthService(UserDetailsService userDetailsService, BCryptPasswordEncoder encoder, DbUserRepository userRepository,
                       UserService userService, DbTokenRepository tokenRepository) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        this.userService = userService;
        this.tokenRepository = tokenRepository;
    }

    public User findUser(String login, String pass) {
        User user = userService.findByLogin(login);
        if (encoder.matches(pass, user.getPassword()))
            return user;
        throw new UsernameNotFoundException("Not found");
    }
}

