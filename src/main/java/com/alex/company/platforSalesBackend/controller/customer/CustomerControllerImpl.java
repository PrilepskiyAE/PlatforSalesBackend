package com.alex.company.platforSalesBackend.controller.customer;

import com.alex.company.platforSalesBackend.dto.customer.CustomerRequest;
import com.alex.company.platforSalesBackend.dto.customer.CustomerResponse;
import com.alex.company.platforSalesBackend.service.customer.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@SecurityRequirement(name = "bearer-jwt")
@Validated
@Tag(name = "Customer", description = "API для управления покупателями")
public class CustomerControllerImpl implements CustomerController {

    private final CustomerService customerService;

    public CustomerControllerImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(
            summary = "Создать покупателя",
            description = "Создаёт нового покупателя"
    )
    @ApiResponse(responseCode = "201", description = "Покупатель успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @Override
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить покупателя по ID",
            description = "Возвращает покупателя по его идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Покупатель найден")
    @ApiResponse(responseCode = "400", description = "Некорректный ID покупателя")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "404", description = "Покупатель не найден")
    @Override
    public ResponseEntity<CustomerResponse> getCustomerById(@Parameter(description = "ID покупателя", example = "1")
                                                                @PathVariable String id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Получить всех покупателей",
            description = "Возвращает список всех покупателей"
    )
    @ApiResponse(responseCode = "200", description = "Список покупателей успешно получен")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @Override
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> response = customerService.getAllCustomer();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить покупателя",
            description = "Удаляет покупателя по его идентификатору"
    )
    @ApiResponse(responseCode = "204", description = "Покупатель успешно удалён")
    @ApiResponse(responseCode = "400", description = "Некорректный ID покупателя")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "404", description = "Покупатель не найден")
    @Override
    public ResponseEntity<Void> deleteCustomer(String id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
