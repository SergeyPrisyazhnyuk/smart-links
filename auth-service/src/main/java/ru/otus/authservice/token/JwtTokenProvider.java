package ru.otus.authservice.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secretKey = "secretKeyTheReallySecretKeyAndReallyReallySecretKey";

    public String generateToken(String username) {
        Claims claims = Jwts.claims().setSubject(username).build();
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}