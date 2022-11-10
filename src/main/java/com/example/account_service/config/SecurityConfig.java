package com.example.account_service.config;

import com.example.account_service.filters.jwtfilters.AfterJWTFilter;
import com.example.account_service.filters.jwtfilters.BeginnerJWTFilter;
import com.example.account_service.services.security.AccountDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
//@Configuration
public class SecurityConfig /*extends WebSecurityConfigurerAdapter */ {

    private final AccountDetailsService accountDetailsService;
    private final BeginnerJWTFilter beginnerJWTFilter;
    private final AfterJWTFilter afterJWTFilter;


    public SecurityConfig(AccountDetailsService accountDetailsService, BeginnerJWTFilter beginnerJWTFilter, AfterJWTFilter afterJWTFilter) {
        this.accountDetailsService = accountDetailsService;
        this.beginnerJWTFilter = beginnerJWTFilter;
        this.afterJWTFilter = afterJWTFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/master/addMaster/**",
                        "/participant/addParticipant",
                        "/sender",
                        "/swagger-ui/*",
                        "/swagger-ui.html",
                        "/start",
                        "/swagger.html",
                        "/v1-api-YOGA-account",
                        "/check/code/*").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/master/addMaster/**",
                        "/participant/addParticipant",
                        "/sender-mail",
                        "/sender-sms",
                        "/swagger-ui/*",
                        "/swagger-ui.html",
                        "/start",
                        "/swagger.html",
                        "/v1-api-YOGA-account",
                        "/check/code",
                        "/check/code/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(beginnerJWTFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin().permitAll()
                .defaultSuccessUrl("/", true)
                .and()
                .addFilterAfter(afterJWTFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
