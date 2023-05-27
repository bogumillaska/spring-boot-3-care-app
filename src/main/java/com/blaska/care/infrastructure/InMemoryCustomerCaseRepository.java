package com.blaska.care.infrastructure;

import com.blaska.care.domain.CustomerCase;
import com.blaska.care.domain.CustomerCaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Optional.ofNullable;

@Repository
public class InMemoryCustomerCaseRepository implements CustomerCaseRepository {
    private final Map<String, List<CustomerCase>> db = new HashMap<>();
    private final Map<Long, CustomerCase> caseIdIndex = new HashMap<>();
    private final AtomicLong identifier = new AtomicLong(0L);

    @Override
    public Optional<CustomerCase> findById(final String clientId, final long caseId) {
        return ofNullable(db.get(clientId))
                .orElse(Collections.emptyList())
                .stream()
                .filter(clientCase -> caseId == clientCase.getCaseId())
                .findAny();
    }

    @Override
    public Optional<List<CustomerCase>> findAllCustomerCases(final String customerId) {
        return ofNullable(db.get(customerId));
    }

    @Override
    public long save(final CustomerCase customerCase) {
        long nextId = identifier.incrementAndGet();
        List<CustomerCase> customerCases = db.get(customerCase.getCustomerReference());
        if (CollectionUtils.isEmpty(customerCases)) {
            customerCases = new ArrayList<>();
        }

        customerCases.add(customerCase.withCaseId(nextId));
        db.put(customerCase.getCustomerReference(), customerCases);
        caseIdIndex.put(nextId, customerCase);
        return nextId;
    }

    @Override
    public void update(final CustomerCase customerCase) {
        caseIdIndex.put(customerCase.getCaseId(), customerCase);
    }
}
