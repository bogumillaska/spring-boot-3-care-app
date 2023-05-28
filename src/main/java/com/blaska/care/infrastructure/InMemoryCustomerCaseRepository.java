package com.blaska.care.infrastructure;

import com.blaska.care.domain.CustomerCase;
import com.blaska.care.domain.CustomerCaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

@Repository
public class InMemoryCustomerCaseRepository implements CustomerCaseRepository {
    private final Map<String, List<CustomerCase>> db = new HashMap<>();
    private final Map<Long, CustomerCase> caseIdIndex = new HashMap<>();
    private final Map<String, List<CustomerCase>> customerReferenceIndex = new HashMap<>();
    private final AtomicLong identifier = new AtomicLong(0L);

    @Override
    public Optional<CustomerCase> findById(final long caseId) {
        return ofNullable(caseIdIndex.get(caseId));
    }

    @Override
    public Optional<List<CustomerCase>> findAllCustomerCases(final String customerReference) {
        return ofNullable(customerReferenceIndex.get(customerReference));
    }

    @Override
    public long save(final CustomerCase customerCase) {
        long nextId = identifier.incrementAndGet();
        List<CustomerCase> customerCases = db.get(customerCase.getCustomerName());
        if (CollectionUtils.isEmpty(customerCases)) {
            customerCases = new ArrayList<>();
        }

        var customerCaseToSave = customerCase.withCaseId(nextId);
        customerCases.add(customerCaseToSave);
        db.put(customerCase.getCustomerName(), customerCases);
        caseIdIndex.put(nextId, customerCaseToSave);
        return nextId;
    }

    @Override
    public void update(final CustomerCase customerCase) {
        caseIdIndex.put(customerCase.getCaseId(), customerCase);
        if (nonNull(customerCase.getCustomerReference())) {
            updateCustomerReference(customerCase);
        }
    }

    private void updateCustomerReference(final CustomerCase customerCase) {
        List<CustomerCase> customerCases = db.get(customerCase.getCustomerReference());
        if (CollectionUtils.isEmpty(customerCases)) {
            customerCases = new ArrayList<>();
        }
        customerCases.add(customerCase.withCustomerReference(customerCase.getCustomerReference()));
        customerReferenceIndex.put(customerCase.getCustomerReference(), customerCases);
    }
}
