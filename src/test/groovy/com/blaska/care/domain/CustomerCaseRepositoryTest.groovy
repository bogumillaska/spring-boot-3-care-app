package com.blaska.care.domain

import com.blaska.care.infrastructure.InMemoryCustomerCaseRepository
import spock.lang.Specification

class CustomerCaseRepositoryTest extends Specification {

    CustomerCaseRepository customerCaseRepository = new InMemoryCustomerCaseRepository()

    def "should be able to save and fetch customer cases"() {
        given:
            String customerId = "1234"
            String customerName = "Jérémie Durand"
        and:
            CustomerCase customerCase = CustomerCase.builder()
                    .customerId(customerId)
                    .customerName(customerName)
                    .build()
        when:
            customerCaseRepository.save(customerCase)
        then:
            Optional<List<CustomerCase>> savedCustomerCase = customerCaseRepository.findAllCustomerCases(customerId)
        and:
            savedCustomerCase.isPresent()
        and:
            savedCustomerCase.get().size() == 1

    }
}
