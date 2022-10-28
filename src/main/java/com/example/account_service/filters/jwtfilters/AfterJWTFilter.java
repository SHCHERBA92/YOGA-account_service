package com.example.account_service.filters.jwtfilters;


import com.example.account_service.security.AccountDetails;
import com.example.account_service.services.security.AccountDetailsService;
import com.example.account_service.services.security.JWTService;
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
public class AfterJWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final AccountDetailsService accountDetailsService;

    public AfterJWTFilter(JWTService jwtService, AccountDetailsService accountDetailsService) {
        this.jwtService = jwtService;
        this.accountDetailsService = accountDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeaders = Arrays.stream(request.getCookies())
                .filter(cookie -> "ACCESS-TOKEN".equals(cookie.getName()))
                .collect(Collectors.toList());

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            if (authorizationHeaders.size() == 0) {
                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    setCookieToken(response);
                }
            } else {
                var authorizationHeader = authorizationHeaders.get(0);
                if (authorizationHeader.getValue().isBlank() || !jwtService.checkDateFromToken(authorizationHeader.getValue())) {
                    setCookieToken(response);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setCookieToken(HttpServletResponse response) {
        var account = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var token = jwtService.createJwtToken(account);
        response.setHeader("Authorization", "Barer " + token);
        response.addCookie(new Cookie("ACCESS-TOKEN", token));
    }
}
