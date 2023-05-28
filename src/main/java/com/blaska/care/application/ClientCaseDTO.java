package com.blaska.care.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCaseDTO {
    @JsonProperty("case-id")
    private long caseId;
    @JsonProperty("client-name")
    private String customerName;
    @JsonProperty("client-reference")
    private String customerReference;
    @JsonProperty("messages")
    private List<MessageDTO> messages;
}
