package com.blaska.care;

import com.blaska.care.model.DBMessage;

import java.util.Optional;

public interface MessageRepository {
    long store(MessageRequest message);

    Optional<DBMessage> findById(long messageId);
}
