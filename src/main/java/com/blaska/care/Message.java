package com.blaska.care;

import lombok.Builder;

@Builder
public class Message {
    private long messageId;
    private String customerName;
    private String message;
}
