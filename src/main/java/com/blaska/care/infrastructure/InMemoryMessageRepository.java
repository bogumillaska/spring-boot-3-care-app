package com.blaska.care.infrastructure;

import com.blaska.care.domain.Message;
import com.blaska.care.domain.MessageRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Repository
public class InMemoryMessageRepository implements MessageRepository {

    private final MessageMapper messageMapper;

    private final Map<String, List<DBMessage>> messageStore = new ConcurrentHashMap<>();
    private final Map<Long, DBMessage> messageByIdIndex = new ConcurrentHashMap<>();
    private final AtomicLong identifier = new AtomicLong(0L);

    InMemoryMessageRepository(final MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }


    @Override
    public long store(final Message message) {
        long nextId = identifier.incrementAndGet();
        List<DBMessage> messages = messageStore.get(message.getSender());
        if (CollectionUtils.isEmpty(messages)) {
            messages = new ArrayList<>();
        }
        var messageToSave = mapMessageRecord(nextId, message);
        messages.add(messageToSave);
        messageStore.put(message.getSender(), messages);
        messageByIdIndex.put(nextId, messageToSave);
        return nextId;
    }

    @Override
    public Optional<Message> findById(final long messageId) {
        return ofNullable(messageByIdIndex.get(messageId))
                .map(messageMapper::mapFrom);
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
                .customerId(message.getSender())
                .message(message.getMessage())
                .build();
    }
}
