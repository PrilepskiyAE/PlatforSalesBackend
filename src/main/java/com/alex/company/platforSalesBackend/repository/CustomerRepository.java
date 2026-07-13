package com.alex.company.platforSalesBackend.repository;

import com.alex.company.platforSalesBackend.entity.CategoryEntity;
import com.alex.company.platforSalesBackend.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,  String> {}
