package com.blaska.care;

import com.blaska.care.exception.MessageNotFoundException;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    long store(final Message messageInput);

    Optional<Message> find(final long messageId);
    List<Message> findByCustomer(final String customerId);
}
