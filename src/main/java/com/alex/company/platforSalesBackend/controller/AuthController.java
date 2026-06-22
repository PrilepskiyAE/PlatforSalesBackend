package com.alex.company.platforSalesBackend.controller;

import com.alex.company.platforSalesBackend.dto.AuthRequest;
import com.alex.company.platforSalesBackend.dto.AuthResponse;
import com.alex.company.platforSalesBackend.dto.RegisterRequest;
import com.alex.company.platforSalesBackend.service.AuthService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@Validated
@Tag(name = "Authentication", description = "API для аутентификации и регистрации пользователей")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт нового пользователя в системе и возвращает JWT‑токен"
    )
    @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован")
    @ApiResponse(responseCode = "400", description = "Некорректные данные регистрации")
    @ApiResponse(responseCode = "409", description = "Пользователь с таким именем уже существует")
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Данные для регистрации пользователя") @Valid @RequestBody  RegisterRequest registerRequest) {

        AuthResponse authResponse = authService.register(registerRequest);
        return ResponseEntity.status(201).body(authResponse);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Проверяет учётные данные пользователя и возвращает JWT‑токен"
    )
    @ApiResponse(responseCode = "200", description = "Аутентификация успешна, токен выдан")
    @ApiResponse(responseCode = "401", description = "Неверные учётные данные")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<AuthResponse> authenticate(
            @Parameter(description = "Учётные данные пользователя") @Valid @RequestBody AuthRequest authRequest) {

        try {
            AuthResponse authResponse = authService.authenticate(authRequest);
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
