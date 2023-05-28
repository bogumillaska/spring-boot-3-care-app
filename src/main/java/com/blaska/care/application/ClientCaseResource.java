package com.blaska.care.application;

import com.blaska.authorization.AuthorizationService;
import com.blaska.care.domain.CustomerCase;
import com.blaska.care.domain.CustomerCaseService;
import com.blaska.care.domain.Message;
import com.blaska.care.domain.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("cases")
@RequiredArgsConstructor
public class ClientCaseResource {

    private final CustomerCaseService customerCaseService;
    private final MessageService messageService;
    private final AuthorizationService authorizationService;

    @PostMapping
    public ResponseEntity<Object> createClientCase(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                                   @RequestBody ClientCaseRequest clientCaseRequest) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!authorizationService.isCsrToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var message = messageService.find(clientCaseRequest.getMessageId());

        var caseId = customerCaseService.createCustomerCase(mapToCustomerCase(message));
        return ResponseEntity.created(URI.create("/cases/" + caseId)).build();
    }

    @PostMapping("/{caseId}/messages")
    public ResponseEntity<Object> appendMessageToCase(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                                      @PathVariable("caseId") long caseId,
                                                      @RequestBody MessageRequest messageRequest) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String sender = authorizationService.getCredentialsFromToken(authorizationHeader);

        var result = customerCaseService.addMessageToCase(caseId, mapMessageToDomain(sender, messageRequest));
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

        customerCaseService.updateCase(caseId, mapPatchToDomain(clientCasePatch));
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{clientReference}", produces="application/json")
    public ResponseEntity<List<ClientCaseDTO>> getCasesByClientReference(@RequestHeader(AUTHORIZATION) String authorizationHeader,
                                                                         @PathVariable("clientReference") String customerReference) {
        if (!authorizationService.isValidToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!authorizationService.isCsrToken(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var cases = customerCaseService.findByCustomer(customerReference).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(cases);
    }

    private ClientCaseDTO mapToDTO(final CustomerCase customerCase) {
        var clientCaseDTOBuilder = ClientCaseDTO.builder()
                .caseId(customerCase.getCaseId())
                .customerName(customerCase.getCustomerName())
                .customerReference(customerCase.getCustomerReference());

        var messageDTOs = customerCase.getMessages().stream()
                .map(m -> MessageDTO.builder()
                        .message(m.getMessage())
                        .sender(m.getSender())
                        .build())
                .collect(Collectors.toList());

        clientCaseDTOBuilder.messages(messageDTOs);
        return clientCaseDTOBuilder.build();
    }

    private CustomerCase mapPatchToDomain(final ClientCasePatch clientCasePatch) {
        return CustomerCase.builder()
                .customerReference(clientCasePatch.getClientReference())
                .build();
    }

    private CustomerCase mapToCustomerCase(Message message) {
        List<Message> messageList = new ArrayList<>();
        messageList.add(Message.builder()
                .sender(message.getSender())
                .message(message.getMessage())
                .build());

        return CustomerCase.builder()
                .customerName(message.getSender())
                .messages(messageList)
                .build();
    }

    private Message mapMessageToDomain(final String sender, MessageRequest message) {
        return Message.builder()
                .sender(sender)
                .message(message.getMessage())
                .build();
    }

}
