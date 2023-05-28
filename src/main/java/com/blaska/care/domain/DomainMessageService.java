package com.blaska.care.domain;

import com.blaska.care.exception.MessageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomainMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public long store(final Message messageInput) {
        return messageRepository.store(messageInput);
    }

    public Message find(final long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("message not found"));
    }

    @Override
    public List<Message> findByCustomer(final String customerId) {
        return messageRepository.findByClientId(customerId);
    }
}
