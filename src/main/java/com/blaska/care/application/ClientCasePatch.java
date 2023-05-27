package com.blaska.care.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ClientCasePatch {

    @JsonProperty("client-reference")
    private String clientReference;
}
