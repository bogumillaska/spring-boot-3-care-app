package com.blaska.care;

import com.blaska.care.exception.MessageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DomainMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public long store(final Message messageInput) {
        return messageRepository.store(messageInput);
    }

    public Optional<Message> find(final long messageId) {
        return Optional.ofNullable(messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("message not found")));
    }

    @Override
    public List<Message> findByCustomer(final String customerId) {
        return messageRepository.findByClientId(customerId);
    }
}
