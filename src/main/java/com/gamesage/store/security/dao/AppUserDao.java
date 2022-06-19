package com.gamesage.store.security.dao;

import com.gamesage.store.security.model.AppUser;

import java.util.Optional;

public interface AppUserDao {

    Optional<AppUser> selectUserByUsername(String username);
}

