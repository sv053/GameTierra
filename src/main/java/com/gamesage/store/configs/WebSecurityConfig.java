package com.gamesage.store.configs;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "*/games").permitAll()
                .antMatchers("*/login", "*/login/*").authenticated()
                .and()
                .logout()
                .permitAll();
    }
}
