package com.alex.company.platforSalesBackend.controller;

import com.alex.company.platforSalesBackend.AppApplication;
import com.alex.company.platforSalesBackend.dto.LoginRequest;
import com.alex.company.platforSalesBackend.dto.RegisterRequest;
import com.alex.company.platforSalesBackend.repository.UserRepository;
import com.alex.company.platforSalesBackend.utils.JwtClaims;
import io.jsonwebtoken.Claims;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest(
        classes = AppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Feature("Аутентификация")
@Epic("API: Авторизация и регистрация пользователей")
@Owner("Prilepskiy AE")
public class AuthControllerRestAssuredTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        userRepository.flush();
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/auth";
        RestAssured.defaultParser = Parser.JSON;
    }

    String username = "testuser_" + System.currentTimeMillis();
    String email = username + "@example.com";
    String pass = "password1233";
    RegisterRequest request = new RegisterRequest(username, email, pass);
    LoginRequest loginRequest = new LoginRequest(request.getUsername(), pass);

    @Test
    @Order(1)
    @DisplayName("Регистрация: возвращается токен и 201 Created")
    @Description("Пользователь отправляет корректные данные для регистрации — система возвращает JWT-токен и статус 201")
    @Severity(SeverityLevel.BLOCKER)
    void registerShouldReturnCreatedAndToken() {

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/register")
                .then()
                .statusCode(201)
                .body("token", notNullValue())
                .body("expiresIn", greaterThanOrEqualTo(86400000));
    }

    @Test
    @Order(2)
    @DisplayName("Регистрация: токен содержит корректные claims")
    @Description("После регистрации извлечённый JWT должен содержать subject, срок действия >=23ч, и роль ROLE_USER")
    @Severity(SeverityLevel.CRITICAL)
    void registerCorrectToken() {

        String newToken =  given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/register")
                .then()
                .statusCode(201)
                .body("token", notNullValue())
                .extract().path("token");

        Claims claims = JwtClaims.parseJwtClaims(newToken);
        System.out.println("🧍 sub (пользователь): " + claims.getSubject());
        System.out.println("🕒 Срок действия: " + claims.getExpiration());
        System.out.println("📅 Выдан: " + claims.getIssuedAt());
        System.out.println("🏷 Роли: " + claims.get("authorities"));

        assertThat(claims.getSubject()).isEqualTo(request.getUsername());


        long expirationTime = claims.getExpiration().getTime();
        long issuedAt = claims.getIssuedAt().getTime();
        long duration = expirationTime - issuedAt;

        assertThat(duration).isGreaterThanOrEqualTo(82800000L);
        assertThat(claims.get("authorities")).isEqualTo(List.of("ROLE_USER"));

    }

    @Test
    @Order(3)
    @DisplayName("Логин: при верных данных возвращается токен и 200 OK")
    @Description("Пользователь с существующими учётными данными должен получить валидный JWT и ответ 200")
    @Severity(SeverityLevel.BLOCKER)
    void loginShouldReturnTokenWhenCredentialsCorrect() {

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/register")
                .then()
                .statusCode(201);


        String newToken = given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("expiresIn", greaterThanOrEqualTo(86400000))
                .extract()
                .response().path("token");;

        Claims claims = JwtClaims.parseJwtClaims(newToken);

        assertThat(claims.getSubject()).isEqualTo(request.getUsername());
        assertThat(claims.get("authorities")).isEqualTo(List.of("ROLE_USER"));
    }

    @Test
    @Order(4)
    @DisplayName("Логин: неверный пароль — возвращается 401 Unauthorized")
    @Description("Попытка входа с неправильным паролем должна завершаться ошибкой 401 с сообщением")
    @Severity(SeverityLevel.CRITICAL)
    void loginWithWrongPasswordShouldReturnUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .statusCode(401)
                .body("message", containsStringIgnoringCase("неверные учётные данные"));
    }

    @Test
    @Order(5)
    @DisplayName("Логин: пустой логин — возвращается 401")
    @Description("Попытка входа с пустым username должна вернуть 401 (без утечки информации)")
    @Severity(SeverityLevel.MINOR)
    void loginWithEmptyUsernameShouldReturnBadRequest() {
        LoginRequest loginRequest = new LoginRequest("", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .statusCode(401)
                .body("message", containsStringIgnoringCase("Неверные учётные данные"));
    }

}
