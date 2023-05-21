package com.blaska.care;

import com.blaska.care.model.DBMessage;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Optional.ofNullable;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private final Map<Long, DBMessage> messageStore = new ConcurrentHashMap<>();
    private final AtomicLong identifier = new AtomicLong(0L);

    @Override
    public long store(final MessageRequest message) {
        long nextId = identifier.incrementAndGet();
        var messageRecord = mapMessageRecord(nextId, message);
        messageStore.put(nextId, messageRecord);
        return nextId;
    }

    @Override
    public Optional<DBMessage> findById(final long messageId) {
        return ofNullable(messageStore.get(messageId));
    }

    private DBMessage mapMessageRecord(final long nextId, final MessageRequest message) {
        return DBMessage.builder()
                .id(nextId)
                .customerId(message.getCustomerName())
                .message(message.getMessage())
                .build();
    }
}
