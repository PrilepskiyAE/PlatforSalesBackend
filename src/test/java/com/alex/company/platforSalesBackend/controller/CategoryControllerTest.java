package com.alex.company.platforSalesBackend.controller;

import com.alex.company.platforSalesBackend.AppApplication;
import com.alex.company.platforSalesBackend.dto.auth.login.LoginRequest;
import com.alex.company.platforSalesBackend.dto.category.CategoryRequest;
import com.alex.company.platforSalesBackend.dto.category.CategoryResponse;
import com.alex.company.platforSalesBackend.repository.CategoryRepository;
import com.alex.company.platforSalesBackend.security.JwtService;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.path.json.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
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
@Feature("Категории")
@Epic("API: Категории товаров, получение, добавление, удаление")
@Owner("Prilepskiy AE")
public class CategoryControllerTest extends BaseControllerTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("POST /api/categories — создание категории должно вернуть 201")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Как авторизованный пользователь я хочу создавать категории, чтобы они отображались в каталоге")
    void testCreateCategory() {
        CategoryRequest request = new CategoryRequest( "Electronics", "Devices and gadgets");

        CategoryResponse response = given()
                .header("Authorization", "Bearer " + validBearerToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/categories")
                .then()
                .statusCode(201)
                .body(
                        "categoryName", equalTo("Electronics"),
                        "description", equalTo("Devices and gadgets"))
                .extract()
                .as(CategoryResponse.class);

        assertThat(response.getCategoryName()).isEqualTo("Electronics");
    }

}
