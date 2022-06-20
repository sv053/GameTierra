package com.gamesage.store.security.configs;

import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    public WebSecurityConfig(AuthService authService, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
        this.authService = authService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/games", "/games/**", "/login")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/users", "/cart")
                .authenticated()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}

