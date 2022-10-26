package com.example.account_service.services.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.account_service.models.security.Account;
import com.example.account_service.security.AccountDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.issure}")
    private String ISSUER;

    @Value("${jwt.secret}")
    private String SECRET;

    public String createJwtToken(AccountDetails userDetails){

        Account account = userDetails.getAccount();

        return JWT.create()
                .withSubject("Account")
                .withClaim("username", account.getEmail())
                .withClaim("role", account.getAuthorities().name())
//                .withClaim("id", account.getId())
                .withIssuer(ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(ZonedDateTime.now().plusMinutes(30).toInstant()))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String getAccountFromToken(String token){
        var verification = JWT.require(Algorithm.HMAC256(SECRET))
                .withSubject("Account")
                .withIssuer(ISSUER).build();
        var userName = verification.verify(token).getClaim("username").asString();
        return userName;
    }

    public boolean checkDateFromToken(String token){
        var verification = JWT.require(Algorithm.HMAC256(SECRET))
                .withSubject("Account")
                .withIssuer(ISSUER).build();
        var expireDate = verification.verify(token).getExpiresAt();
        return expireDate.after(new Date());
    }
}
