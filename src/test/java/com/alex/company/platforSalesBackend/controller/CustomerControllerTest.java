package com.alex.company.platforSalesBackend.controller;

import com.alex.company.platforSalesBackend.AppApplication;
import com.alex.company.platforSalesBackend.dto.customer.CustomerRequest;
import com.alex.company.platforSalesBackend.dto.customer.CustomerResponse;
import com.alex.company.platforSalesBackend.repository.CustomerRepository;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
@Feature("Клиенты")
@Epic("API: Управление клиентами (создание, получение, список, удаление)")
@Owner("Prilepskiy AE")
public class CustomerControllerTest extends BaseControllerTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("POST /api/customers — создание клиента должно вернуть 201")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Как администратор я хочу создавать клиентов, чтобы они отображались в системе")
    void testCreateCustomer() {
        CustomerRequest request = new CustomerRequest(
                "Acme Corp",
                "John Doe",
                "CEO",
                "123 Main St",
                "Springfield",
                "IL",
                "62704",
                "USA",
                "+1-555-123-4567",
                "+1-555-987-6543"
        );

        CustomerResponse response = given()
                .header("Authorization", "Bearer " + validBearerToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/customers")
                .then()
                .statusCode(201)
                .body(
                        "customerId", notNullValue(),
                        "companyName", equalTo("Acme Corp"),
                        "contactName", equalTo("John Doe"),
                        "contactTitle", equalTo("CEO"),
                        "city", equalTo("Springfield"),
                        "country", equalTo("USA"))
                .extract()
                .as(CustomerResponse.class);

        assertThat(response.getCustomerId()).isNotNull();
        assertThat(response.getCompanyName()).isEqualTo("Acme Corp");
    }
}
