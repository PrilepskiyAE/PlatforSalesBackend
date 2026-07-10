package com.alex.company.platforSalesBackend.controller;

import com.alex.company.platforSalesBackend.AppApplication;
import com.alex.company.platforSalesBackend.dto.category.CategoryRequest;
import com.alex.company.platforSalesBackend.dto.category.CategoryResponse;
import com.alex.company.platforSalesBackend.repository.CategoryRepository;
import com.alex.company.platforSalesBackend.security.JwtService;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(
        classes = AppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Feature("Категории")
@Epic("API: Категории товаров, получение, добавление, удаление")
@Owner("Prilepskiy AE")
public class CategoryControllerTest extends BaseControllerTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    protected JwtService jwtService;

    private String validBearerToken;

    @BeforeEach
    void setUp() {
        // ❌ УБРАЛИ basePath — это была главная причина 401
        String rawToken = jwtService.generateToken("test-user", "USER");
        validBearerToken = "Bearer " + rawToken;
    }

    @Test
    @DisplayName("POST /api/categories — создание категории должно вернуть 201")
    void testCreateCategory() {
        CategoryRequest request = new CategoryRequest( "Electronics", "Devices and gadgets");

        System.out.println("DEBUG: Token length = " + validBearerToken.length());
        System.out.println("DEBUG: Token starts with = " + validBearerToken.substring(0, Math.min(30, validBearerToken.length())));

        CategoryResponse response = given()
                .header("Authorization", validBearerToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                // ✅ Пишем полный путь явно
                .post("/api/categories")
                .then()
                .statusCode(201)
                .body("categoryId", notNullValue(),
                        "categoryName", equalTo("Electronics"),
                        "description", equalTo("Devices and gadgets"))
                .extract()
                .as(CategoryResponse.class);

        assertThat(response.getCategoryName()).isEqualTo("Electronics");
    }
}
