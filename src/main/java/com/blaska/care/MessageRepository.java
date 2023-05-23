package com.blaska.care;

import com.blaska.care.application.MessageRequest;
import com.blaska.care.model.DBMessage;

import java.util.Optional;

public interface MessageRepository {
    long store(Message message);

    Optional<DBMessage> findById(long messageId);
}
