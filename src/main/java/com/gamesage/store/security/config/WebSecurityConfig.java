package com.gamesage.store.security.config;

import com.gamesage.store.security.config.auth.AuthFilter;
import com.gamesage.store.security.config.auth.AuthProvider;
import com.gamesage.store.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userService;
    private final BCryptPasswordEncoder encoder;
    private final AuthProvider authProvider;

    public WebSecurityConfig(UserService userService, BCryptPasswordEncoder encoder, AuthProvider authProvider) {
        this.userService = userService;
        this.encoder = encoder;
        this.authProvider = authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(createCustomFilter(), AnonymousAuthenticationFilter.class)
                .csrf().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/login", "/users")
//                .permitAll()
//                .and()
//                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/games", "/games/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users", "/cart", "/users/**", "/cart/**")
                .authenticated()
                .and()
                .logout()
                .permitAll()
                //  .logoutSuccessUrl("/games")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    protected AbstractAuthenticationProcessingFilter createCustomFilter() throws Exception {
        AuthFilter filter = new AuthFilter(new NegatedRequestMatcher(
                new AndRequestMatcher(
                        new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/games")
                        //      new AntPathRequestMatcher("/users")
                )
        ));
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth
//                .authenticationProvider(daoAuthenticationProvider());
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(authProvider);
//    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Bean
    public AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }
}

