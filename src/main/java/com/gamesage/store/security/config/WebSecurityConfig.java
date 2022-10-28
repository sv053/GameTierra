package com.gamesage.store.security.config;

import com.gamesage.store.security.config.auth.AuthFilter;
import com.gamesage.store.security.config.auth.AuthProvider;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    private final AuthProvider authProvider;
    private final AuthService authService;

    public WebSecurityConfig(UserService userService, BCryptPasswordEncoder encoder, AuthProvider authProvider, AuthService authService) {
        this.userService = userService;
        this.encoder = encoder;
        this.authProvider = authProvider;
        this.authService = authService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login")
                .permitAll()
                .and()
                .addFilterBefore(new AuthFilter(authProvider, authService), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .logout()
                .permitAll();
    }

//    public AuthenticationManager usersAuthenticationManager() {
//        return authentication -> {
//            User user = userService.findByLogin(authentication.getName());
//            if (user != null) {
//                return new UsernamePasswordAuthenticationToken(user, null);
//            }
//            throw new EntityNotFoundException(authentication.getName());
//        };
//    }
//
//    protected AuthenticationManagerResolver<HttpServletRequest> resolver() {
//        return request -> {
//            if (request.getPathInfo().startsWith("/users")) {
//                return usersAuthenticationManager();
//            }
//            return usersAuthenticationManager();
//        };
//    }
//
//    private AuthenticationFilter authenticationFilter() {
//        AuthenticationFilter filter = new AuthenticationFilter(
//                resolver(),
//                authenticationFilter().getAuthenticationConverter());
//        filter.setSuccessHandler((request, response, auth) -> {});
//        return filter;
//    }
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .authorizeExchange()
//                .pathMatchers("/**")
//                .authenticated()
//                .and()
//                .httpBasic()
//                .disable()
//                .addFilterAfter(
//                        new AuthenticationWebFilter((ReactiveAuthenticationManager) resolver()),
//                        SecurityWebFiltersOrder.REACTOR_CONTEXT
//                )
//                .build();
//    }
}

