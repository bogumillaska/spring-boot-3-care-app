package com.blaska.care.infrastructure;

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
