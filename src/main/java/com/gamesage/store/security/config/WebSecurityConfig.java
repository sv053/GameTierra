package com.gamesage.store.security.config;

import com.gamesage.store.security.config.provider.AuthenticationProvider_;
import com.gamesage.store.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

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
    private final AuthenticationProvider_ provider;
    //  @Value("${howtodoinjava.http.auth.tokenName}")
    private String authHeaderName;
    //TODO: retrieve this token value from data source
    //  @Value("${howtodoinjava.http.auth.tokenValue}")
    private String authHeaderValue;


    public WebSecurityConfig(UserService userService, BCryptPasswordEncoder encoder,
                             final AuthenticationProvider_ authenticationProvider) {
        this.userService = userService;
        this.encoder = encoder;
        this.provider = authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        PreAuthTokenHeaderFilter filter = new PreAuthTokenHeaderFilter(authHeaderName);
//
//        filter.setAuthenticationManager(authentication -> {
//            String principal = (String) authentication.getPrincipal();
//
//            if (!authHeaderValue.equals(principal)) {
//                throw new BadCredentialsException("The API key was not found "
//                        + "or not the expected value.");
//            }
//            authentication.setAuthenticated(true);
//            return authentication;
//        });

        http
                .csrf().disable()
                .httpBasic()
                .and()
//                .addFilter(filter)
//                .addFilterBefore(new ExceptionTranslationFilter(
//                                new Http403ForbiddenEntryPoint()),
//                        filter.getClass()
//                )
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login", "/users")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/games", "/games/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users", "/cart", "/users/**", "/cart/**")
                .authenticated()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/games")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");


        http.
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                antMatchers(actuatorEndpoints()).hasRole(backendAdminRole).
                anyRequest().authenticated().
                and().
                anonymous().disable().
                exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());

        http.addFilterBefore(new AuthenticationFilter_(authenticationManager()), BasicAuthenticationFilter.class).
                addFilterBefore(new ManagementEndpointAuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);

    }

    //   @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .exceptionHandling()
//                .and()
//                .authenticationProvider(provider)
//                .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
//                .authorizeRequests()
//                .requestMatchers(PROTECTED_URLS)
//                .authenticated()
//                .and()
//                .csrf().disable()
//                .formLogin().disable()
//                .httpBasic().disable()
//                .logout().disable();
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(daoAuthenticationProvider())
                // .authenticationProvider(domainUsernamePasswordAuthenticationProvider())
                // .authenticationProvider(backendAdminUsernamePasswordAuthenticationProvider())
                .authenticationProvider(tokenAuthenticationProvider());
    }

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
//
//    private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
//            new AntPathRequestMatcher("/api/**")
//    );

//    @Override
//    protected void configure(final AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(provider);
//    }
//
//    @Override
//    public void configure(final WebSecurity webSecurity) {
//        webSecurity.ignoring().antMatchers("/token/**");
//    }
//
//    @Bean
//    AuthenticationFilter authenticationFilter() throws Exception {
//        final AuthenticationFilter filter = new AuthenticationFilter(PROTECTED_URLS);
//        filter.setAuthenticationManager(authenticationManager());
//        //filter.setAuthenticationSuccessHandler(successHandler());
//        return filter;
//    }

    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }
}

