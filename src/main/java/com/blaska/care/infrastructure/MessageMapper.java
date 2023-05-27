package com.blaska.care.infrastructure;

import com.blaska.care.domain.Message;
import org.springframework.stereotype.Component;

@Component
class MessageMapper {

    Message mapFrom(DBMessage storedMessage){
        return Message.builder()
                .messageId(storedMessage.getId())
                .message(storedMessage.getMessage())
                .build();
    }
}
