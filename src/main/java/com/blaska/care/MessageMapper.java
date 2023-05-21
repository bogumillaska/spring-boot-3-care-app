package com.blaska.care;

import com.blaska.care.model.DBMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public Message mapFrom(DBMessage storedMessage){
        return Message.builder()
                .messageId(storedMessage.getId())
                .message(storedMessage.getMessage())
                .build();
    }
}
