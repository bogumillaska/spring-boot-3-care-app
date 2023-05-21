package com.blaska.care.domain;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerCaseService {

    void createCustomerCase();
    void appendMessageToCase();
    List<CustomerCase> findByCustomer(String customerId);
}
