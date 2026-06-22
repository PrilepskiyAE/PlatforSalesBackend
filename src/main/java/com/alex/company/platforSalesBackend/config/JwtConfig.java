package com.alex.company.platforSalesBackend.config;

import com.alex.company.platforSalesBackend.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
   private @Value("${jwt.secret}") String secret;
   private @Value("${jwt.expiration}") Long expiration;
    @Bean
    public JwtService jwtService() {
        return new JwtService(secret, expiration);
    }
}
