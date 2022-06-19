package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.dao.AppUserDao;
import com.gamesage.store.security.model.AppUser;
import com.gamesage.store.security.model.Role;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("db_gamesage")
public class UserDaoService implements AppUserDao {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDaoService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUser> selectUsers(String username) {
        User user = userService.findByLogin(username);
        return List.of(
                new AppUser(
                        new Role().getAllowedOperations(),
                        passwordEncoder.encode(user.getPassword()),
                        user.getLogin(),
                        true,
                        true,
                        true,
                        true
                ));
    }

    @Override
    public Optional<AppUser> selectUserByUsername(String username) {
        return selectUsers(username)
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }
}

