package com.blaska.care.domain;

import java.util.List;

public interface CustomerCaseService {

    long createCustomerCase(final CustomerCase customerCase);
    List<CustomerCase> findByCustomer(String customerReference);

    boolean addMessageToCase(long caseId, Message message);

    void updateCase(long caseId, CustomerCase customerCase);
}
