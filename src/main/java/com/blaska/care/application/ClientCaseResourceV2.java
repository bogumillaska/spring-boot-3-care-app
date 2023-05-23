package com.blaska.care.application;

import com.blaska.authorization.AuthorizationService;
import com.blaska.care.Message;
import com.blaska.care.domain.CustomerCase;
import com.blaska.care.domain.CustomerCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class ClientCaseResourceV2 {

    private final CustomerCaseService customerCaseService;
    private final AuthorizationService authorizationService;

    @PostMapping("cases")
    public ResponseEntity<Object> createClientCase(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                                   @RequestBody ClientCaseRequest clientCaseRequest) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
        }

        String clientId = authorizationService.getClientIdFromToken(authorizationHeader);

        var caseId = customerCaseService.createCustomerCase(mapToDomain(clientId, clientCaseRequest));
        return ResponseEntity.created(URI.create("/case/" + caseId)).build();
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
