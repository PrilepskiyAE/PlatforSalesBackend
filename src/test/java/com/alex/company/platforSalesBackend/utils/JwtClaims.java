package com.alex.company.platforSalesBackend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

import java.util.Base64;

public class JwtClaims {
   public static Claims parseJwtClaims(String token) {

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        // Парсим второй кусок — payload
        byte[] decoded = decoder.decode(chunks[1]);
        String jsonBody = new String(decoded);

        // Конвертируем в Claims (через Jackson можно, но проще — вручную парсить как Map)
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonBody, DefaultClaims.class);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JWT payload", e);
        }
}
}
