package com.blaska.care;

import com.blaska.care.domain.CustomerCase;
import com.blaska.care.domain.CustomerCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/case")
@RequiredArgsConstructor
class CaseResource {

    private final DomainMessageService messageService;
    private final CustomerCaseService customerCaseService;

    @GetMapping
    ResponseEntity<List<CustomerCase>> getCustomerCases(@RequestParam("customer") String customerId) {
        return ResponseEntity.ok(customerCaseService.findByCustomer(customerId));
    }

    @PostMapping
    ResponseEntity<Object> createFromMessage(@RequestBody CreateCaseRequest createCaseRequest) {
        Optional<Message> message = messageService.find(createCaseRequest.getMessageId());
        return ResponseEntity.created(URI.create("case/1")).build();
    }
}
