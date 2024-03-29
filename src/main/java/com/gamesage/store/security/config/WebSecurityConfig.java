package com.gamesage.store.security.config;

import com.gamesage.store.controller.CustomLogoutHandler;
import com.gamesage.store.security.auth.filter.FilterChainExceptionHandler;
import com.gamesage.store.security.auth.filter.PreAuthenticationFilter;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final FilterChainExceptionHandler exceptionHandler;
    private final AuthService authService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder encoder;

    public WebSecurityConfig(UserService userService, FilterChainExceptionHandler exceptionHandler,
                             AuthService authService, TokenService tokenService, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.exceptionHandler = exceptionHandler;
        this.authService = authService;
        this.tokenService = tokenService;
        this.encoder = encoder;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() {
        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthenticatedAuthProvider.setPreAuthenticatedUserDetailsService(userService);
        return new ProviderManager(preAuthenticatedAuthProvider);
    }

    @Bean
    public PreAuthenticationFilter preAuthenticationFilter() {
        PreAuthenticationFilter filter = new PreAuthenticationFilter(tokenService, encoder);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    public LogoutHandler customLogoutHandler() {
        return new CustomLogoutHandler(authService, tokenService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").anonymous()
                .antMatchers(HttpMethod.GET, "/users", "/cart", "/users/**", "/cart/**").authenticated()
                .antMatchers(HttpMethod.POST, "/cart/**").authenticated()
                .and()
                .addFilter(preAuthenticationFilter())
                .addFilterBefore(exceptionHandler, LogoutFilter.class)
                .logout()
                .addLogoutHandler(customLogoutHandler())
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
    }
}

