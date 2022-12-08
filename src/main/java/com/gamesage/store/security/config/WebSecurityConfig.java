package com.gamesage.store.security.config;

import com.gamesage.store.security.auth.filter.FilterChainExceptionHandler;
import com.gamesage.store.security.auth.filter.PreAuthenticationFilter;
import com.gamesage.store.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() {
        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthenticatedAuthProvider.setPreAuthenticatedUserDetailsService(userService);
        return new ProviderManager(preAuthenticatedAuthProvider);
    }

    @Bean
    public PreAuthenticationFilter preAuthenticationFilter() throws Exception {
        PreAuthenticationFilter filter = new PreAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/games", "/games/**").permitAll()
                .and().authorizeRequests()
                .antMatchers("/users", "/users/**").authenticated()
                .and()
                .addFilter(preAuthenticationFilter())
                .addFilterAfter(new FilterChainExceptionHandler(), LogoutFilter.class);
    }
}

