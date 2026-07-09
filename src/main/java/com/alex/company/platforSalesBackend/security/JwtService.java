package com.alex.company.platforSalesBackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtService {

    private final SecretKey key;
    private final Long expiration;

    public JwtService(String secret, Long expiration) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = expiration;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", List.of("ROLE_" + role));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Токен истёк: " + e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            System.out.println("Неподдерживаемый JWT: " + e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            System.out.println("Некорректный JWT: " + e.getMessage());
            return false;
        } catch (SignatureException e) {
            System.out.println("Ошибка подписи: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Неожиданная ошибка при валидации: " + e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }
    public Long getExpirationTime() {
        return expiration;
    }
    public List<String> getAuthoritiesFromToken(String token) {
        return (List<String>) parseClaims(token).get("authorities");
    }
}
