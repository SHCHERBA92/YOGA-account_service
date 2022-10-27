package com.example.account_service.filters.jwtfilters;

import com.example.account_service.services.security.AccountDetailsService;
import com.example.account_service.services.security.JWTService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;


@Component
public class BeginnerJWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final AccountDetailsService accountDetailsService;

    public BeginnerJWTFilter(JWTService jwtService, AccountDetailsService accountDetailsService) {
        this.jwtService = jwtService;
        this.accountDetailsService = accountDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var authorizationHeaders = Arrays.stream(request.getCookies())
                .filter(cookie -> "ACCESS-TOKEN".equals(cookie.getName()))
                .collect(Collectors.toList());

        if (authorizationHeaders.size() > 0) {
            var authorizationHeader = authorizationHeaders.get(0);
            if (!authorizationHeader.getValue().isBlank()) {
                var token = authorizationHeader.getValue();
                if (jwtService.checkDateFromToken(token)) {
                    var userNAme = jwtService.getAccountFromToken(token);
                    var account = accountDetailsService.loadUserByUsername(userNAme);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(account, account.getPassword(), account.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
