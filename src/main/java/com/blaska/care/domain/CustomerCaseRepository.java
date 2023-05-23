package com.blaska.care.domain;

import java.util.List;
import java.util.Optional;

public interface CustomerCaseRepository {

    Optional<List<CustomerCase>> findAllCustomerCases(String customerId);
    long save(CustomerCase customerCase);
}
