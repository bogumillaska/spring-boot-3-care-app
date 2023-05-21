package com.blaska.care;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Slf4j
class MessageResource {

    private static final Logger logger = LoggerFactory.getLogger(MessageResource.class);

    private final MessageService messageService;

    @PostMapping
    ResponseEntity<Object> createMessage(@RequestBody MessageRequest messageInput) {
        logger.info("Creating message with input : {}", messageInput);
        final long messageId = messageService.store(messageInput);
        return ResponseEntity
                .created(URI.create("message/" + messageId))
                .build();
    }
}
