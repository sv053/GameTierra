package com.gamesage.store.security.config;

import com.gamesage.store.security.filter.FilterChainExceptionHandler;
import com.gamesage.store.security.filter.TokenPreAuthenticatedProcessingFilter;
import com.gamesage.store.security.service.TokenAuthenticatedUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticatedUserDetailsService tokenAuthenticatedUserDetailsService;
    private final FilterChainExceptionHandler exceptionHandler;

    public WebSecurityConfig(TokenAuthenticatedUserDetailsService tokenAuthenticatedUserDetailsService, FilterChainExceptionHandler exceptionHandler) {
        this.tokenAuthenticatedUserDetailsService = tokenAuthenticatedUserDetailsService;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/login").anonymous()
                .antMatchers(HttpMethod.GET, "/users", "/cart", "/users/**", "/cart/**").authenticated()
                .and()
                .addFilter(tokenAuthFilter())
                .addFilterBefore(exceptionHandler, LogoutFilter.class);
    }

    @Bean
    public TokenPreAuthenticatedProcessingFilter tokenPreAuthenticatedProcessingFilter() {
        TokenPreAuthenticatedProcessingFilter tokenPreAuthenticatedFilter = new TokenPreAuthenticatedProcessingFilter();
        tokenPreAuthenticatedFilter.setAuthenticationManager(tokenAuthenticationManager());
        return tokenPreAuthenticatedFilter;
    }

    @Bean
    public Filter tokenAuthFilter() {
        TokenPreAuthenticatedProcessingFilter filter = new TokenPreAuthenticatedProcessingFilter();
        filter.setAuthenticationManager(tokenAuthenticationManager());
        return filter;
    }

    @Bean
    public AuthenticationManager tokenAuthenticationManager() {
        PreAuthenticatedAuthenticationProvider preAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthProvider.setPreAuthenticatedUserDetailsService(tokenAuthenticatedUserDetailsService);
        return new ProviderManager(preAuthProvider);
    }
}

