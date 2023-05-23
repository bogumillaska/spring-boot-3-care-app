package com.blaska.care.application;

import com.blaska.authorization.AuthorizationService;
import com.blaska.care.Message;
import com.blaska.care.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class MessageResourceV2 {

    private final MessageService messageService;
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


    private Message mapToDomain(String clientId, MessageRequest messageRequest) {
        return Message.builder()
                .customerName(clientId)
                .message(messageRequest.getMessage())
                .build();
    }
}
