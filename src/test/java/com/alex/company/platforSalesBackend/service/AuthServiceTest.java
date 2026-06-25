package com.alex.company.platforSalesBackend.service;

import com.alex.company.platforSalesBackend.dto.AuthRequest;
import com.alex.company.platforSalesBackend.dto.AuthResponse;
import com.alex.company.platforSalesBackend.dto.RegisterRequest;
import com.alex.company.platforSalesBackend.entity.UserEntity;
import com.alex.company.platforSalesBackend.exception.AuthenticationException;
import com.alex.company.platforSalesBackend.exception.InvalidRegistrationDataException;
import com.alex.company.platforSalesBackend.exception.UserAlreadyExistsException;
import com.alex.company.platforSalesBackend.exception.UserNotFoundException;
import com.alex.company.platforSalesBackend.repository.UserRepository;
import com.alex.company.platforSalesBackend.security.JwtService;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Feature("Сервис Аунтификации")
@Owner("Prilepskiy AE")
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private final String USERNAME = "prilepskiy_ae";
    private final String PASSWORD = "password123";
    private final String ROLE_USER = "USER";
    private final String EMAIL = "john@example.com";
    private final String ROLE_ADMIN = "ADMIN";
    private final String JWT_TOKEN = "mock-jwt-token";
    private final Long EXPIRATION = 3600L;
    RegisterRequest request = new RegisterRequest(USERNAME,EMAIL  ,PASSWORD);

    @Test
    @DisplayName("Регистрация: успешная → пользователь сохранён, пароль закодирован, токен сгенерирован")
    void register_ValidRequest_ShouldCreateUserAndReturnToken() {

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encodedPassword");
        when(jwtService.generateToken(USERNAME, ROLE_USER)).thenReturn(JWT_TOKEN);
        when(jwtService.getExpirationTime()).thenReturn(EXPIRATION);

        AuthResponse response = authService.register(request);

        verify(userRepository).findByUsername(USERNAME);
        verify(passwordEncoder).encode(PASSWORD);
        verify(userRepository).save(argThat(user ->
                user.getUsername().equals(USERNAME) &&
                        user.getPassword().equals("encodedPassword") &&
                        user.getRole().equals(ROLE_USER)
        ));
        verify(jwtService).generateToken(USERNAME, ROLE_USER);
        assertThat(response.getToken()).isEqualTo(JWT_TOKEN);
        assertThat(response.getExpiresIn()).isEqualTo(EXPIRATION);
    }

    @Test
    @DisplayName("Регистрация: пользователь уже существует → бросает исключение")
    void register_UserExists_ShouldThrowUserAlreadyExistsException() {

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new UserEntity()));

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("Пользователь с таким именем уже существует");

        verify(userRepository).findByUsername(USERNAME);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Регистрация: пустой логин или пароль → бросает исключение")
    void register_MissingFields_ShouldThrowInvalidRegistrationDataException() {
        RegisterRequest request = new RegisterRequest(null, null, null);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(InvalidRegistrationDataException.class)
                .hasMessage("Имя пользователя и пароль обязательны");
    }

    @Test
    @DisplayName("Аутентификация: успешная → возвращается токен")
    void authenticate_ValidCredentials_ShouldReturnToken() {

        AuthRequest request = new AuthRequest(USERNAME, PASSWORD);
        UserEntity user = new UserEntity();
        user.setUsername(USERNAME);
        user.setRole(ROLE_USER);
        Authentication auth = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(USERNAME, ROLE_USER)).thenReturn(JWT_TOKEN);
        when(jwtService.getExpirationTime()).thenReturn(EXPIRATION);

        AuthResponse response = authService.authenticate(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(USERNAME, ROLE_USER);
        assertThat(response.getToken()).isEqualTo(JWT_TOKEN);
        assertThat(response.getExpiresIn()).isEqualTo(EXPIRATION);
    }

    @Test
    @DisplayName("Аутентификация: неверные учётные данные → бросает исключение")
    void authenticate_InvalidCredentials_ShouldThrowAuthenticationException() {

        AuthRequest request = new AuthRequest(USERNAME, PASSWORD);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid"));

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Неверные учётные данные");
    }

    @Test
    @DisplayName("Аутентификация: пользователь не найден → бросает исключение")
    void authenticate_UserNotFound_ShouldThrowUserNotFoundException() {
        AuthRequest request = new AuthRequest(USERNAME, PASSWORD);
        Authentication auth = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticate(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Пользователь не найден");
    }
}
