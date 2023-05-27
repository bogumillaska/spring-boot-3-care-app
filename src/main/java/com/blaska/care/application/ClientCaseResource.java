package com.blaska.care.application;

import com.blaska.authorization.AuthorizationService;
import com.blaska.care.domain.CustomerCase;
import com.blaska.care.domain.CustomerCaseService;
import com.blaska.care.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("cases")
@RequiredArgsConstructor
public class ClientCaseResource {

    private final CustomerCaseService customerCaseService;
    private final AuthorizationService authorizationService;

    @PostMapping
    public ResponseEntity<Object> createClientCase(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                                   @RequestBody ClientCaseRequest clientCaseRequest) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
        }

        String clientId = authorizationService.getClientIdFromToken(authorizationHeader);

        var caseId = customerCaseService.createCustomerCase(mapToDomain(clientId, clientCaseRequest));
        return ResponseEntity.created(URI.create("/case/" + caseId)).build();
    }

    @PostMapping("/{caseId}/messages")
    public ResponseEntity<Object> appendMessageToCase(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                                      @PathVariable("caseId") long caseId,
                                                      @RequestBody ClientCaseRequest clientCaseRequest) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
        }

        String clientId = authorizationService.getClientIdFromToken(authorizationHeader);

        var result = customerCaseService.addMessageToCase(clientId, caseId, mapMessageToDomain(clientCaseRequest));
        if (result)
            return ResponseEntity.status(HttpStatus.CREATED).build();
        else
            return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{caseId}")
    public ResponseEntity<Object> updateCase(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                             @PathVariable("caseId") long caseId,
                                             @RequestBody ClientCasePatch clientCasePatch) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
        }

        String clientId = authorizationService.getClientIdFromToken(authorizationHeader);

        customerCaseService.updateCase(clientId, caseId, mapPatchToDomain(clientCasePatch));
        return ResponseEntity.ok().build();
    }

    private CustomerCase mapPatchToDomain(final ClientCasePatch clientCasePatch) {
        return CustomerCase.builder()
                .customerReference(clientCasePatch.getClientReference())
                .build();
    }

    private CustomerCase mapToDomain(String clientId, ClientCaseRequest clientCaseRequest) {
        var initialMessage = mapMessageToDomain(clientCaseRequest);

        return CustomerCase.builder()
                .customerName(clientId)
                .customerReference(clientCaseRequest.getClientReference())
                .messages(List.of(initialMessage))
                .build();
    }

    private Message mapMessageToDomain(ClientCaseRequest clientCaseRequest) {
        return Message.builder()
                .message(clientCaseRequest.getMessage())
                .build();
    }

}
