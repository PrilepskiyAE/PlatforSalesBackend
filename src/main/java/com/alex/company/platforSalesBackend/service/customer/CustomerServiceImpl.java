package com.alex.company.platforSalesBackend.service.customer;

import com.alex.company.platforSalesBackend.dto.customer.CustomerRequest;
import com.alex.company.platforSalesBackend.dto.customer.CustomerResponse;
import com.alex.company.platforSalesBackend.entity.CustomerEntity;
import com.alex.company.platforSalesBackend.exception.CustomerNotFoundException;
import com.alex.company.platforSalesBackend.exception.UserNotFoundException;
import com.alex.company.platforSalesBackend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        CustomerEntity entity = CustomerRequest.fromEntity(request);
        CustomerEntity savedEntity = customerRepository.save(entity);
        return CustomerResponse.fromEntity(savedEntity);
    }

    @Override
    public CustomerResponse getCustomerById(String id) {
        CustomerEntity entity = customerRepository.findById(id).orElseThrow(() -> {
            logger.warn("Customer not found with id: {}", id);
            return new CustomerNotFoundException(id);
        });
        return CustomerResponse.fromEntity(entity);
    }

    @Override
    public List<CustomerResponse> getAllCustomer() {
        return customerRepository.findAll().stream().map(CustomerResponse::fromEntity).toList();
    }

    @Override
    @Transactional
    public void deleteCustomer(String id) {
        CustomerEntity entity  =  customerRepository.findById(id).orElseThrow(() -> {
            logger.warn("DELETE ERROR: Customer not found with id: {}", id);
            return new CustomerNotFoundException(id);
        });
        customerRepository.delete(entity);
    }
}
