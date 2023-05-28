package com.blaska.care.domain;

import java.util.List;
import java.util.Optional;

public interface CustomerCaseRepository {

    Optional<CustomerCase> findById(long caseId);
    Optional<List<CustomerCase>> findAllCustomerCases(String customerId);
    long save(CustomerCase customerCase);
    void update(CustomerCase customerCase);
}
