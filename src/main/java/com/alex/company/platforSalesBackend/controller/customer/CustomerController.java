package com.alex.company.platforSalesBackend.controller.customer;

import com.alex.company.platforSalesBackend.dto.category.CategoryRequest;
import com.alex.company.platforSalesBackend.dto.category.CategoryResponse;
import com.alex.company.platforSalesBackend.dto.customer.CustomerRequest;
import com.alex.company.platforSalesBackend.dto.customer.CustomerResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CustomerController {
    ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request);

    ResponseEntity<CustomerResponse> getCustomerById(String id);

    ResponseEntity<List<CustomerResponse>> getAllCustomers();

    ResponseEntity<Void> deleteCustomer(String id);
}
