package com.Zorvyn.FinanceApp.service;

import com.Zorvyn.FinanceApp.enums.Roles;

import io.jsonwebtoken.Claims;

public interface JwtService {
    String generateToken(String email , Roles role , Long id);
    String  extractRole(String token);
    String extractEmail(String token);
    Boolean isTokenExpired(String token );
    Boolean validateToken(String email , String token);
    Claims extractClaims(String token);
}
