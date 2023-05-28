package com.blaska.care.domain;

import java.util.List;

public interface MessageService {
    long store(final Message messageInput);

    Message find(final long messageId);
    List<Message> findByCustomer(final String customerId);
}
