package com.blaska.care.application;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDTO {
    private String message;
}
