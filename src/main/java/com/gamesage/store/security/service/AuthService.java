package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.dao.AppUserDao;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private final AppUserDao appUserDao;

    @Autowired
    public AuthService(UserService userService, @Qualifier("db_gamesage") AppUserDao appUserDao) {
        this.userService = userService;
        this.appUserDao = appUserDao;
    }

    private boolean checkIfCredentialsAreTheSame(String login1, String login2, String pass1, String pass2) {
        return login1.equals(login2) && pass1.equals(pass2);
    }

    public boolean checkIfUserExists(String login, String pass) {
        User user = findUserByLogin(login);
        return checkIfCredentialsAreTheSame(login, user.getLogin(), pass, user.getPassword());
    }

    private User findUserByLogin(String login) {
        return userService.findByLogin(login);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return appUserDao
                .selectUserByUsername(login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", login)));
    }
}

