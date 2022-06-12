package com.gamesage.store.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/games", "/games/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/login/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .logout()
                .permitAll();
    }
}

