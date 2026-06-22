package com.alex.company.platforSalesBackend.config;

import com.alex.company.platforSalesBackend.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Bean
    public JwtService jwtService( @Value("${jwt.secret}") String secret,
                                  @Value("${jwt.expiration}") Long expiration) {
        return new JwtService(secret, expiration);
    }
}
