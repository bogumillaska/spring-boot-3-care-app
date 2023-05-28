package com.blaska.care.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Message {
    private long messageId;
    private String sender;
    private String message;
}
