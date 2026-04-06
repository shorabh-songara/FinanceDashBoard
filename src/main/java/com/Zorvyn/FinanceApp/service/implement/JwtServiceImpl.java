package com.Zorvyn.FinanceApp.service.implement;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.Zorvyn.FinanceApp.enums.Roles;
import com.Zorvyn.FinanceApp.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService{

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long EXPIRATION_TIME;


    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }


    @Override
    public String generateToken(String email, Roles role, Long id) {
        Map<String , Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("user_id" , id);

        return Jwts.builder()
        .setClaims(claims)
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .signWith(getSigningKey() , SignatureAlgorithm.HS256)
        .compact();
        
    }

    @Override
    public String extractRole(String token) {
       return extractClaims(token).get("role" , String.class);
    }

    @Override
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    @Override
    public Boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    @Override
    public Boolean validateToken(String email, String token) {
       final String extractedEmail = extractEmail(token);
       return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    @Override
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
