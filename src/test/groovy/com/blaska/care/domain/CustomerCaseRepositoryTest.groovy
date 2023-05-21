package com.blaska.care.domain


import spock.lang.Specification

class CustomerCaseRepositoryTest extends Specification {

    CustomerCaseRepository customerCaseRepository = new InMemoryCustomerCaseRepository()

    def "should be able to save and fetch customer cases"() {
        given:
            String customerId = "1234"
            String customerName = "Jérémie Durand"
        and:
            CustomerCase customerCase = new CustomerCase(customerId, customerName)
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
