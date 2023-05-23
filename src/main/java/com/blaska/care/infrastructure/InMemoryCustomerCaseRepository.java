package com.blaska.care.infrastructure;

import com.blaska.care.domain.CustomerCase;
import com.blaska.care.domain.CustomerCaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryCustomerCaseRepository implements CustomerCaseRepository {
    private final Map<String, List<CustomerCase>> db = new HashMap<>();
    private final AtomicLong identifier = new AtomicLong(0L);

    @Override
    public Optional<List<CustomerCase>> findAllCustomerCases(final String customerId) {
        return Optional.ofNullable(db.get(customerId));
    }

    @Override
    public long save(final CustomerCase customerCase) {
        long nextId = identifier.incrementAndGet();
        List<CustomerCase> customerCases = db.get(customerCase.getCustomerId());
        if (CollectionUtils.isEmpty(customerCases)) {
            customerCases = new ArrayList<>();
        }

        customerCases.add(customerCase.withCaseId(nextId));
        db.put(customerCase.getCustomerId(), customerCases);
        return nextId;
    }
}
