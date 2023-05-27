package com.blaska.care.application;

import com.blaska.authorization.AuthorizationService;
import com.blaska.care.domain.Message;
import com.blaska.care.domain.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
public class MessageResource {

    private final MessageService messageService;
    private final AuthorizationService authorizationService;

    @PostMapping("messages")
    public ResponseEntity<Object> sendMessage(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                              @RequestBody MessageRequest messageRequest) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var clientId = authorizationService.getClientIdFromToken(authorizationHeader);

        var messageId = messageService.store(mapToDomain(clientId, messageRequest));
        return ResponseEntity.created(URI.create("messages/" + messageId)).build();
    }

    @GetMapping("messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@RequestHeader(AUTHORIZATION) String authorizationHeader) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var clientId = authorizationService.getClientIdFromToken(authorizationHeader);

        var messages = messageService.findByCustomer(clientId);
        var messagesDTO = messages.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messagesDTO);
    }

    private MessageDTO mapToDto(Message message) {
        return MessageDTO.builder()
                .message(message.getMessage())
                .build();
    }


    private Message mapToDomain(String clientId, MessageRequest messageRequest) {
        return Message.builder()
                .customerId(clientId)
                .message(messageRequest.getMessage())
                .build();
    }
}
