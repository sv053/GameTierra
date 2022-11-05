package com.gamesage.store.security.config;

import com.gamesage.store.security.auth.filter.PreAuthenticationFilter;
import com.gamesage.store.security.auth.filter.TransactionFilter;
import com.gamesage.store.security.auth.manager.AuthManager;
import com.gamesage.store.security.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthManager authManager;
    private final AuthService authService;

    public WebSecurityConfig(AuthManager authManager, AuthService authService) {
        this.authManager = authManager;
        this.authService = authService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                antMatchers("/login").
                permitAll().
                and().
                authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint());

        http
                .addFilterAfter(preAuthenticationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(transactionFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public PreAuthenticationFilter preAuthenticationFilter() {
        PreAuthenticationFilter filter = new PreAuthenticationFilter();
        return filter;
    }

    @Bean
    public TransactionFilter transactionFilter() {
        TransactionFilter filter = new TransactionFilter("/login", authManager, authService);
        return filter;
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}

