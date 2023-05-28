package com.blaska.care.application;

import com.blaska.authorization.AuthorizationService;
import com.blaska.care.domain.Message;
import com.blaska.care.domain.MessageService;
import com.blaska.care.exception.MessageNotFoundException;
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
        var sender = authorizationService.getCredentialsFromToken(authorizationHeader);

        var messageId = messageService.store(mapToDomain(sender, messageRequest));
        return ResponseEntity.created(URI.create("messages/" + messageId))
                .body(new MessageResponse(messageId));
    }

    @GetMapping("messages")
    public ResponseEntity<List<MessageDTO>> getMessages(@RequestHeader(AUTHORIZATION) String authorizationHeader) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var clientId = authorizationService.getCredentialsFromToken(authorizationHeader);

        var messages = messageService.findByCustomer(clientId);
        var messagesDTO = messages.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messagesDTO);
    }

    @GetMapping("messages/{id}")
    public ResponseEntity<MessageDTO> getMessage(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                                 @PathVariable("id") long messageId) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var clientId = authorizationService.getCredentialsFromToken(authorizationHeader);

        var messages = messageService.findByCustomer(clientId);
        var messagesDTO = messages.stream()
                .filter(message -> message.getMessageId() == messageId)
                .map(this::mapToDto)
                .findAny()
                .orElseThrow(() -> new MessageNotFoundException("Message with provided id does not exist"));
        return ResponseEntity.ok(messagesDTO);
    }

    private MessageDTO mapToDto(Message message) {
        return MessageDTO.builder()
                .message(message.getMessage())
                .build();
    }


    private Message mapToDomain(String sender, MessageRequest messageRequest) {
        return Message.builder()
                .sender(sender)
                .message(messageRequest.getMessage())
                .build();
    }
}
