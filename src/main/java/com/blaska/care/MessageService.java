package com.blaska.care;

import com.blaska.care.exception.MessageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public long store(final Message messageInput) {
        return messageRepository.store(messageInput);
    }

    public Message find(final long messageId) {
        return messageRepository.findById(messageId)
                .map(messageMapper::mapFrom)
                .orElseThrow(() -> new MessageNotFoundException("message not found"));
    }
}
