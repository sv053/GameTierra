package com.gamesage.store.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public WebSecurityConfig() {

        //   securityContext = SecurityContextHolder.getContext();
        //   this.user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }

//    public SecurityProperties.User getUser() {
//        return user;
//    }

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

}

//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user")
//                .password(passwordEncoder.encode("password"))
//                .roles("USER");
//
//    }
//}

