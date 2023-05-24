package com.blaska.care.application;

import com.blaska.authorization.AuthorizationService;
import com.blaska.care.Message;
import com.blaska.care.DomainMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class MessageResourceV2 {

    private final DomainMessageService messageService;
    private final AuthorizationService authorizationService;

    @PostMapping("messages")
    public ResponseEntity<Object> sendMessage(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                              @RequestBody MessageRequest messageRequest) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
        }
        var clientId = authorizationService.getClientIdFromToken(authorizationHeader);

        var messageId = messageService.store(mapToDomain(clientId, messageRequest));
        return ResponseEntity.created(URI.create("messages/" + messageId)).build();
    }

    @GetMapping("messages")
    public ResponseEntity<Object> getMessages(@RequestHeader(AUTHORIZATION) String authorizationHeader) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authorized");
        }
        var clientId = authorizationService.getClientIdFromToken(authorizationHeader);

        var messages = messageService.findByCustomer(clientId);
        return ResponseEntity.ok(messages);
    }


    private Message mapToDomain(String clientId, MessageRequest messageRequest) {
        return Message.builder()
                .customerId(clientId)
                .message(messageRequest.getMessage())
                .build();
    }
}
