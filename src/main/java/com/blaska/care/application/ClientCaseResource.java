package com.blaska.care.application;

import com.blaska.care.Message;
import com.blaska.care.domain.CustomerCase;
import com.blaska.care.domain.CustomerCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientCaseResource {

    private final CustomerCaseService customerCaseService;

    @PostMapping("client/{clientId}/case")
    public ResponseEntity<Object> createClientCase(@PathVariable("clientId") String clientId,
                                                   @RequestBody ClientCaseRequest clientCaseRequest) {
        customerCaseService.createCustomerCase(mapToDomain(clientId, clientCaseRequest));
        return ResponseEntity.created(URI.create("client/" + clientId + "/case/1")).build();
    }

    private CustomerCase mapToDomain(String clientId, ClientCaseRequest clientCaseRequest) {
        var initialMessage = Message.builder()
                .message(clientCaseRequest.getMessage())
                .build();

        return CustomerCase.builder()
                .customerId(clientId) // FIXME
                .customerName(clientId)
                .messages(List.of(initialMessage))
                .build();
    }
}
