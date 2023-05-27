package com.blaska.care.domain;

import java.util.List;

public interface CustomerCaseService {

    long createCustomerCase(final CustomerCase customerCase);
    List<CustomerCase> findByCustomer(String customerId);

    boolean addMessageToCase(final String clientId, long caseId, Message message);

    void updateCase(String clientId, long caseId, CustomerCase customerCase);
}
