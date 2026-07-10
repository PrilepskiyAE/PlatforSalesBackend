package com.alex.company.platforSalesBackend.controller;

import com.alex.company.platforSalesBackend.config.JwtConfig;
import com.alex.company.platforSalesBackend.dto.auth.login.LoginRequest;
import com.alex.company.platforSalesBackend.dto.registration.RegisterRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

public abstract class BaseControllerTest {
    @Value("${local.server.port}")
    private  int port;
    @Autowired
    protected JwtConfig jwtConfig;
    protected String validBearerToken;
    String username = "string";
    String pass = "string";
    LoginRequest loginRequest = new LoginRequest(username, pass);
    @BeforeEach
     void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
        validBearerToken = given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("expiresIn", greaterThanOrEqualTo(86400000))
                .extract()
                .response().path("token");
    }

}
