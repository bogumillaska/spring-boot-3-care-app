package com.blaska.care;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Message {
    private long messageId;
    private String customerId;
    private String message;
}
