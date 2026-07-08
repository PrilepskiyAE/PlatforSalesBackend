package com.alex.company.platforSalesBackend.service;

import com.alex.company.platforSalesBackend.dto.auth.AuthRequest;
import com.alex.company.platforSalesBackend.dto.auth.AuthResponse;
import com.alex.company.platforSalesBackend.dto.registration.RegisterRequest;
import com.alex.company.platforSalesBackend.entity.UserEntity;
import com.alex.company.platforSalesBackend.exception.AuthenticationException;
import com.alex.company.platforSalesBackend.exception.InvalidRegistrationDataException;
import com.alex.company.platforSalesBackend.exception.UserAlreadyExistsException;
import com.alex.company.platforSalesBackend.exception.UserNotFoundException;
import com.alex.company.platforSalesBackend.repository.UserRepository;
import com.alex.company.platforSalesBackend.security.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }

        if (registerRequest.getUsername() == null || registerRequest.getPassword() == null) {
            throw new InvalidRegistrationDataException("Имя пользователя и пароль обязательны");
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        if (registerRequest.isAdmin()) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user.getUsername(),user.getRole());
        return new AuthResponse(jwtToken, jwtService.getExpirationTime());
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new AuthenticationException("Неверные учётные данные", e);
        }

        UserEntity user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        String jwtToken = jwtService.generateToken(user.getUsername(), user.getRole());
        return new AuthResponse(jwtToken, jwtService.getExpirationTime());
    }

}

