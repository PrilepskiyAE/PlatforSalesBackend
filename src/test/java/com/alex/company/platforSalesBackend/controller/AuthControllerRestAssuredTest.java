package com.alex.company.platforSalesBackend.controller;

import com.alex.company.platforSalesBackend.AppApplication;
import com.alex.company.platforSalesBackend.dto.RegisterRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@SpringBootTest(
        classes = AppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@Rollback
public class AuthControllerRestAssuredTest {

    @Value("${local.server.port}")
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/auth";
    }

    @Test
    @Order(1)
    void registerShouldReturnCreatedAndToken() {
        RegisterRequest request = new RegisterRequest("test", "test@example.com", "password1233");

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
}
