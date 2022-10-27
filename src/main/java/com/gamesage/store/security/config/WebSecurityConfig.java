package com.gamesage.store.security.config;

import com.gamesage.store.security.config.auth.AuthFilter;
import com.gamesage.store.security.config.auth.AuthProvider;
import com.gamesage.store.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                //    .addFilterBefore(createCustomFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .permitAll();
    }

//    protected AbstractAuthenticationProcessingFilter createCustomFilter() throws Exception {
//        AuthFilter filter = new AuthFilter(new NegatedRequestMatcher(
//                new AndRequestMatcher(
//                        new AntPathRequestMatcher("/login"),
//                        new AntPathRequestMatcher("/games")
//                )
//        ), authProvider);
//        filter.setAuthenticationManager(authenticationManagerBean());
//        return filter;
//    }

    protected UsernamePasswordAuthenticationFilter createCustomFilter() throws Exception {
        AuthFilter filter = new AuthFilter(authProvider);
        //  filter.setAuthenticationDetailsSource();
        return filter;
    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth
//                .authenticationProvider(authProvider);
//    }

//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth
//                .authenticationProvider(daoAuthenticationProvider());
//    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(encoder);
//        provider.setUserDetailsService(userService);
//        return provider;
//    }
//
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public AuthenticationEntryPoint unauthorizedEntryPoint() {
//        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//    }
//
//    @Bean
//    public AuthenticationEntryPoint forbiddenEntryPoint() {
//        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
//    }
}

