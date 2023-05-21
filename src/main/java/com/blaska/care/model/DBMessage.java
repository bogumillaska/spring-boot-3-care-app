package com.blaska.care.model;

import lombok.Builder;
import lombok.Getter;
import lombok.With;

@Getter
@Builder
@With
public class DBMessage {
    private long id;
    private String customerId;
    private String message;
}
