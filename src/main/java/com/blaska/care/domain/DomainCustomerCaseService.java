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
    public List<CustomerCase> findByCustomer(final String customerReference) {
        return customerCaseRepository.findAllCustomerCases(customerReference)
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean addMessageToCase(final long caseId, final Message message) {
        var customerCase = customerCaseRepository.findById(caseId)
                .orElseThrow(() -> new CustomerCaseNotFoundException(
                        String.format("Client case number %s", caseId)));

        customerCase.getMessages()
                .add(message);
        customerCaseRepository.update(customerCase);
        return true;
    }

    @Override
    public void updateCase(final long caseId, final CustomerCase customerCasePatch) {
        var customerCase = customerCaseRepository.findById(caseId)
                .orElseThrow(() -> new CustomerCaseNotFoundException(
                        String.format("Client case number %s", caseId)));

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
