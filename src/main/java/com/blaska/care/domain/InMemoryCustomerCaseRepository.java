package com.blaska.care.domain;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Repository
public class InMemoryCustomerCaseRepository implements CustomerCaseRepository {
    private final Map<String, List<CustomerCase>> db = new HashMap<>();

    @Override
    public Optional<List<CustomerCase>> findAllCustomerCases(final String customerId) {
        return Optional.ofNullable(db.get(customerId));
    }

    @Override
    public void save(final CustomerCase customerCase) {
        List<CustomerCase> customerCases = db.get(customerCase.getCustomerId());
        if (CollectionUtils.isEmpty(customerCases)) {
            customerCases = new ArrayList<>();
        }
        customerCases.add(customerCase);
        db.put(customerCase.getCustomerId(), customerCases);
    }
}
