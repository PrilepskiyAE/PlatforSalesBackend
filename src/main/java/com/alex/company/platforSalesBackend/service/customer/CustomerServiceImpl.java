package com.alex.company.platforSalesBackend.service.customer;

import com.alex.company.platforSalesBackend.dto.customer.CustomerRequest;
import com.alex.company.platforSalesBackend.dto.customer.CustomerResponse;

import java.util.List;

public class CustomerServiceImpl implements CustomerService{
    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        return null;
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        return null;
    }

    @Override
    public List<CustomerResponse> getAllCustomer() {
        return List.of();
    }

    @Override
    public void deleteCustomer(Long id) {

    }
}
