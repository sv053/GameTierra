package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.dao.AppUserDao;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private final AppUserDao appUserDao;
    @Value("${spring.security.user.name}")
    private String name;
    @Value("${spring.security.user.password}")
    private String password;
    @Value("${spring.security.user.roles}")
    private String roles;

    @Autowired
    public AuthService(UserService userService, @Qualifier("db_gamesage") AppUserDao appUserDao) {
        this.userService = userService;
        this.appUserDao = appUserDao;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkIfCredentialsExist(String login, String pass) {
        return login.equals(name) && password.equals(pass);
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

