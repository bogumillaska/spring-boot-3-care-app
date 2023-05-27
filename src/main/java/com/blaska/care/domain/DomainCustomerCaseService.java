package com.blaska.care.domain;

import com.blaska.care.exception.CustomerCaseNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;

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
    public List<CustomerCase> findByCustomer(final String customerId) {
        return customerCaseRepository.findAllCustomerCases(customerId)
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean addMessageToCase(final String clientId, final long caseId, final Message message) {
        var customerCase = customerCaseRepository.findById(clientId, caseId)
                .orElseThrow(() -> new CustomerCaseNotFoundException(
                        String.format("Client case number %s not found for customer %d", clientId, caseId)));

        customerCase.getMessages()
                .add(message);
        customerCaseRepository.update(customerCase);
        return true;
    }

    @Override
    public void updateCase(final String clientId, final long caseId, final CustomerCase customerCasePatch) {
        var customerCase = customerCaseRepository.findById(clientId, caseId)
                .orElseThrow(() -> new CustomerCaseNotFoundException(
                        String.format("Client case number %s not found for customer %d", clientId, caseId)));

        var updatedCase = mapPatchToCustomerCase(customerCase, customerCasePatch);
        customerCaseRepository.update(updatedCase);
    }

    private CustomerCase mapPatchToCustomerCase(final CustomerCase customerCase, final CustomerCase customerCasePatch) {
        var updatedCustomerCaseBuilder = customerCase.toBuilder();
        ofNullable(customerCasePatch.getCustomerReference())
                .ifPresent(updatedCustomerCaseBuilder::customerReference);

        return updatedCustomerCaseBuilder.build();
    }
}
