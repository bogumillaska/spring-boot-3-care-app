package com.blaska.care.domain;

import com.blaska.care.application.ClientCaseRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DomainCustomerCaseService implements CustomerCaseService {

    private final CustomerCaseRepository customerCaseRepository;

    public DomainCustomerCaseService(final CustomerCaseRepository customerCaseRepository) {
        this.customerCaseRepository = customerCaseRepository;
    }

    @Override
    public long createCustomerCase(final CustomerCase customerCase) {
        return customerCaseRepository.save(customerCase);
    }

    @Override
    public void appendMessageToCase() {

    }

    @Override
    public List<CustomerCase> findByCustomer(final String customerId) {
        return customerCaseRepository.findAllCustomerCases(customerId)
                .orElse(Collections.emptyList());
    }
}
