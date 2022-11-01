package com.gamesage.store.security.auth.manager;

import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthManager implements AuthenticationManager {

    @Autowired
    private UserService userService;
    // @Autowired
    //  private AuthProvider authProvider;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        UserDetails userToAuthenticate =  (UserDetails) authentication.getPrincipal();
//        User persistentUser = userService.findByLogin(userToAuthenticate.getUsername());
//
//        if (userToAuthenticate.getPassword().equals(persistentUser.getPassword())){
//            authentication.setAuthenticated(true);
//        }
        return authentication;
    }
}

