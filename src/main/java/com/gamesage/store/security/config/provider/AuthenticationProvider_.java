package com.gamesage.store.security.config.provider;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.security.model.AppUser;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
public class AuthenticationProvider_ extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    UserService userService;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        User user = userService.findByLogin(s);
        return new AppUser(user);
    }
}
