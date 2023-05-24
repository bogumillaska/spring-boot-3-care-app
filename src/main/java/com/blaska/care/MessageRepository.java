package com.blaska.care;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    long store(Message message);

    Optional<Message> findById(long messageId);
    List<Message> findByClientId(String clientId);
}
