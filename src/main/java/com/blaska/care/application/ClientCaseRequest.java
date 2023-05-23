package com.blaska.care.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ClientCaseRequest {
    @JsonProperty("message")
    private String message;
}
