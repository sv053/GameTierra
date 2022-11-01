package com.gamesage.store.security.config;

import com.gamesage.store.security.auth.filter.AuthenticationLoggingFilter;
import com.gamesage.store.security.auth.filter.RequestValidationFilter;
import com.gamesage.store.security.auth.manager.AuthManager;
import com.gamesage.store.security.auth.provider.AuthProvider;
import com.gamesage.store.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProvider authProvider;
    private final AuthManager authManager;
    private final RequestValidationFilter requestValidationFilter;
    private final AuthenticationLoggingFilter authenticationLoggingFilter;
    private final UserService userService;

    public WebSecurityConfig(AuthProvider authProvider, AuthManager authManager, UserService userService,
                             RequestValidationFilter requestValidationFilter, AuthenticationLoggingFilter authenticationLoggingFilter) {
        this.userService = userService;
        this.authProvider = authProvider;
        this.authManager = authManager;
        this.requestValidationFilter = requestValidationFilter;
        this.authenticationLoggingFilter = authenticationLoggingFilter;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                //  .authenticationProvider(authProvider)
                //  .addFilterBefore(requestValidationFilter, BasicAuthenticationFilter.class)
                //  .addFilterAfter(authenticationLoggingFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .permitAll();
    }

//    public Authentication usersAuthenticationManager() {
//        AuthManager authManager = new AuthManager();
//        try {
//            User user = userService.findByLogin(authManager.authenticate());
//            if (user != null) {
//                return authManager.authenticate(new AuthToken(user));
//            }
//        } catch (Exception e) {
//            throw new EntityNotFoundException(e.getMessage());
//        }
}

