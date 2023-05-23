package com.blaska.care.domain;

import com.blaska.care.application.ClientCaseRequest;

import java.util.List;

public interface CustomerCaseService {

    long createCustomerCase(final CustomerCase customerCase);
    void appendMessageToCase();
    List<CustomerCase> findByCustomer(String customerId);
}
