package com.gamesage.store.security.config;

import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.auth.filter.PreAuthenticationFilter;
import com.gamesage.store.security.auth.filter.TransactionFilter;
import com.gamesage.store.security.auth.manager.AuthManager;
import com.gamesage.store.security.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

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
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers()
                .addHeaderWriter(new StaticHeadersWriter(HeaderName.TOKEN_HEADER, ""))
                .and()
                .csrf().and().cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .antMatcher("/users")
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint());

        http
                .addFilterAfter(new PreAuthenticationFilter(authenticationManagerBean()), BasicAuthenticationFilter.class)
                .addFilterAfter(transactionFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public TransactionFilter transactionFilter() {
        TransactionFilter filter = new TransactionFilter("/users", authManager, authService);
        filter.setFilterProcessesUrl("/users/**");
        return filter;
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}

