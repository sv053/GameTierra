package com.gamesage.store.security.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.TreeSet;

public class Role implements GrantedAuthority {

    private final Set<GrantedAuthority> allowedOperations = new TreeSet<>();
    private String id;

    public Role() {
    }

    @Override
    public String getAuthority() {
        return id;
    }

    public Set<GrantedAuthority> getAllowedOperations() {
        return allowedOperations;
    }
}

