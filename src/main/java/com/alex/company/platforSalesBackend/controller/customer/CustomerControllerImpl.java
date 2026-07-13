package com.alex.company.platforSalesBackend.controller.customer;

import com.alex.company.platforSalesBackend.dto.customer.CustomerRequest;
import com.alex.company.platforSalesBackend.dto.customer.CustomerResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CustomerControllerImpl implements CustomerController {
    @Override
    public ResponseEntity<CustomerResponse> createCustomer(CustomerRequest request) {
        return null;
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomerById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(Long id) {
        return null;
    }
}
