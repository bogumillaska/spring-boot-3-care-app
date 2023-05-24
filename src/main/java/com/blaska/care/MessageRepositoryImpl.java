package com.blaska.care;

import com.blaska.care.model.DBMessage;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    private final MessageMapper messageMapper;

    private final Map<String, List<DBMessage>> messageStore = new ConcurrentHashMap<>();
    private final AtomicLong identifier = new AtomicLong(0L);

    MessageRepositoryImpl(final MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }


    @Override
    public long store(final Message message) {
        long nextId = identifier.incrementAndGet();
        List<DBMessage> messages = messageStore.get(message.getCustomerId());
        if (CollectionUtils.isEmpty(messages)) {
            messages = new ArrayList<>();
        }
        messages.add(mapMessageRecord(nextId, message));
        messageStore.put(message.getCustomerId(), messages);
        return nextId;
    }

    @Override
    public Optional<Message> findById(final long messageId) {
        return empty();
    }

    @Override
    public List<Message> findByClientId(final String clientId) {
        return ofNullable(messageStore.get(clientId))
                .orElseGet(Collections::emptyList)
                .stream()
                .map(messageMapper::mapFrom)
                .collect(Collectors.toList());
    }

    private DBMessage mapMessageRecord(final long nextId, final Message message) {
        return DBMessage.builder()
                .id(nextId)
                .customerId(message.getCustomerId())
                .message(message.getMessage())
                .build();
    }
}
